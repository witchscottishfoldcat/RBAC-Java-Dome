package com.example.System;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@MapperScan("com.example.System.mapper")
public class SystemApplication {

    private static final Logger logger = LoggerFactory.getLogger(SystemApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SystemApplication.class, args);
        
        Environment env = context.getEnvironment();
        String serverPort = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        
        String swaggerPath = contextPath + "/swagger-ui.html";
        String apiDocsPath = contextPath + "/v3/api-docs";
        
        System.out.println(System.lineSeparator() + 
                "----------------------------------------------------------" + System.lineSeparator() + 
                "\tRBAC系统启动成功！" + System.lineSeparator() + 
                "\t本地访问地址: \thttp://localhost:" + serverPort + contextPath + System.lineSeparator() + 
                "\tSwagger文档地址: \thttp://localhost:" + serverPort + swaggerPath + System.lineSeparator() + 
                "\tAPI文档地址: \thttp://localhost:" + serverPort + apiDocsPath + System.lineSeparator() + 
                "----------------------------------------------------------");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        logger.info("RBAC系统已完全启动，所有组件已就绪");
    }
}
