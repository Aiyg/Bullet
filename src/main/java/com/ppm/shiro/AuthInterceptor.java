package com.ppm.shiro;

import com.alibaba.fastjson.JSONObject;
import com.ppm.exception.BusinessException;
import com.ppm.exception.code.BaseResponseCode;
import com.ppm.service.RedisService;
import com.ppm.utils.HttpTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author yangkai
 * date 2020/9/15
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisService redisService;

    public AuthInterceptor() {
        System.out.println("token拦截器----加载");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 拦截处理代码
        String token  = request.getHeader("Authorization");
        // 如果是OPTIONS请求则结束
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
            return true;
        }
        try{
            if (StringUtils.isBlank(token)) {
                throw new BusinessException(BaseResponseCode.TOKEN_PARSE_ERROR);
            } else {
                String userId = (String) redisService.get(token);
                if(StringUtils.isBlank(userId)) {
                    throw new BusinessException(BaseResponseCode.TOKEN_PARSE_ERROR);
                }
                redisService.set(token,userId,1,TimeUnit.HOURS);
                request.setAttribute("userId", userId);
            }
            /*request.setAttribute("userId", "1");*/
            return true;
        }catch (Exception e) {
            //response.setStatus(HttpStatus.FORBIDDEN.value());
            throw new BusinessException(BaseResponseCode.TOKEN_PARSE_ERROR);
        }
    }
}
