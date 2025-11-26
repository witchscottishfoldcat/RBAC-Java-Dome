package com.example.System.controller;

import com.example.System.common.Result;
import com.example.System.dto.LoginRequest;
import com.example.System.dto.LoginResponse;
import com.example.System.entity.User;
import com.example.System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户登录、注册和认证相关接口")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录", description = "根据用户名和密码进行身份验证，成功后返回JWT令牌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "登录失败", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/login")
    public Result<LoginResponse> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "登录请求信息", required = true, content = @Content(schema = @Schema(implementation = LoginRequest.class))
    ) @RequestBody LoginRequest loginRequest) {
        logger.info("用户登录请求: username={}", loginRequest.getUsername());
        try {
            LoginResponse response = userService.login(loginRequest);
            logger.info("用户登录成功: username={}, userId={}", loginRequest.getUsername(), response.getUserId());
            return Result.success(response);
        } catch (Exception e) {
            logger.error("用户登录失败: username={}, error={}", loginRequest.getUsername(), e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "用户注册", description = "创建新用户账户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "注册成功", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "注册失败", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/register")
    public Result<String> register(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "用户注册信息", required = true, content = @Content(schema = @Schema(implementation = User.class))
    ) @RequestBody User user) {
        logger.info("用户注册请求: username={}", user.getUsername());
        try {
            boolean success = userService.register(user);
            if (success) {
                logger.info("用户注册成功: username={}", user.getUsername());
                return Result.success("注册成功");
            } else {
                logger.warn("用户注册失败: username={}", user.getUsername());
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            logger.error("用户注册异常: username={}, error={}", user.getUsername(), e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息", security = @SecurityRequirement(name = "JWT"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "获取失败", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @GetMapping("/info")
    public Result<User> getUserInfo(@Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        logger.info("获取用户信息请求: userId={}", userId);
        try {
            User user = userService.getById(userId);
            if (user != null) {
                // 不返回密码
                user.setPassword(null);
                logger.info("获取用户信息成功: userId={}", userId);
                return Result.success(user);
            } else {
                logger.warn("获取用户信息失败，用户不存在: userId={}", userId);
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            logger.error("获取用户信息异常: userId={}, error={}", userId, e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}