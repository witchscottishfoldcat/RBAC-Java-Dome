package com.example.System.controller;

import com.example.System.annotation.RequirePermission;
import com.example.System.common.Result;
import com.example.System.entity.Permission;
import com.example.System.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Tag(name = "权限管理", description = "权限相关接口")
@RequirePermission(roles = {"admin"})
public class PermissionController {

    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    @Operation(summary = "获取所有权限")
    @GetMapping("/list")
    public Result<List<Permission>> getAllPermissions() {
        logger.info("获取所有权限请求");
        try {
            List<Permission> permissions = permissionService.list();
            logger.info("获取所有权限成功，共{}个权限", permissions.size());
            return Result.success(permissions);
        } catch (Exception e) {
            logger.error("获取所有权限异常: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "创建权限")
    @PostMapping("/create")
    public Result<String> createPermission(@RequestBody Permission permission) {
        logger.info("创建权限请求: permissionCode={}", permission.getPermissionCode());
        try {
            boolean success = permissionService.save(permission);
            if (success) {
                logger.info("创建权限成功: permissionCode={}", permission.getPermissionCode());
                return Result.success("权限创建成功");
            } else {
                logger.warn("创建权限失败: permissionCode={}", permission.getPermissionCode());
                return Result.error("权限创建失败");
            }
        } catch (Exception e) {
            logger.error("创建权限异常: permissionCode={}, error={}", permission.getPermissionCode(), e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "更新权限")
    @PutMapping("/update/{id}")
    public Result<String> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        logger.info("更新权限请求: permissionId={}", id);
        try {
            permission.setId(id);
            boolean success = permissionService.updateById(permission);
            if (success) {
                logger.info("更新权限成功: permissionId={}", id);
                return Result.success("权限更新成功");
            } else {
                logger.warn("更新权限失败: permissionId={}", id);
                return Result.error("权限更新失败");
            }
        } catch (Exception e) {
            logger.error("更新权限异常: permissionId={}, error={}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/delete/{id}")
    public Result<String> deletePermission(@PathVariable Long id) {
        logger.info("删除权限请求: permissionId={}", id);
        try {
            boolean success = permissionService.removeById(id);
            if (success) {
                logger.info("删除权限成功: permissionId={}", id);
                return Result.success("权限删除成功");
            } else {
                logger.warn("删除权限失败: permissionId={}", id);
                return Result.error("权限删除失败");
            }
        } catch (Exception e) {
            logger.error("删除权限异常: permissionId={}, error={}", id, e.getMessage());
            throw e;
        }
    }
}