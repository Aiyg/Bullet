package com.ppm.utils; /*******************************************************************************
 * Copyright (c) 2010, 2015 git@git.oschina.net:kaiwill/springstage.git
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName:  com.qinyetech.webapp.utils.StringTools<br/>
 * Function:  <br/>
 * date: 2016/8/20 <br/>
 *
 * @author 24642
 * @version 1.0
 * @since JDK 1.7
 */
public class StringTools {
    private static final Log log = LogFactory.getLog(StringTools.class);

    /**
     * 将图片路径替换为指定值
     * @param rootPath 服务器路径
     * @param imgPath 图片路径
     * @return
     */
    public static String appRichTextImgPathFormat(String rootPath, String imgPath){
        String oldPath = "/share/img/";
        String newPath = "/share/img/";

        if(StringUtils.isNoneBlank(imgPath) && StringUtils.isNoneBlank(oldPath) && newPath!=null){
            imgPath = StringUtils.replace(imgPath,oldPath,(rootPath + newPath));
        }
        return imgPath;
    }

    public static boolean vlNotBlank(String[] params){
        boolean rv = true;
        for(String s : params){
            if(StringUtils.isBlank(s)){
                rv = false;
                break;
            }
        }
        return rv;
    }


    /**
     * unicode转为汉字或明文
     * @param unicode unicode字符
     * @param defValue 若解析失败, 按此默认值输出
     * @author mafeiyue
     * @date 2016-10-17
     * @return
     */
    public static String unicode2Chinese(String unicode, String defValue){
        if(StringUtils.isNoneBlank(unicode)){
            defValue = unicode;
        }
        return defValue;
    }

    /**
     * 中文转 unicode
     * @param gbString
     * @return
     */
    public static String gbEncoding(final String gbString) {//gbString = "测试" 
       char[] utfBytes = gbString.toCharArray();//utfBytes = [测, 试] 
       String unicodeBytes = "";
       for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
        String hexB = Integer.toHexString(utfBytes[byteIndex]); //转换为16进制整型字符串 
        if (hexB.length() <= 2) {
         hexB = "00" + hexB;
       }
     unicodeBytes = unicodeBytes + "\\u" + hexB;
    }
 return unicodeBytes;
}

//Unicode转中文
 public static String decodeUnicode(final String dataStr) {
     int start = 0;
     int end = 0;
     final StringBuffer buffer = new StringBuffer();
      while (start > -1) {
         end = dataStr.indexOf("\\u", start + 2);
         String charStr = "";
          if (end == -1) {
            charStr = dataStr.substring(start + 2, dataStr.length());
         } else {
             charStr = dataStr.substring(start + 2, end);
        }
         char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。   
             buffer.append(new Character(letter).toString());
            start = end;
        }
     return buffer.toString();
 }


    /**
     * 获取UUID, 自动将UUID中的[中划线]去除
     * PS:  1. UUID全部以小写返回
     * @author 马飞月
     * @version 1.0
     */
    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * 获取UUID, 自动将UUID中的[中划线]去除
     * PS:  1. UUID全部以小写返回
     * @param noLine 为[null / true]会将UUID中的[中划线]去除, 其它情况[中划线]会保留
     * @author 马飞月
     * @version 1.0
     */
    public static String getUuid(Boolean noLine){
        String rv = UUID.randomUUID().toString();
        if(noLine==null || noLine==true){
            rv = rv.replaceAll("-", "");
        }
        return rv;
    }


    /**
     * 获取时间戳字符串
     * @return
     */
    public static String getTimestamp(){
        return System.currentTimeMillis()+"";
    }


    /**
     * 获取时间戳字符串
     * @return
     */
    public static String getTimestamp(String prefix){
        String rv = System.currentTimeMillis()+"";
        if(StringUtils.isNoneBlank(prefix)){
            rv = prefix + rv;
        }
        return rv;
    }


    /**
     * 系统生成课程代码
     * @return
     */
    public static String getCourseNo(){
        return "KC" + System.currentTimeMillis();
    }


    /**
     * 系统生成排课代码
     * @return
     */
    public static String getCourseSyllabusNo(){
        return "PK" + System.currentTimeMillis();
    }


    /*===================================密码加密 S===============================*/
    /**
     * encryptPassword:加密明文密码. <br/>
     * @param account 登录账号
     * @param password 明文密码
     * @return
     *      salt-当前产生的盐值
     *      passwordDigist-密文密码
     * @since JDK 1.7
     */
    public static Map<String, String> encryptPassword(String account, String password){
        if(StringUtils.isBlank(account) || "".equals(password)){
            return null;
        }
        String algorithmName = "md5";
        int hashIterations = 2;
        String random = new SecureRandomNumberGenerator().nextBytes().toHex();
        String salt = account + random;
        String passwordDigist = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(salt), hashIterations).toHex();

        Map<String, String> rv = new HashedMap();
        rv.put("salt", random);
        rv.put("passwordDigist", passwordDigist);
        return rv;
    }

    /*===================================密码加密 E===============================*/


    /**
     * 获取随机N位数字
     * @param length 获取N个数字
     * @return
     */
    public static String getRandomNumber(Integer length){
        String rv = "";
        if(length==null || length<=0){
            return rv;
        }
        int[] seedNum = {0,1,2,3,4,5,6,7,8,9};

        int index = 0;
        for(int i=0;i<length;i++){
            index = (int)(Math.random()*10);
            rv += seedNum[index];
        }
        return rv;
    }




    /**
     * 获取当前月多少天
     * @return
     */
    public static int getDayOfMonth(){
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day=aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }


    /**
     * 过滤出正确邮箱
     * PS: 推荐使用 getValidEmailV2
     * @param emailStr 邮箱字符串，如";;aa@126.com;;;;;;bb@126.com;;"
     * @return
     */
    public static String getValidEmail(String emailStr){
        if(StringUtils.isBlank(emailStr)){
            return null;
        }

        String[] arr = emailStr.split(";");
        if(ArrayUtils.isEmpty(arr)){
            return null;
        }

        String result = "";
        for(String s : arr){
            if(StringUtils.isNotBlank(s)){
                result += s + ";";
            }
        }
        return result;
    }


    /**
     * 过滤出正确邮箱
     * @param emailStr
     *       处理前
     *           aa@126.com,,,,,bb@126.com，，，，，cc@126.com;;;;;    dd@126.com；    ；；；；aa@126.com
     *       处理后
     *           aa@126.com;bb@126.com;cc@126.com;dd@126.com;
     * @return
     */
    public static String getValidEmailV2(String emailStr){
        if(StringUtils.isBlank(emailStr)){
            return null;
        }

        emailStr = emailStr.trim();
        if (emailStr.indexOf(",")!=-1){
            emailStr = emailStr.replaceAll(",",";");
        }
        if (emailStr.indexOf("，")!=-1){
            emailStr = emailStr.replaceAll("，",";");
        }
        if (emailStr.indexOf("；")!=-1){
            emailStr = emailStr.replaceAll("；",";");
        }

        String[] arr = emailStr.trim().split(";");

        if(ArrayUtils.isEmpty(arr)){
            return null;
        }

        String result = "";
        for(String s : arr){
            s = s.trim();
            if(StringUtils.isNotBlank(s) && result.indexOf(s)==-1){
                result += s + ";";
            }
        }
        if (StringUtils.isNotBlank(result)){
            result = result.substring(0,result.length()-1);
        }
        return result;
    }


    /**
     * 过滤出正确手机
     * @param mobileStr 手机符串，如"[' 13688889999' / '013688889999' / 010-56875555]"
     * @return
     */
    public static String getValidMobile(String mobileStr){
        if(StringUtils.isBlank(mobileStr)){
            return null;
        }
        mobileStr = mobileStr.toLowerCase().trim()
                .replaceAll(" ", "")
                .replaceAll("-", "");
        if(mobileStr.startsWith("0")){
            mobileStr = mobileStr.substring(1, mobileStr.length());
        }
        return mobileStr;
    }

    /**
     * 根据特殊字符 获取第一个
     * @param mobileStr
     * @return
     */
    public static String getFirstMobil(String mobileStr){
        if (StringUtils.isBlank(mobileStr)){
            return null;
        }
        mobileStr = mobileStr.trim()
        .replaceAll("，",",")
        .replaceAll(" ",",")
        .replaceAll(";",",")
        .replaceAll("；",",");

        String[] arr = mobileStr.split(",");

        if(ArrayUtils.isEmpty(arr)){
            return null;
        }

        mobileStr = arr[0].replaceAll("-","");



        return mobileStr;
    }



    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 解决乱码问题
     * @param fileNames
     * @return
     */
    public static String dellCoding(String fileNames){
        String fileName1=fileNames;
        if(StringUtils.isNotEmpty(fileNames) && isMessyCode(fileNames)){
            try {
                fileName1=  new String(fileName1.getBytes("ISO-8859-1"),"UTF-8");
                if(isMessyCode(fileName1)){
                    fileName1=  new String(fileName1.getBytes("ISO-8859-1"),"GBK");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return fileName1;
    }

    /**
     * 微信昵称解决乱码问题
     * @param fileNames
     * @return
     */
    public static String dellCodingWxNickName(String fileNames){
        String fileName1=fileNames;
        if(StringUtils.isNotEmpty(fileNames) && isMessyCode(fileNames)){
            try {
                fileName1=  new String(fileName1.getBytes("ISO-8859-1"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return fileName1;
    }

    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {

                if (!isChinese(c)) {
                    count = count + 1;
                    System.out.print(c);
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * 获取异常的字符串信息
     * @param e 异常
     * @return 字符串
     */
    public static String getExceptionStr(Exception e){
        StringBuffer sb = new StringBuffer(e.toString());
        StackTraceElement[] messages = e.getStackTrace();
        for (int i = 0; i < messages.length; i++) {
            sb.append("\r\n").append("	at "+messages[i].toString());
        }
        return sb.toString();
    }
    /**
     * 四舍五入保留一位小数
     */
    public static double rounding(double d){
        DecimalFormat df = new DecimalFormat("#");
        return Double.valueOf(df.format(d));
    }

    /*
     * 如果是小数，保留两位，非小数，保留整数
     * @param number
     */
    public static String getDoubleString(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            //如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            numberStr = df.format(number);
        }
        return numberStr;
    }

    /**
     * 获取客户机ip字符串 0.0.0.0格式
     * @param request 请求
     * @return 字符串
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(ip!=null&&ip.trim().equalsIgnoreCase("0:0:0:0:0:0:0:1")){
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 获取日志信息  （ 调用者类名 方法名 添加的字段名、值  标志位  ）
     * （例如：LoginAction.login:name=NEC success ） fieldName为name value为NEC flag为额外信息
     * @param fieldName 字段属性名
     * @param value 字段属性值
     * @param flag 额外信息如 成功或失败
     * @return
     */
    public static String getLogInfo(String fieldName,Object value,String flag){
        StackTraceElement[] trace = new Throwable().getStackTrace();
        String classNamef =  trace[1].getClassName();
        String className =  classNamef.substring(classNamef.lastIndexOf(".")+1);
        String methodName =  trace[1].getMethodName();
        return className+"."+methodName+":"+(fieldName==null?flag:fieldName+"="+
                (value==null?"null":value.toString())+" "+flag);
    }

    /**
     * 输出JSON
     *
     * @param response
     * @param result
     * @throws IOException
     */
    public static void print(HttpServletResponse response, String result) throws IOException
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    /**
     * 拼接IN条件值
     * @param str
     * @return
     */
    public static String getInCond(String str) {
        String[] strArr = str.split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strArr.length; i++) {
            if (i == strArr.length - 1) {
                sb.append("'" + strArr[i] + "'");
            } else {
                sb.append("'" + strArr[i] + "'" + ",");
            }
        }
        return sb.toString();
    }
    /**
     * 拼接IN条件值
     * @param strArr
     * @return
     */
    public static String getInCond(String[] strArr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strArr.length; i++) {
            if (i == strArr.length - 1) {
                sb.append("'" + strArr[i] + "'");
            } else {
                sb.append("'" + strArr[i] + "'" + ",");
            }
        }
        return sb.toString();
    }

    /**
     * list 去重
     * @param list
     * @return
     */
    public   static   List  removeDuplicate(List list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).equals(list.get(i)))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }
    /**
     *获取随机数组
     * @param needSize 所需长度
     * @param size  总长度
     * @return
     */
    public static Object[] getRandomNum(int needSize,int size){
        Random random = new Random();
        Object[] values = new Object[needSize];
        HashSet<Integer> hashSet = new HashSet<Integer>();

        // 生成随机数字并存入HashSet
        for(int i = 0;i < values.length;i++){
            int number = random.nextInt(size);
            hashSet.add(number);
        }

        values = hashSet.toArray();
        return values;
    }


    public static String getCityConstants(String DM){
        if(StringUtils.isNotBlank(DM)) {
            switch (DM) {
                case "BJ":
                    return "北京";
                case "GZ":
                    return "广州";
                case "CD":
                    return "成都";
                case "TJ":
                    return "天津";
                case "SZ":
                    return "深圳";
                case "SH":
                    return "上海";
                case "JN":
                    return "济南";
                case "XA":
                    return "西安";
                case "WH":
                    return "武汉";
                case "CQ":
                    return "重庆";
                default:
                    return DM;
            }
        }
        return null;
    }

    /**
     * 获取最小key值
     * @param map
     * @return
     */
    public static String getMinKey(Map map) {
        if (map == null){
            return null;
        }
        Set<String> set = map.keySet();
        Object[] obj = set.toArray();
        Arrays.sort(obj);
        return obj[0].toString();
    }

    /**
     * 获取最大key值
     * @param map
     * @return
     */
    public static String getMaxKey(Map map) {
        if (map == null){
            return null;
        }
        Set<String> set = map.keySet();
        Object[] obj = set.toArray();
        Arrays.sort(obj);
        return obj[obj.length -1].toString();
    }

    /**
     * 替换英文圆括号符号
     * @param Node
     * @return
     */
    public static String replaceRoundBrackets(String Node){
        String newNode = "";
        String allConvertNode = "";
        if(Node.contains("（")&& Node.contains("）")){
            newNode = Node.replaceAll("（", "(");
            allConvertNode = newNode.replace("）",")");
        }
        else if(!(Node.contains("（"))&& Node.contains("）")){
            allConvertNode = Node.replaceAll("）", ")");
        }
        else if(Node.contains("（") && !(Node.contains("）"))){
            newNode = Node.replaceAll("（", "(");
            allConvertNode = newNode;
        }
        else{
            allConvertNode = Node;
        }
        return allConvertNode;
    }

    //小程序剔除标签并且更换小程序所需标签
    public static String deleteHtml(String html){
        return html.replaceAll("<br />","-_-")
                    .replaceAll("<br/>","-_-")
                    .replaceAll("<img","^_^")
                    .replaceAll("\\<.*?>","")
                    .replaceAll("-_-","\\\n")
                    //.replaceAll(" ", "")
                    .replaceAll("(\\\r\\\n)", "")
                    .replaceAll("\\^_\\^","<image")
                    .replaceAll("/share/img","https://www.easthome.com/share/img");
    }

    //小程序剔除标签并且更换小程序所需标签
    public static String deleteHtml1(String html){
        return html.replaceAll("/share/img","https://www.easthome.com/share/img")
                .replaceAll("<img","<img style='width:100%;display:block;' ");
    }


    /**
     * 从倒数第n个"_"开始截取子字符串
     * @param str
     * @param n
     * @return
     */
    public static String getLocation(String str,int n) {
        int index = str.lastIndexOf("_");
        for (int i = 0; i < n - 1; i++) {
            index = str.lastIndexOf("_", index - 1);
        }
        String location = str.substring(0,index);
        return location;
    }
    /**
     * 是否是纯中文
     * @param str
     * @return
     */
    public static boolean checkStrIsChinese(String str){
        String regex3 = "[\\u4e00-\\u9fa5]+";
        boolean result = str.matches(regex3);
        return result;
    }
    /**
     * 获得一个UUID
     * @return String UUID
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }


    //测试一个方法执行所需时间
    public static Long startEndTime(){
        long startTime = System.currentTimeMillis(); //获取开始或结束时间
        return startTime;
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j)
                    || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }


}
