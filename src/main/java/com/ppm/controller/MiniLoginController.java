package com.ppm.controller;

import com.alibaba.fastjson.JSONObject;
import com.ppm.constants.Constant;
import com.ppm.entity.*;
import com.ppm.exception.code.BaseResponseCode;
import com.ppm.mapper.*;
import com.ppm.service.RedisService;
import com.ppm.utils.DataResult;
import com.ppm.utils.HttpTools;
import com.ppm.utils.StringTools;
import com.ppm.utils.WebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private WxFriendMapper wxFriendMapper;
    @Autowired
    private FriendMapper friendMapper;

    /**
     * 小程序登陆
     */
    @RequestMapping(value={"/login"})
    @ResponseBody
    public DataResult login(String code,String activityId,String headImg,String nickName,String sex, HttpSession session, HttpServletRequest request) throws IOException {
        try{
            String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                    "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&" +
                    "grant_type=authorization_code";
            JSONObject jsonObject = HttpTools.httpsRequest(url, "GET", null);
            String openid = jsonObject.get("openid").toString();
            String session_key = jsonObject.get("session_key").toString();
            if (StringUtils.isNotBlank(openid)) {
                WxMember wxMember = wxMemberMapper.findOne(openid);
                if (wxMember != null) {
                    wxMember.setHeadImg(headImg);
                    wxMember.setNickName(nickName);
                    wxMember.setSex(sex);
                    wxMemberMapper.updateByPrimaryKeySelective(wxMember);
                }else{
                    wxMember=new WxMember();
                    wxMember.setCreateTime(new Date());
                    wxMember.setSex(sex);
                    wxMember.setHeadImg(headImg);
                    wxMember.setNickName(nickName);
                    wxMember.setOpenid(openid);
                    wxMemberMapper.insert(wxMember);
                }


                List<WxMember> list = redisService.getList(Constant.ONLINE_WX_MEMBER+activityId+sex);
                Boolean boo = true;
                for(WxMember mem:list){
                    if(mem.getOpenid().equals(openid)){
                        boo=false;
                    }
                }
                if(boo){
                    list.add(wxMember);
                }

                redisService.delete(Constant.ONLINE_WX_MEMBER+activityId+sex);
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
    public DataResult findTwain(String oid,String activityId, HttpServletRequest request) throws IOException {
        try{
            WxMember wxMember = wxMemberMapper.findOne(oid);
            List<WxMember> list = new ArrayList<>();

            if("1".equals(wxMember.getSex())){
                list = redisService.getList(Constant.ONLINE_WX_MEMBER+activityId+"2");
            }else{
                list = redisService.getList(Constant.ONLINE_WX_MEMBER+activityId+"1");
            }
            Collections.shuffle(list);
            return DataResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return DataResult.getResult(BaseResponseCode.ACCOUNT_ERROR);
        }
    }

    /**
     * 随机匹配对对碰一对
     */
    @RequestMapping(value={"/randomTwain"})
    @ResponseBody
    public DataResult randomTwain(String activityId, HttpServletRequest request) throws IOException {
        try{
            Map<String,Object> wxMem = new HashMap<>();
            List<WxMember> boyList = redisService.getList(Constant.ONLINE_WX_MEMBER+activityId+"1");
//            List<WxMember> boyList = new ArrayList<>();
//            boyList.add(wxMemberMapper.findOne("oGI9P5YIctKFEz-LGfbv9hxznn1M"));
            List<WxMember> girlList = redisService.getList(Constant.ONLINE_WX_MEMBER+activityId+"2");
//            List<WxMember> girlList = new ArrayList<>();
//            girlList.add(wxMemberMapper.findOne("oGI9P5YtP2xkG17ZBkvT9n2g3CHI"));

            if(boyList.size()>0 && girlList.size()>0) {
                WxMember boy = boyList.get(new Random().nextInt(boyList.size()));
                WxMember girl = girlList.get(new Random().nextInt(girlList.size()));
                WxFriend wxFriend = new WxFriend();
                wxFriend.setContent("缘分对对碰");
                wxFriend.setCreateTime(new Date());
                wxFriend.setWxMemberId(boy.getId());
                wxFriend.setWxMemberFriendId(girl.getId());
                wxFriendMapper.insert(wxFriend);

                WxFriend wxFriend1 = new WxFriend();
                wxFriend1.setContent("缘分对对碰");
                wxFriend1.setCreateTime(new Date());
                wxFriend1.setWxMemberId(girl.getId());
                wxFriend1.setWxMemberFriendId(boy.getId());
                wxFriendMapper.insert(wxFriend1);

                Friend friend = new Friend();
                friend.setWxMemberId(boy.getId().toString());
                friend.setWxMemberFriendId(girl.getId().toString());
                friend = friendMapper.getFriend(friend);
                //首次对话，添加好友信息
                if(friend==null){
                    friend = new Friend();
                    friend.setWxMemberId(boy.getId().toString());
                    friend.setWxMemberFriendId(girl.getId().toString());
                    friend.setCreateTime(new Date());
                    friendMapper.insert(friend);
//------------------------互相添加为好友---------------------------//
                    friend = new Friend();
                    friend.setWxMemberId(girl.getId().toString());
                    friend.setWxMemberFriendId(boy.getId().toString());
                    friend.setCreateTime(new Date());
                    friendMapper.insert(friend);
                }
                wxMem.put("boy",boy);
                wxMem.put("girl",girl);
                return DataResult.success(wxMem);
            }else{
                return DataResult.getResult(BaseResponseCode.USER_EMPTY);
            }

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
    public DataResult sendBullet(String oid,Integer activityId,String content,String type, HttpServletRequest request) throws IOException {
        try{
            WxMember wxMember = wxMemberMapper.findOne(oid);
            BulletSendRecord record = new BulletSendRecord();
            record.setActivityId(activityId);
            record.setContent(content);
            record.setType(type);
            record.setCreateTime(new Date());
            record.setStatus("0");
            record.setWxMemberId(wxMember.getId());
            bulletSendRecordMapper.insert(record);
            record.setHeadImg(wxMember.getHeadImg());
            WebSocketServer.sendInfo(net.sf.json.JSONObject.fromObject(record).toString(),"11"+activityId.toString());
            WebSocketServer.sendInfo(net.sf.json.JSONObject.fromObject(record).toString(),"22"+activityId.toString());
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
    public DataResult bulletList(String oid,Integer activityId, HttpServletRequest request) throws IOException {
        try{
            WxMember wxMember = wxMemberMapper.findOne(oid);
            BulletSendRecord record = new BulletSendRecord();
            record.setActivityId(activityId);
            record.setStatus("0");
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


    /**
     * 好友列表
     */
    @RequestMapping(value={"/friendList"})
    @ResponseBody
    public DataResult friendList(String oid, HttpServletRequest request) throws IOException {
        WxMember wxMember = wxMemberMapper.findOne(oid);
        WxFriend record = new WxFriend();
        record.setWxMemberId(wxMember.getId());
        List<WxFriend> list = wxFriendMapper.friendList(record);

        return DataResult.success(list);
    }

    /**
     * 聊天列表
     */
    @RequestMapping(value={"/chatList"})
    @ResponseBody
    public DataResult chatList(String oid,Integer wxMemberFriendId, HttpServletRequest request) throws IOException {
        WxMember wxMember = wxMemberMapper.findOne(oid);
        WxFriend record = new WxFriend();
        record.setWxMemberId(wxMember.getId());
        record.setWxMemberFriendId(wxMemberFriendId);
        List<WxFriend> list = wxFriendMapper.chatList(record);

        return DataResult.success(list);
    }


    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push")
    public Map pushToWeb(String cid, String oid, String message) {
        Map result = new HashMap();
        try {
            WxMember sendMem = wxMemberMapper.findOne(oid);
            WxFriend wxFriend = new WxFriend();
            wxFriend.setContent(message);
            wxFriend.setCreateTime(new Date());
            wxFriend.setWxMemberId(sendMem.getId());
            wxFriend.setWxMemberFriendId(Integer.parseInt(cid));
            wxFriendMapper.insert(wxFriend);
            Friend friend = new Friend();
            friend.setWxMemberId(sendMem.getId().toString());
            friend.setWxMemberFriendId(cid);
            friend = friendMapper.getFriend(friend);
            //首次对话，添加好友信息
            if(friend==null){
                friend = new Friend();
                friend.setWxMemberId(sendMem.getId().toString());
                friend.setWxMemberFriendId(cid);
                friend.setCreateTime(new Date());
                friendMapper.insert(friend);
//------------------------互相添加为好友---------------------------//
                friend = new Friend();
                friend.setWxMemberId(cid);
                friend.setWxMemberFriendId(sendMem.getId().toString());
                friend.setCreateTime(new Date());
                friendMapper.insert(friend);
            }

            Map<String,Object> param = new HashMap<>();
            param.put("message",message);
            param.put("headImg",sendMem.getHeadImg());
            param.put("date","");
            param.put("type",1);
            param.put("nickName",sendMem.getNickName());
            WebSocketServer.sendInfo(net.sf.json.JSONObject.fromObject(param).toString(),cid);
            result.put("code", 0);
            result.put("msg", "success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



}
