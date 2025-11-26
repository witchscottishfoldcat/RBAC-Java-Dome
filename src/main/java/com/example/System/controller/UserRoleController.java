package com.example.System.controller;

import com.example.System.annotation.RequirePermission;
import com.example.System.common.Result;
import com.example.System.entity.Permission;
import com.example.System.entity.Role;
import com.example.System.service.PermissionService;
import com.example.System.service.RoleService;
import com.example.System.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-role")
@Tag(name = "用户角色管理", description = "用户角色关系相关接口")
@RequirePermission(roles = {"admin"})
public class UserRoleController {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleController.class);

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Operation(summary = "获取用户的角色")
    @GetMapping("/user/{userId}/roles")
    public Result<List<Role>> getUserRoles(@Parameter(description = "用户ID") @PathVariable Long userId) {
        logger.info("获取用户角色请求: userId={}", userId);
        try {
            List<Role> roles = roleService.getRolesByUserId(userId);
            logger.info("获取用户角色成功: userId={}, 角色数量={}", userId, roles.size());
            return Result.success(roles);
        } catch (Exception e) {
            logger.error("获取用户角色异常: userId={}, error={}", userId, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "获取用户的权限")
    @GetMapping("/user/{userId}/permissions")
    public Result<List<Permission>> getUserPermissions(@Parameter(description = "用户ID") @PathVariable Long userId) {
        logger.info("获取用户权限请求: userId={}", userId);
        try {
            List<Permission> permissions = permissionService.getPermissionsByUserId(userId);
            logger.info("获取用户权限成功: userId={}, 权限数量={}", userId, permissions.size());
            return Result.success(permissions);
        } catch (Exception e) {
            logger.error("获取用户权限异常: userId={}, error={}", userId, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "为用户分配角色")
    @PostMapping("/assign")
    public Result<String> assignRole(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "角色ID") @RequestParam Long roleId) {
        logger.info("为用户分配角色请求: userId={}, roleId={}", userId, roleId);
        try {
            boolean success = userRoleService.assignUserRole(userId, roleId);
            if (success) {
                logger.info("为用户分配角色成功: userId={}, roleId={}", userId, roleId);
                return Result.success("角色分配成功");
            } else {
                logger.warn("为用户分配角色失败: userId={}, roleId={}", userId, roleId);
                return Result.error("角色分配失败");
            }
        } catch (Exception e) {
            logger.error("为用户分配角色异常: userId={}, roleId={}, error={}", userId, roleId, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "取消用户角色")
    @DeleteMapping("/remove")
    public Result<String> removeRole(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "角色ID") @RequestParam Long roleId) {
        logger.info("取消用户角色请求: userId={}, roleId={}", userId, roleId);
        try {
            boolean success = userRoleService.removeUserRole(userId, roleId);
            if (success) {
                logger.info("取消用户角色成功: userId={}, roleId={}", userId, roleId);
                return Result.success("角色取消成功");
            } else {
                logger.warn("取消用户角色失败: userId={}, roleId={}", userId, roleId);
                return Result.error("角色取消失败");
            }
        } catch (Exception e) {
            logger.error("取消用户角色异常: userId={}, roleId={}, error={}", userId, roleId, e.getMessage());
            throw e;
        }
    }
}