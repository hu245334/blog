package org.example.blog.controller;

import jakarta.validation.constraints.Pattern;
import org.example.blog.pojo.Result;
import org.example.blog.pojo.User;
import org.example.blog.service.UserService;
import org.example.blog.utils.JwtUtil;
import org.example.blog.utils.Md5Util;
import org.example.blog.utils.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;

    public UserController(UserService userService, StringRedisTemplate stringRedisTemplate) {
        this.userService = userService;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}") String username,
                           @Pattern(regexp = "^\\S{5,16}") String password) {
        // 用户名合法
        // 查询用户
        User user = userService.findByUserName(username);
        if (user == null) {
            // 用户未被注册, 执行注册逻辑
            userService.register(username, password);
            return Result.success();
        } else {
            // 用户已被注册
            return Result.error("用户名已被注册");
        }
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{5,16}") String username,
                        @Pattern(regexp = "^\\S{5,16}") String password) {
        // 根据用户名查询用户
        User login_user = userService.findByUserName(username);
        // 判断用户是否存在
        if (login_user == null) {
            return Result.error("用户名不存在");
        }

        if (Md5Util.getMD5String(password).equals(login_user.getPassword())) {
            // 登陆成功
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", login_user.getId());
            claims.put("username", login_user.getUsername());
            String token = JwtUtil.genToken(claims);

            // 将token存储到redis中
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            valueOperations.set(token, token, 12, TimeUnit.HOURS);

            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader(name = "Authorization") String token) {
        // 根据token获取用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params,
                            @RequestHeader(name = "Authorization") String token) {
        // 校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要参数");
        }
        // 原密码是否正确
        // 调用userService根据用户名查询用户密码，再和old_pwd比较
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if (!Md5Util.getMD5String(oldPwd).equals(loginUser.getPassword())) {
            return Result.error("原密码错误");
        }
        // new_pwd和re_pwd是否一致
        if (!newPwd.equals(rePwd)) {
            return Result.error("两次密码不一致");
        }
        // 调用userService完成更新密码
        userService.updatePwd(newPwd);

        // 删除redis中的token
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.getOperations().delete(token);

        return Result.success();
    }
}