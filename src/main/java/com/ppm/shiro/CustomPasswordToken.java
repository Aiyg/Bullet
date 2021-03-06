package com.ppm.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @ClassName: CustomPasswordToken
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 17:58
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 17:58
 * @Version: 0.0.1
 */
public class CustomPasswordToken  extends UsernamePasswordToken {
    private String token;

    public CustomPasswordToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}
