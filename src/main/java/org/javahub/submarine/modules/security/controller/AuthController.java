package org.javahub.submarine.modules.security.controller;

import org.javahub.submarine.common.dto.Auth;
import org.javahub.submarine.common.exception.ServiceException;
import org.javahub.submarine.common.util.JwtUtil;
import org.javahub.submarine.common.util.UserUtil;
import org.javahub.submarine.modules.security.entity.JwtUser;
import org.javahub.submarine.modules.system.dto.UserDto;
import org.javahub.submarine.modules.system.entity.User;
import org.javahub.submarine.modules.system.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @menu 登录校验
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Auth login(UserDto userDto) {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(userDto.getUsername());
        if(Objects.isNull(jwtUser)) {
            throw new ServiceException("用户不存在");
        }
        if(!jwtUser.isEnabled()) {
            throw new ServiceException("当前账号已冻结，请联系管理员");
        }
        if(!bCryptPasswordEncoder.matches(userDto.getPassword(), jwtUser.getPassword())) {
            throw new ServiceException("密码错误");
        }
        String token = jwtUtil.create(jwtUser.getUsername());
        return new Auth(token, jwtUser);
    }

    /**
     * 用户信息
     */
    @GetMapping("/info")
    public JwtUser info() {
        User user = userService.getUserById(UserUtil.getJwtUser().getId());
        return JwtUser.createByUser(user);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public void logout() {

    }

}