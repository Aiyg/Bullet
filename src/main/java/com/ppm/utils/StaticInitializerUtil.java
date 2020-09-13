package com.ppm.utils;

import org.springframework.stereotype.Component;

/**
 * @ClassName: StaticContextInitializer
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 10:07
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 10:07
 * @Version: 0.0.1
 */
@Component
public class StaticInitializerUtil {
   private TokenSettings tokenSettings;

    public StaticInitializerUtil(TokenSettings tokenSettings) {
        JwtTokenUtil.setTokenSettings(tokenSettings);
    }
}
