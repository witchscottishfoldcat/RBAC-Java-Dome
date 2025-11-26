package com.example.System.controller;

import com.example.System.annotation.RequirePermission;
import com.example.System.common.Result;
import com.example.System.entity.User;
import com.example.System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户列表")
    @GetMapping("/list")
    @RequirePermission(value = {"user:view"})
    public Result<List<User>> getAllUsers() {
        logger.info("获取用户列表请求");
        try {
            List<User> users = userService.list();
            // 不返回密码
            users.forEach(user -> user.setPassword(null));
            logger.info("获取用户列表成功，共{}个用户", users.size());
            return Result.success(users);
        } catch (Exception e) {
            logger.error("获取用户列表异常: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/detail/{id}")
    @RequirePermission(value = {"user:view"})
    public Result<User> getUserDetail(@Parameter(description = "用户ID") @PathVariable Long id) {
        logger.info("获取用户详情请求: userId={}", id);
        try {
            User user = userService.getById(id);
            if (user != null) {
                user.setPassword(null);
                logger.info("获取用户详情成功: userId={}", id);
                return Result.success(user);
            } else {
                logger.warn("获取用户详情失败，用户不存在: userId={}", id);
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            logger.error("获取用户详情异常: userId={}, error={}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "创建用户")
    @PostMapping("/create")
    @RequirePermission(value = {"user:create"}, roles = {"admin"})
    public Result<String> createUser(@RequestBody User user) {
        logger.info("创建用户请求: username={}", user.getUsername());
        try {
            boolean success = userService.register(user);
            if (success) {
                logger.info("创建用户成功: username={}", user.getUsername());
                return Result.success("用户创建成功");
            } else {
                logger.warn("创建用户失败: username={}", user.getUsername());
                return Result.error("用户创建失败");
            }
        } catch (Exception e) {
            logger.error("创建用户异常: username={}, error={}", user.getUsername(), e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "更新用户")
    @PutMapping("/update/{id}")
    @RequirePermission(value = {"user:update"})
    public Result<String> updateUser(@Parameter(description = "用户ID") @PathVariable Long id, @RequestBody User user) {
        logger.info("更新用户请求: userId={}", id);
        try {
            user.setId(id);
            boolean success = userService.updateById(user);
            if (success) {
                logger.info("更新用户成功: userId={}", id);
                return Result.success("用户更新成功");
            } else {
                logger.warn("更新用户失败: userId={}", id);
                return Result.error("用户更新失败");
            }
        } catch (Exception e) {
            logger.error("更新用户异常: userId={}, error={}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{id}")
    @RequirePermission(value = {"user:delete"}, roles = {"admin"})
    public Result<String> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        logger.info("删除用户请求: userId={}", id);
        try {
            boolean success = userService.removeById(id);
            if (success) {
                logger.info("删除用户成功: userId={}", id);
                return Result.success("用户删除成功");
            } else {
                logger.warn("删除用户失败: userId={}", id);
                return Result.error("用户删除失败");
            }
        } catch (Exception e) {
            logger.error("删除用户异常: userId={}, error={}", id, e.getMessage());
            throw e;
        }
    }
}