package com.lianjia.matrix.command.registry;

import com.lianjia.matrix.common.command.registry.InstructionSender;
import com.lianjia.matrix.common.command.registry.anno.EnableBossRegistry;
import com.lianjia.matrix.common.command.registry.entity.Instruction;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.listener.InstructionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 程天亮
 * @Created
 */
@EnableBossRegistry
@EnableAutoConfiguration
@Configuration
@ComponentScan("com.lianjia")
public class BossCommandRegistryApplicationTest implements CommandLineRunner {

    @Autowired
    private InstructionSender instructionSender;

    public static void main(String[] args) {
        SpringApplication.run(BossCommandRegistryApplicationTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final Project project = new Project();
        project.setName("test-boss");
        project.setCode("test-boss");
        project.setAuthCode("");
        instructionSender.registry(project);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        Map<String,Object> map = new HashMap<>();
                        map.put("value",1);
                        instructionSender.broadcast(Instruction.from(map));
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    @Component
    public static class CommandReceiveListener implements InstructionListener {

        @Override
        public void onInstructionRecieved(Instruction instruction) {
            System.out.println("Received Command:" + instruction.value());
        }
    }
}
