package com.example.System.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "app.database.auto-init", havingValue = "true", matchIfMissing = true)
public class UserTableInitializer {

    private static final Logger logger = LoggerFactory.getLogger(UserTableInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase() {
        logger.info("开始检查并初始化用户表...");
        
        try {
            // 检查用户表是否存在
            if (!tableExists("user")) {
                logger.info("用户表不存在，开始创建...");
                createUserTable();
                logger.info("用户表创建完成！");
            } else {
                logger.info("用户表已存在，跳过创建");
            }
            
            logger.info("用户表检查和初始化完成！");
        } catch (Exception e) {
            logger.error("初始化用户表时出错: ", e);
        }
    }

    private boolean tableExists(String tableName) {
        try {
            jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?", 
                Integer.class, tableName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void createUserTable() throws Exception {
        ClassPathResource resource = new ClassPathResource("sql/user_table.sql");
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        
        // 读取所有行
        String[] sqlStatements = reader.lines()
                .filter(line -> !line.trim().isEmpty() && !line.trim().startsWith("--"))
                .collect(Collectors.joining("\n"))
                .split(";");
        
        // 逐个执行SQL语句
        for (String sql : sqlStatements) {
            if (!sql.trim().isEmpty()) {
                jdbcTemplate.execute(sql.trim());
            }
        }
    }
}