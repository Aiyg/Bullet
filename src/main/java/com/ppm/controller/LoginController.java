package com.ppm.controller;

import com.ppm.entity.SysUser;
import com.ppm.exception.BusinessException;
import com.ppm.exception.code.BaseResponseCode;
import com.ppm.mapper.*;
import com.ppm.service.RedisService;
import com.ppm.service.UserService;
import com.ppm.utils.DataResult;
import com.ppm.utils.StringTools;
import com.ppm.vo.req.LoginReqVO;
import com.ppm.vo.req.RegisterReqVO;
import com.ppm.vo.resp.LoginRespVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private SysUserMapper sysUserMapper;

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
        SysUser sysUser1=sysUserMapper.getUserInfoByName(vo.getUsername());
        if(sysUser1!=null){
            return DataResult.getResult(BaseResponseCode.USER_REUSE);
        }
        vo.setDeptId(StringTools.getUUID());
        DataResult<String> result=DataResult.success();
        result.setData(userService.userRegister(vo));
        return result;
    }

}
