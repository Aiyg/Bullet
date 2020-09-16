package com.ppm.controller;

import com.alibaba.fastjson.JSONObject;
import com.ppm.constants.Constant;
import com.ppm.entity.*;
import com.ppm.exception.code.BaseResponseCode;
import com.ppm.mapper.ActivityMapper;
import com.ppm.mapper.BulletMapper;
import com.ppm.mapper.BulletSendRecordMapper;
import com.ppm.mapper.WxMemberMapper;
import com.ppm.service.RedisService;
import com.ppm.utils.DataResult;
import com.ppm.utils.HttpTools;
import com.ppm.utils.StringTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * function 小程序登陆
 * Created by gff on 2017/8/21.
 */
@RestController
@RequestMapping(value="/api/wx")
public class MiniLoginController  {
    private final Log log = LogFactory.getLog(getClass());

    final private String appid = "wx63f0dc793722288f";
    final private String secret = "e33ed24be90b644bc34ce82348ad2e6c";
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

    /**
     * 小程序登陆
     */
    @RequestMapping(value={"/login"})
    @ResponseBody
    public DataResult login(String code,String activityId, HttpSession session, HttpServletRequest request) throws IOException {
        try{
            String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                    "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&" +
                    "grant_type=authorization_code";
            JSONObject jsonObject = HttpTools.httpsRequest(url, "GET", null);
            String unionid = jsonObject.get("unionid").toString();
            String openid = jsonObject.get("openid").toString();
            String sex = jsonObject.get("sex").toString();
            if (StringUtils.isNotBlank(openid)) {
                WxMember wxMember = wxMemberMapper.findOne("");
                if (wxMember != null) {
                    wxMember.setHeadImg("12345645");
                    wxMember.setNickName("3453456");
                    wxMemberMapper.updateByPrimaryKeySelective(wxMember);
                }else{
                    wxMember=new WxMember();
                    wxMember.setArea("23");//jsonObject.get("area").toString()
                    wxMember.setCreateTime(new Date());
                    wxMember.setSex(sex);
                    wxMember.setHeadImg("2323");//jsonObject.get("headimg").toString()
                    wxMember.setNickName("2332");//jsonObject.get("nickname").toString()
                    wxMember.setOpenid("sss");//jsonObject.get("openid").toString()
                    wxMember.setUnionid(StringTools.getUUID());//jsonObject.get("unionid").toString()
                    wxMemberMapper.insert(wxMember);
                }


                List<WxMember> list = redisService.getList(Constant.ONLINE_WX_MEMBER+activityId+sex);
                list.add(wxMember);
                redisService.setList(Constant.ONLINE_WX_MEMBER+activityId+sex,list,2, TimeUnit.HOURS);
                /*session.setAttribute(Constant.MEMBER,wxMember);
                session.setMaxInactiveInterval(600);//登陆十分钟有效期*/
                return DataResult.success(wxMember);
            }
            return DataResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return DataResult.getResult(BaseResponseCode.ACCOUNT_ERROR);
        }
    }

    /**
     * 对对碰
     */
    @RequestMapping(value={"/findTwain"})
    @ResponseBody
    public DataResult findTwain(String openid,String activityId, HttpServletRequest request) throws IOException {
        try{
            WxMember wxMember = wxMemberMapper.findOne(openid);
            List<WxMember> list = new ArrayList<>();
            if("1".equals(wxMember.getSex())){
                list = redisService.getList(Constant.ONLINE_WX_MEMBER+activityId+"2");
            }else{
                list = redisService.getList(Constant.ONLINE_WX_MEMBER+activityId+"1");
            }
            return DataResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return DataResult.getResult(BaseResponseCode.ACCOUNT_ERROR);
        }
    }

    /**
     * 弹幕清单
     */
    @RequestMapping(value={"/bullet"})
    @ResponseBody
    public DataResult bullet(Integer activityId, HttpServletRequest request) throws IOException {
        try{
            Activity activity = activityMapper.selectByPrimaryKey(activityId);
            List<Bullet> list = bulletMapper.findBullet(activity.getUserId());
            return DataResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return DataResult.getResult(BaseResponseCode.ACCOUNT_ERROR);
        }
    }

    /**
     * 弹幕发送
     */
    @RequestMapping(value={"/sendBullet"})
    @ResponseBody
    public DataResult sendBullet(String openid,Integer activityId,String content, HttpServletRequest request) throws IOException {
        try{
            WxMember wxMember = wxMemberMapper.findOne(openid);
            BulletSendRecord record = new BulletSendRecord();
            record.setActivityId(activityId);
            record.setContent(content);
            record.setCreateTime(new Date());
            record.setStatus("0");
            record.setWxMemberId(wxMember.getId());
            bulletSendRecordMapper.insert(record);
            return DataResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return DataResult.getResult(BaseResponseCode.ACCOUNT_ERROR);
        }
    }

    /**
     * 弹幕列表
     */
    @RequestMapping(value={"/bulletList"})
    @ResponseBody
    public DataResult bulletList(String openid,Integer activityId, HttpServletRequest request) throws IOException {
        try{
            WxMember wxMember = wxMemberMapper.findOne(openid);
            BulletSendRecord record = new BulletSendRecord();
            record.setActivityId(activityId);
            record.setStatus("0");
            record.setWxMemberId(wxMember.getId());
            List<BulletSendRecord> list = bulletSendRecordMapper.findBulletList(record);

            for(BulletSendRecord re:list){
                BulletSendRecord cord = new BulletSendRecord();
                cord.setId(re.getId());
                cord.setStatus("1");
                bulletSendRecordMapper.updateByPrimaryKeySelective(cord);
            }
            return DataResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return DataResult.getResult(BaseResponseCode.ACCOUNT_ERROR);
        }
    }



}
