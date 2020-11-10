package com.ppm.controller;

import com.alibaba.fastjson.JSONObject;
import com.ppm.constants.Constant;
import com.ppm.entity.*;
import com.ppm.exception.code.BaseResponseCode;
import com.ppm.mapper.*;
import com.ppm.service.RedisService;
import com.ppm.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * function 小程序登陆
 * Created by gff on 2017/8/21.
 */
@Controller
@RequestMapping(value="/api/code")
public class MiniCodeController {
    private final Log log = LogFactory.getLog(getClass());

    final private String appid = "wxa3234f15461aface";
    final private String secret = "243d9b2852cda6e3a812846a7cae249f";

    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    public void getCode(HttpServletRequest request,String activityId, HttpServletResponse response) throws IOException {
        String accessToken = WeiXinUtil.getAccessToken(appid,secret);
        String codeUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken;
        Map<String,Object> param = new HashMap<>();
        param.put("scene",activityId);
        param.put("page","pages/home/home");
        HttpTools.doImgPost(codeUrl, param);
    }



}
