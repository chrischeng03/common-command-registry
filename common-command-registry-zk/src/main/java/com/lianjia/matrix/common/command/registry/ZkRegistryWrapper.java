package com.lianjia.matrix.common.command.registry;

import com.alibaba.fastjson.JSON;
import com.lianjia.matrix.common.command.registry.config.ZkProperties;
import com.lianjia.matrix.common.command.registry.entity.Instruction;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.CommandException;
import com.lianjia.matrix.common.command.registry.exceptions.LifecycleException;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;
import com.lianjia.matrix.common.command.registry.listener.InstructionListener;
import com.lianjia.matrix.common.command.registry.listener.ProjectStatusListener;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author 程天亮
 * @Created
 */
public class ZkRegistryWrapper implements RegistryWrapper {
    protected static final String COMMAND_DATA_PATH = "/data/command";

    protected static final String PROJECT_LIFE_PATH = "/life";

    private boolean isBossSide;

//    protected String namespace;

    protected CuratorFramework client;

    private ZkProperties zooKeeper;

    private NodeCache instrutionCache;

    private PathChildrenCache projectCache;


    private static final Logger LOGGER = LoggerFactory.getLogger(ZkRegistryWrapper.class);

    private Project selfProject;

    private String authority;

    private InstructionListener instructionRecieveListener;

    private ProjectStatusListener projectStatusListener;

    public ZkRegistryWrapper(Project selfProject) {
        this.selfProject = selfProject;
    }

    public void setBossSide(boolean bossSide) {
        isBossSide = bossSide;
    }

    public void setInstructionRecieveListener(InstructionListener instructionRecieveListener) {
        this.instructionRecieveListener = instructionRecieveListener;
    }

    @Override
    public void sendInstruction(Project project, Instruction instruction) throws CommandException {
        Stat stat = new Stat();
        String path = COMMAND_DATA_PATH + "/" + project.getCode();
        try {
            client.getData().storingStatIn(stat).forPath(path);
            client.setData().withVersion(
                    stat.getVersion()).forPath(
                    path, instruction.toBytes());
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

    public void setProjectStatusListener(ProjectStatusListener projectStatusListener) {
        this.projectStatusListener = projectStatusListener;
    }

    @Override
    public List<Project> registedProjects() throws RegistryException {
        try {
            List<String> projectNodes = client.getChildren().forPath(PROJECT_LIFE_PATH);
            if (null != projectNodes) {
                List<Project> projects = new ArrayList<>();
                for (String projectNode : projectNodes) {
                    String path = PROJECT_LIFE_PATH + "/" + projectNode;
                    byte[] dataBytes = client.getData().forPath(path);
                    if (null != dataBytes) {
                        try {
                            parseJsonProject(projects, projectNode, dataBytes);
                        } catch (Exception e) {
                            parseSimpleProject(projects, projectNode, dataBytes);
                        }

                    }
                }
                return projects;
            }
            return null;
        } catch (Exception e) {
            throw new RegistryException(e);
        }
    }

    public ZkRegistryWrapper() {
    }

    public void setZooKeeper(ZkProperties zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void init() throws LifecycleException {
        this.authority = zooKeeper.getAuthority();
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.
                builder().
                connectString(zooKeeper.getConnectionString())
                .namespace(zooKeeper.getNamespace())
                .compressionProvider(new NoCompressProvider())
                .sessionTimeoutMs(zooKeeper.getSessionTimeoutMs())
                .connectionTimeoutMs(zooKeeper.getConnectionTimeoutMs())
                .retryPolicy(
                        new ExponentialBackoffRetry(zooKeeper.getBaseSleepTimeMs(),
                                zooKeeper.getMaxRetries(), zooKeeper.getMaxSleepMs()));
        if (authority != null && authority.length() > 0) {
            builder = builder.authorization("digest", authority.getBytes());
        }
        client = builder.build();
        try {
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    switch (newState) {
                        case LOST:
                            LOGGER.info("[Disconnect From ZK:{}]", zooKeeper);
                            break;
                        case READ_ONLY:
                            break;
                        case SUSPENDED:
                            break;
                        case CONNECTED:
                            LOGGER.info("[Connect to ZK:{}]", zooKeeper);
                            try {
                                registry(selfProject);
                            } catch (Exception e) {
                                LOGGER.info("[Init Error From Reconnect]", e);
                            }
                            break;
                        case RECONNECTED:
                            LOGGER.info("[Reconnected to ZK:{}]", zooKeeper);
                            try {
                                registry(selfProject);
                            } catch (Exception e) {
                                LOGGER.info("[Init Error From Reconnect]", e);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
            client.start();
            if (isBossSide) {
                initRegistryProjectsListeners(client);
            }
            if (selfProject == null) {
                synchronized (this) {
                    if (selfProject == null) return;
                }
            }
            doInit();
        } catch (Exception e) {
            throw new LifecycleException(e);
        }
    }

    @Override
    public void destroy() throws LifecycleException {
        CloseableUtils.closeQuietly(instrutionCache);
        CloseableUtils.closeQuietly(client);
    }

    @Override
    public void registry(Project project) throws CommandException {
        if (project == null) return;
        try {
//            synchronized (this) {
//                selfProject = project;
//            }
            String projectPath = PROJECT_LIFE_PATH + "/" + project.getCode();
            if (authority == null) {
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                        .forPath(projectPath, JSON.toJSONBytes(project));
            } else {
                if (client.checkExists().forPath(projectPath) == null) {
                    client.create().creatingParentsIfNeeded()
                            .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                            .withACL(buildAcls())
                            .forPath(projectPath, JSON.toJSONBytes(project));
                }
            }

            String path = COMMAND_DATA_PATH + "/" + project.getCode();
            if (client.checkExists().forPath(path) == null) {
                if (authority == null) {
                    client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                } else {
                    client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).withACL(buildAcls()).forPath(path);
                }
            }
            doInit();
        } catch (Exception e) {
            LOGGER.info("{},注册项目（{}）失败", zooKeeper.getConnectionString(), project.getName(), e);
        }
    }

    public void detach(Project project) throws CommandException {
        try {
            client.delete().forPath(PROJECT_LIFE_PATH + "/" + project.getCode());
        } catch (Exception e) {
            throw new CommandException(zooKeeper.getConnectionString() + " 项目（" + project.getName() + "）注销失败", e);
        }
    }

    protected List<ACL> buildAcls() throws NoSuchAlgorithmException {
        List<ACL> acls = new ArrayList<>();

        Id aclId = new Id("digest", DigestAuthenticationProvider.generateDigest(authority));
        ACL acl = new ACL(ZooDefs.Perms.ALL, aclId);
        acls.add(acl);
        return acls;
    }

    private void doInit() throws Exception {
        instrutionCache = new NodeCache(client, COMMAND_DATA_PATH + "/" + selfProject.getCode(), false);
        instrutionCache.start(false);
        instrutionCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                if (instrutionCache.getCurrentData() != null) {
                    byte[] data = instrutionCache.getCurrentData().getData();
                    if (data == null) {
                        LOGGER.debug("[没有收到有效的指令]");
                    }
                    notifyInstructionCome(Instruction.from(data));
                }
            }
        });
    }

    private void notifyInstructionCome(Instruction instruction) {
        if (null != instructionRecieveListener) {
            instructionRecieveListener.onInstructionRecieved(instruction);
        }
    }

    private void parseJsonProject(List<Project> projects, String projectNode, byte[] dataByates) {
        Project project = JSON.parseObject(dataByates, Project.class);
        project.setNode(projectNode);
        projects.add(project);
    }

    private void parseSimpleProject(List<Project> projects, String projectNode, byte[] dataBytes) {
        String data = new String(dataBytes);
        String[] ds = data.split(",");
        String projectCode, projectName;
        if (ds.length > 1) {
            projectCode = ds[0];
            projectName = ds[1];
        } else {
            projectCode = projectNode;
            projectName = ds[0];
        }
        Project project = new Project();
        project.setCode(projectCode);
        project.setName(projectName);
        project.setNode(projectNode);
        projects.add(project);
    }

    private void initRegistryProjectsListeners(CuratorFramework client){
        try {
            projectCache = new PathChildrenCache(client, PROJECT_LIFE_PATH, true, true, Executors.newFixedThreadPool(2));
            projectCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            projectCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            notifyProjectRegisted(client, event.getData());
                            break;
                        case CHILD_REMOVED:
                            notifyProjectDetached(client, event.getData());
                            break;
                        case INITIALIZED:
                        case CONNECTION_LOST:
                        case CONNECTION_RECONNECTED:
                        case CONNECTION_SUSPENDED:
                        case CHILD_UPDATED:
                            break;
                    }
                }
            });
        } catch (Exception e) {
            throw new LifecycleException(e);
        }
    }

    private void notifyProjectRegisted(CuratorFramework client, ChildData data) {
        if (null == data || data.getData() == null) return;
        Project project = getProject(data);
        if (null != projectStatusListener) {
            projectStatusListener.onProjectRegisted(project);
        }
    }

    private void notifyProjectDetached(CuratorFramework client, ChildData data) {
        if (data == null || data.getData() == null) return;
        Project project = getProject(data);
        projectStatusListener.onProjectDetached(project);
    }

    private Project getProject(ChildData data) {
        Project project = new Project();
        String projectName = new String(data.getData());
        project.setName(projectName);
        String projectCode = data.getPath();
        project.setCode(projectCode);
        project.setNode(data.getPath());
        return project;
    }

    public static class NoCompressProvider implements CompressionProvider {

        @Override
        public byte[] compress(String path, byte[] data) throws Exception {
            return data;
        }

        @Override
        public byte[] decompress(String path, byte[] compressedData) throws Exception {
            return compressedData;
        }
    }
}
