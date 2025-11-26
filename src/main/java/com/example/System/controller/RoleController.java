package com.example.System.controller;

import com.example.System.annotation.RequirePermission;
import com.example.System.common.Result;
import com.example.System.entity.Role;
import com.example.System.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色相关接口")
@RequirePermission(roles = {"admin"})
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Operation(summary = "获取所有角色")
    @GetMapping("/list")
    public Result<List<Role>> getAllRoles() {
        logger.info("获取所有角色请求");
        try {
            List<Role> roles = roleService.list();
            logger.info("获取所有角色成功，共{}个角色", roles.size());
            return Result.success(roles);
        } catch (Exception e) {
            logger.error("获取所有角色异常: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "创建角色")
    @PostMapping("/create")
    public Result<String> createRole(@RequestBody Role role) {
        logger.info("创建角色请求: roleCode={}", role.getRoleCode());
        try {
            boolean success = roleService.save(role);
            if (success) {
                logger.info("创建角色成功: roleCode={}", role.getRoleCode());
                return Result.success("角色创建成功");
            } else {
                logger.warn("创建角色失败: roleCode={}", role.getRoleCode());
                return Result.error("角色创建失败");
            }
        } catch (Exception e) {
            logger.error("创建角色异常: roleCode={}, error={}", role.getRoleCode(), e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "更新角色")
    @PutMapping("/update/{id}")
    public Result<String> updateRole(@PathVariable Long id, @RequestBody Role role) {
        logger.info("更新角色请求: roleId={}", id);
        try {
            role.setId(id);
            boolean success = roleService.updateById(role);
            if (success) {
                logger.info("更新角色成功: roleId={}", id);
                return Result.success("角色更新成功");
            } else {
                logger.warn("更新角色失败: roleId={}", id);
                return Result.error("角色更新失败");
            }
        } catch (Exception e) {
            logger.error("更新角色异常: roleId={}, error={}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteRole(@PathVariable Long id) {
        logger.info("删除角色请求: roleId={}", id);
        try {
            boolean success = roleService.removeById(id);
            if (success) {
                logger.info("删除角色成功: roleId={}", id);
                return Result.success("角色删除成功");
            } else {
                logger.warn("删除角色失败: roleId={}", id);
                return Result.error("角色删除失败");
            }
        } catch (Exception e) {
            logger.error("删除角色异常: roleId={}, error={}", id, e.getMessage());
            throw e;
        }
    }
}