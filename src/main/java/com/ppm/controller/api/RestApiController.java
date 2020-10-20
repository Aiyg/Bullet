package com.ppm.controller.api;

import com.ppm.constants.Constant;
import com.ppm.entity.WxFriend;
import com.ppm.entity.WxMember;
import com.ppm.mapper.WxFriendMapper;
import com.ppm.mapper.WxMemberMapper;
import com.ppm.service.HomeService;
import com.ppm.utils.DataResult;
import com.ppm.utils.JwtTokenUtil;
import com.ppm.utils.WebSocketServer;
import com.ppm.vo.resp.HomeRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: HomeController
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 21:20
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 21:20
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("/api")
@Api(tags = "首页数据")
public class RestApiController {
    @Autowired
    private HomeService homeService;
    @Autowired
    private WxFriendMapper wxFriendMapper;
    @Autowired
    private WxMemberMapper wxMemberMapper;

    @GetMapping("/test")
    @ApiOperation(value ="获取首页数据接口")
    public DataResult<HomeRespVO> getHomeInfo(HttpServletRequest request){
        HttpSession session = request.getSession();
        WxMember wxMember = (WxMember) session.getAttribute(Constant.MEMBER);
        System.out.println(session.getAttribute("test"));
        session.setAttribute("test","我们都有一个家名字叫种话");
        session.setMaxInactiveInterval(20);
        DataResult<HomeRespVO> result=DataResult.success();
        result.setData(homeService.getHomeInfo("5bc41939-78d9-40f8-a761-b9cf35f5d9e4"));
        return result;
    }
    @GetMapping("/test2")
    @ApiOperation(value ="获取首页数据接口")
    public DataResult getHomeInfo1(HttpServletRequest request){
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("test2"));
        session.setAttribute("test2","我们都有一个家");
        System.out.println(session.getMaxInactiveInterval());
        Map<String,Object> map = new HashMap<>();
        DataResult result=DataResult.success();
        map.put("a","b");
        result.setData(map);
        return result;
    }

    //页面请求
    @GetMapping("/index/{userId}")
    public ModelAndView socket(@PathVariable String userId) {
        ModelAndView mav=new ModelAndView("/socket1");
        mav.addObject("userId", userId);
        return mav;
    }

    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public Map pushToWeb(@PathVariable String cid, String message) {
        Map result = new HashMap();
        try {
            WebSocketServer.sendInfo(message,cid);
            result.put("code", 200);
            result.put("msg", "success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
