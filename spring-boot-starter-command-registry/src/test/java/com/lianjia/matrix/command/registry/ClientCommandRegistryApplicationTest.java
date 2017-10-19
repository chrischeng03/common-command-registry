package com.lianjia.matrix.command.registry;

import com.lianjia.matrix.common.command.registry.ClientCommandRegistry;
import com.lianjia.matrix.common.command.registry.anno.EnableClientRegistry;
import com.lianjia.matrix.common.command.registry.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 程天亮
 * @Created
 */
//@EnableAutoConfiguration
//@Configuration
//@ComponentScan("com.lianjia")
//@EnableClientRegistry
public class ClientCommandRegistryApplicationTest implements CommandLineRunner {

    @Autowired
    private ClientCommandRegistry clientCommandRegistry;

    public static void main(String[] args) {
        SpringApplication.run(ClientCommandRegistryApplicationTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final Project project = new Project();
        project.setName("test-client");
        project.setCode("test-client");
        project.setAuthCode("");
        clientCommandRegistry.registry(project);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
                clientCommandRegistry.detach(project);
            }
        }).start();
    }
}
