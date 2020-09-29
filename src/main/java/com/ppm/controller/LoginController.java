package com.ppm.controller;

import com.ppm.mapper.*;
import com.ppm.service.RedisService;
import com.ppm.service.UserService;
import com.ppm.utils.DataResult;
import com.ppm.vo.req.LoginReqVO;
import com.ppm.vo.req.RegisterReqVO;
import com.ppm.vo.resp.LoginRespVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * function 后台管理接口
 * Created by gff on 2017/8/21.
 */
@RestController
@RequestMapping(value="/sysUser/")
public class LoginController {
    private final Log log = LogFactory.getLog(getClass());

    final private String appid = "wxa3234f15461aface";
    final private String secret = "243d9b2852cda6e3a812846a7cae249f";

    @Autowired
    private WxMemberMapper wxMemberMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BulletMapper bulletMapper;
    @Autowired
    private BulletSendRecordMapper bulletSendRecordMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private AdvertMapper advertMapper;
    @Autowired
    private WxFriendMapper wxFriendMapper;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录接口")
    public DataResult<LoginRespVO> login(LoginReqVO vo){
        DataResult<LoginRespVO> result= DataResult.success();
        result.setData(userService.userLogin(vo));
        return result;
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册接口")
    public DataResult<String> register(RegisterReqVO vo){
        vo.setDeptId("1231321");
        DataResult<String> result=DataResult.success();
        result.setData(userService.userRegister(vo));
        return result;
    }



}
