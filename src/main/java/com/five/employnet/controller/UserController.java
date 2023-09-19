package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.entity.User;
import com.five.employnet.service.UserService;
import com.five.employnet.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final WeChatService weChatService;
    private final JwtUtil jwtUtil;
    @Value("${wechat.path}")
    private String basePath;

    public UserController(UserService userService, WeChatService weChatService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.weChatService = weChatService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/update")
    public R<String> update(HttpServletRequest request, @RequestBody User user) {
        String authorizationHeader = request.getHeader("Authorization");
        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
        Long userId = Long.valueOf(jwtUtil.extractUsername(authToken));
        user.setId(userId);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        userService.updateById(user);
        return R.success("success");
    }

    @RequestMapping("/login")
    public R<User> login(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");

        log.info(code);

        JSONObject responseBody = weChatService.getWeChatSessionInfo(code);

        if (responseBody.has("errcode")) {
            return R.error("微信授权失败");
        } else {
            String openId = (String) responseBody.get("openid");
            String sessionKey = (String) responseBody.get("session_key");

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getOpenId, openId);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setOpenId(openId);
                user.setSessionKey(sessionKey);
                log.info(user.toString());
                userService.save(user);
            }
            user.setOpenId("");
            user.setSessionKey("");
            R<User> res = R.success(user);

            Long userId = user.getId();
            String token = jwtUtil.generateToken(String.valueOf(userId));
            res.add("token", token);
            return res;
        }
    }

    @PostMapping("/test")
    public String test(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        log.info(code);
        return code;
    }

}


