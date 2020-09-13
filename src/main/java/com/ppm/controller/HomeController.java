package com.ppm.controller;

import com.ppm.service.HomeService;
import com.ppm.utils.DataResult;
import com.ppm.utils.JwtTokenUtil;
import com.ppm.vo.resp.HomeRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/sys")
@Api(tags = "首页数据")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    @ApiOperation(value ="获取首页数据接口")
    public DataResult<HomeRespVO> getHomeInfo(HttpServletRequest request){
        String accessToken=request.getHeader("authorization");
        /**
         * 通过access_token拿userId
         */
        String userId= JwtTokenUtil.getUserId(accessToken);
        DataResult<HomeRespVO> result=DataResult.success();
        result.setData(homeService.getHomeInfo(userId));
        return result;
    }
    @GetMapping("/home1")
//    @ApiOperation(value ="获取首页数据接口")
    public DataResult getHomeInfo1(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        DataResult result=DataResult.success();
        map.put("a","b");
        result.setData(map);
        return result;
    }
}
