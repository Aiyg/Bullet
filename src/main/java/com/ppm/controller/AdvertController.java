package com.ppm.controller;

import com.ppm.constants.Constant;
import com.ppm.entity.Activity;
import com.ppm.entity.Advert;
import com.ppm.entity.WxMember;
import com.ppm.exception.code.BaseResponseCode;
import com.ppm.mapper.ActivityMapper;
import com.ppm.mapper.AdvertMapper;
import com.ppm.mapper.WxMemberMapper;
import com.ppm.utils.DataResult;
import com.ppm.utils.StringTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * function 小程序登陆
 * Created by gff on 2017/8/21.
 */
@RestController
@RequestMapping(value="/api/advert")
public class AdvertController {
    private final Log log = LogFactory.getLog(getClass());

    final private String appid = "wx63f0dc793722288f";
    final private String secret = "e33ed24be90b644bc34ce82348ad2e6c";
    @Autowired
    private AdvertMapper advertMapper;
    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 小程序广告接口
     */
    @RequestMapping(value={"/findAdvert"})
    @ResponseBody
    public DataResult findAdvert(Integer activityId, HttpServletRequest request) throws IOException {
        try{
            Activity activity = activityMapper.selectByPrimaryKey(activityId);
            activity.getUserId();
            List<Advert> advertList = advertMapper.findAvert(activity.getUserId());
            return DataResult.success(advertList);
        }catch (Exception e){
            e.printStackTrace();
            return DataResult.getResult(BaseResponseCode.SYSTEM_BUSY);
        }
    }

}
