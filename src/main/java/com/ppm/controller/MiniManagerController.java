package com.ppm.controller;

import com.github.pagehelper.PageHelper;
import com.ppm.aop.annotation.LogAnnotation;
import com.ppm.entity.Activity;
import com.ppm.entity.Advert;
import com.ppm.entity.Bullet;
import com.ppm.entity.SysUser;
import com.ppm.mapper.*;
import com.ppm.service.RedisService;
import com.ppm.service.UserService;
import com.ppm.utils.DataResult;
import com.ppm.utils.HttpTools;
import com.ppm.utils.PageUtils;
import com.ppm.utils.WeiXinUtil;
import com.ppm.vo.req.ActivityPageReqVO;
import com.ppm.vo.req.AdvertPageReqVO;
import com.ppm.vo.req.BulletPageReqVO;
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
    @RequestMapping("/users")
    public DataResult<PageVO<SysUser>> pageInfo(UserPageReqVO vo){
        DataResult<PageVO<SysUser>> result= DataResult.success();
        result.setData(userService.pageInfo(vo));
        return result;
    }

    //用户锁定
    @RequestMapping("/userLocd")
    public DataResult userLocd(String id){
        SysUser sysUser= new SysUser();
        sysUser.setId(id);
        sysUser.setStatus(2);
        userService.updateByPrimaryKeySelective(sysUser);
        return  DataResult.success();
    }

    //广告管理
    @RequestMapping("/advertList")
    public DataResult advertList(AdvertPageReqVO vo,HttpServletRequest request){
        if(!"e1ec8f1f-fb61-4948-a178-bb78963cbf0e".equals(request.getAttribute("userId"))){
            vo.setUserId((String) request.getAttribute("userId"));
        }
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        return DataResult.success(PageUtils.getPageVO(advertMapper.selectAll(vo)));
    }

    @RequestMapping("/advertDetail")
    public DataResult advertDetail(Integer id,HttpServletRequest request){
        return DataResult.success(advertMapper.selectByPrimaryKey(id));
    }

    @RequestMapping("/advertAdd")
    public DataResult advertAdd(Advert advert,HttpServletRequest request){
        advert.setIsDeleted("0");
        advert.setCreateTime(new Date());
        advert.setUserId((String) request.getAttribute("userId"));
        advertMapper.insert(advert);
        return DataResult.success();
    }
    @RequestMapping("/advertUpdate")
    public DataResult advertUpdate(Advert advert,HttpServletRequest request){
        advert.setUpdateTime(new Date());
        advertMapper.updateByPrimaryKeySelective(advert);
        return DataResult.success();
    }

    @RequestMapping("/advertDel")
    public DataResult advertDel(Advert advert,HttpServletRequest request){
        advert.setIsDeleted("1");
        advertMapper.updateByPrimaryKeySelective(advert);
        return DataResult.success();
    }

    //-----------------------------活动管理----------------------------------

    //活动管理
    @RequestMapping("/activityList")
    public DataResult activityList(ActivityPageReqVO vo, HttpServletRequest request){
        vo.setUserId((String) request.getAttribute("userId"));
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        return DataResult.success(PageUtils.getPageVO(activityMapper.selectAll(vo)));
    }

    @RequestMapping("/activityDetail")
    public DataResult activityDetail(Integer id,HttpServletRequest request){
        return DataResult.success(activityMapper.selectByPrimaryKey(id));
    }

    @RequestMapping("/activitytAdd")
    public DataResult activitytAdd(Activity activity, HttpServletRequest request){
        activity.setIsDeleted("0");
        activity.setCreateTime(new Date());
        activity.setUserId((String) request.getAttribute("userId"));
        activityMapper.insert(activity);
        return DataResult.success();
    }
    @RequestMapping("/activityUpdate")
    public DataResult activityUpdate(Activity activity,HttpServletRequest request){
        activity.setUpdateTime(new Date());
        activityMapper.updateByPrimaryKeySelective(activity);
        return DataResult.success();
    }

    @RequestMapping("/activityDel")
    public DataResult activityDel(Activity activity,HttpServletRequest request){
        activity.setIsDeleted("1");
        activityMapper.updateByPrimaryKeySelective(activity);
        return DataResult.success();
    }

    //-------------弹幕管理---------------------------------------

    //弹幕管理
    @RequestMapping("/bulletList")
    public DataResult bulletList(BulletPageReqVO vo, HttpServletRequest request){
        vo.setUserId((String) request.getAttribute("userId"));
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        return DataResult.success(PageUtils.getPageVO(bulletMapper.selectAll(vo)));
    }

    @RequestMapping("/bulletDetail")
    public DataResult bulletDetail(Integer id,HttpServletRequest request){
        return DataResult.success(bulletMapper.selectByPrimaryKey(id));
    }

    @RequestMapping("/bullettAdd")
    public DataResult bullettAdd(Bullet bullet, HttpServletRequest request){
        bullet.setIsDeleted("0");
        bullet.setCreateTime(new Date());
        bullet.setUserId((String) request.getAttribute("userId"));
        bulletMapper.insert(bullet);
        return DataResult.success();
    }
    @RequestMapping("/bulletUpdate")
    public DataResult bulletUpdate(Bullet bullet,HttpServletRequest request){
        bulletMapper.updateByPrimaryKeySelective(bullet);
        return DataResult.success();
    }
    @RequestMapping("/bulletDel")
    public DataResult bulletDel(Bullet bullet,HttpServletRequest request){
        bullet.setIsDeleted("1");
        bulletMapper.updateByPrimaryKeySelective(bullet);
        return DataResult.success();
    }

}
