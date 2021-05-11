package com.ppm.utils;

import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.AlgorithmParameterSpec;

public class WeiXinUtil {

    final static private String appid = "wxa3234f15461aface";
    final static private String secret = "243d9b2852cda6e3a812846a7cae249f";
    // 获取token
    public static String getAccessToken(String appid,String serect) {
        String access_token = "";
        String grant_type = "client_credential";// 获取access_token填写client_credential
        // 这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grant_type + "&appid=" + appid + "&secret="
                + serect; // 访问链接

        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            /*
             * System.setProperty("sun.net.client.defaultConnectTimeout",
             * "30000");// 连接超时30秒
             * System.setProperty("sun.net.client.defaultReadTimeout", "30000");
             * // 读取超时30秒
             */
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.parseObject(message);
            access_token = demoJson.getString("access_token");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    public static boolean validContent(String content){
        String token=getAccessToken(appid,secret);
        String url="https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+token;
        JSONObject json  = new JSONObject();
        json.put("content",content);
        JSONObject jsonObject = HttpTools.httpsRequest(url, "POST", json.toString());
        System.out.println(jsonObject);
        if("ok".equals(String.valueOf(jsonObject.get("errmsg")))){
            return true;
        }else{
            return false;
        }

    }

    // 获取ticket
    public static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";// 这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.parseObject(message);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }


    /*public static String decrypt(String encrypdata, String ivdata, String sessionkey) throws Exception {
        byte[] encData = Base64.decode(encrypdata);
        byte[] iv = Base64.decode(ivdata);
        byte[] key = Base64.decode(sessionkey);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串  
        JSONObject jsonObject = JSONObject.parseObject(new String(cipher.doFinal(encData),"UTF-8"));
        return jsonObject.get("phoneNumber").toString();
    }*/

    public static String decryptS5(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] raw = decoder.decodeBuffer(sKey);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(decoder.decodeBuffer(ivParameter));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] myendicod = decoder.decodeBuffer(sSrc);
            byte[] original = cipher.doFinal(myendicod);
            String originalString = new String(original, encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

}
