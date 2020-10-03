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


    @RequestMapping(value = "/upload")
    @ResponseBody
    public String uploadImage(MultipartFile file){
        //获取文件名称
        String orgFileName = file.getOriginalFilename();
        /*String date = new SimpleDateFormat("yyyyMM").format(new Date());
        String path = filePath+"/"+date+"/image";*/
        //使用随机数生成文件名称,加点和文件的后缀
        String fileName = new Date().getTime()+orgFileName;
        String path = "/home/image/"+new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try {
            //新建一个File实例,放入要存的抽象路径,文件的名字
            File targetFile = new File(path, fileName);
            //使用注解流的方式把图片写入文件中
            FileUtils.writeByteArrayToFile(targetFile, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }


}
