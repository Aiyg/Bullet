package com.ppm.controller;

import com.github.pagehelper.PageHelper;
import com.ppm.aop.annotation.LogAnnotation;
import com.ppm.entity.Advert;
import com.ppm.entity.SysUser;
import com.ppm.mapper.*;
import com.ppm.service.RedisService;
import com.ppm.service.UserService;
import com.ppm.utils.DataResult;
import com.ppm.utils.HttpTools;
import com.ppm.utils.PageUtils;
import com.ppm.utils.WeiXinUtil;
import com.ppm.vo.req.AdvertPageReqVO;
import com.ppm.vo.req.UserPageReqVO;
import com.ppm.vo.resp.PageVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * function 后台管理接口
 * Created by gff on 2017/8/21.
 */
@RestController
@RequestMapping(value="/auth/")
public class MiniManagerController {
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
    private UserService userService;

    //获取企业用户列表
    @PostMapping("/users")
    public DataResult<PageVO<SysUser>> pageInfo(@RequestBody UserPageReqVO vo){
        DataResult<PageVO<SysUser>> result= DataResult.success();
        result.setData(userService.pageInfo(vo));
        return result;
    }

    //广告管理
    @PostMapping("/advertList")
    public DataResult advertList(AdvertPageReqVO vo,HttpServletRequest request){
        vo.setUserId((String) request.getAttribute("userId"));
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        return DataResult.success(PageUtils.getPageVO(advertMapper.selectAll(vo)));
    }

    @PostMapping("/advertDetail")
    public DataResult advertDetail(Integer id,HttpServletRequest request){
        return DataResult.success(advertMapper.selectByPrimaryKey(id));
    }

    @PostMapping("/advertAdd")
    public DataResult advertAdd(Advert advert,HttpServletRequest request){
        advert.setIsDeleted("0");
        advert.setCreateTime(new Date());
        advert.setUserId((String) request.getAttribute("userId"));
        advertMapper.insert(advert);
        return DataResult.success();
    }
    @PostMapping("/advertUpdate")
    public DataResult advertUpdate(Advert advert,HttpServletRequest request){
        advert.setUpdateTime(new Date());
        advertMapper.updateByPrimaryKeySelective(advert);
        return DataResult.success();
    }

    @PostMapping("/advertDel")
    public DataResult advertDel(Advert advert,HttpServletRequest request){
        advert.setIsDeleted("1");
        advertMapper.updateByPrimaryKeySelective(advert);
        return DataResult.success();
    }


}
