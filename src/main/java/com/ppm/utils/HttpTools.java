package com.ppm.utils;

import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: http utils </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author LiLu
 * @version 1.0
 */
public class HttpTools {


    private static final String URL_PARAM_CONNECT_FLAG = "&";
    private static final int SIZE 	= 1024 * 1024;
    private static Log log = LogFactory.getLog(HttpTools.class);

    private HttpTools() {
    }

    /**
     * GET METHOD
     * @param strUrl String
     * @param map Map
     * @throws IOException
     * @return List
     */
    public static List URLGet(String strUrl, Map map) throws IOException {
        String strtTotalURL = "";
        List result = new ArrayList();
        if(strtTotalURL.indexOf("?") == -1) {
            strtTotalURL = strUrl + "?" + getUrl(map);
        } else {
            strtTotalURL = strUrl + "&" + getUrl(map);
        }
        log.debug("strtTotalURL:" + strtTotalURL);
        System.out.println("strtTotalURL="+strtTotalURL);
        URL url = new URL(strtTotalURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setFollowRedirects(true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()),SIZE);
        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            else {
                result.add(line);
            }
        }
        in.close();
        return (result);
    }

    /**
     * POST METHOD
     * @param strUrl String
     * @throws IOException
     * @return List
     */
    public static List URLPost(String strUrl, Map map) throws IOException {

        String content = "";
        content = getUrl(map);
        String totalURL = null;
        if(strUrl.indexOf("?") == -1) {
            totalURL = strUrl + "?" + content;
        } else {
            totalURL = strUrl + "&" + content;
        }

        System.out.println("totalURL : " + totalURL);

        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setAllowUserInteraction(false);
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=GBK");
        BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.
                getOutputStream()));
        bout.write(content);
        bout.flush();
        bout.close();
        BufferedReader bin = new BufferedReader(new InputStreamReader(con.
                getInputStream()),SIZE);
        List result = new ArrayList();
        while (true) {
            String line = bin.readLine();
            if (line == null) {
                break;
            }
            else {
                result.add(line);
            }
        }
        return (result);
    }

    /**
     * ���URL
     * @param map Map
     * @return String
     */
    private static String getUrl(Map map) {
        if (null == map || map.keySet().size() == 0) {
            return ("");
        }
        StringBuffer url = new StringBuffer();
        Set keys = map.keySet();
        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = String.valueOf(i.next());
            if (map.containsKey(key)) {
                Object val = map.get(key);
                String str = val!=null?val.toString():"";
                try {
                    str = URLEncoder.encode(str, "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url.append(key).append("=").append(str).
                        append(URL_PARAM_CONNECT_FLAG);
            }
        }
        String strURL = "";
        strURL = url.toString();
        if (URL_PARAM_CONNECT_FLAG.equals("" + strURL.charAt(strURL.length() - 1))) {
            strURL = strURL.substring(0, strURL.length() - 1);
        }
        return (strURL);
    }

    public static String getShortUrl(String longUrl) throws UnsupportedEncodingException {
        String requestUrl = "http://sa.sogou.com/gettiny?url="+URLEncoder.encode(longUrl, "UTF-8");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 先从线程中取值，如果取不到，说明没有使用线程，再利用这个方法获取
        // 因为发送信息等操作，都是调用的这个方法，所以在这里进行处理一下吧
        HttpGet httpget = new HttpGet(requestUrl);
        // 发送请求
        CloseableHttpResponse response = null;
            try {
            response = httpclient.execute(httpget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 得到响应提
        HttpEntity entity = response.getEntity();
        String param = null;
        try {
            param = EntityUtils.toString(entity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return param;
    }

    public static JSONObject postRequest(String requestUrl) throws UnsupportedEncodingException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 先从线程中取值，如果取不到，说明没有使用线程，再利用这个方法获取
        // 因为发送信息等操作，都是调用的这个方法，所以在这里进行处理一下吧
        HttpPost httpPost = new HttpPost(requestUrl);
        // 发送请求
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 得到响应提
        HttpEntity entity = response.getEntity();
        String param = null;
        try {
            param = EntityUtils.toString(entity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return JSONObject.fromObject(param);
    }

    /***
     * https请求专用方法
     * @param requestUrl
     * @param requestMethod "GET" "POST"
     * @param outputStr
     * @return
     */
    public static com.alibaba.fastjson.JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        com.alibaba.fastjson.JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
           /* TrustManager[] tm = new TrustManager[]{};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();*/
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            /* conn.setSSLSocketFactory(ssf);*/
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);// 5秒 连接主机的超时时间（单位：毫秒）
            conn.setReadTimeout(5000);// 5秒 从主机读取数据的超时时间（单位：毫秒）
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = com.alibaba.fastjson.JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("连接超时：{}", ce);
        } catch (Exception e) {
            log.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

    /***
     * http请求专用方法
     * @param requestUrl
     * @param requestMethod "GET" "POST"
     * @param outputStr
     * @return
     */
    public static com.alibaba.fastjson.JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        com.alibaba.fastjson.JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
           /* TrustManager[] tm = new TrustManager[]{};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();*/
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            /* conn.setSSLSocketFactory(ssf);*/
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);// 5秒 连接主机的超时时间（单位：毫秒）
            conn.setReadTimeout(5000);// 5秒 从主机读取数据的超时时间（单位：毫秒）
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = com.alibaba.fastjson.JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("连接超时：{}", ce);
        } catch (Exception e) {
            log.error("http请求异常：{}", e);
        }
        return jsonObject;
    }

    /**
     * 获取数据流
     *
     * @param url
     * @param paraMap
     * @return
     */
    public static byte[] doImgPost(String url, Map<String, Object> paraMap) {
        byte[] result = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        HttpServletResponse response1=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try{
            // 设置请求的参数
            JSONObject postData = new JSONObject();
            for (Map.Entry<String, Object> entry : paraMap.entrySet()) {
                postData.put(entry.getKey(), entry.getValue());
            }
            httpPost.setEntity(new StringEntity(postData.toString(), "UTF-8"));
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toByteArray(entity);
            ServletOutputStream out;
            out = response1.getOutputStream();
            byte data[] = result;
            out.write(data);
            out.close();

        } catch (Exception e) {
            System.out.println("调用异常");
        }finally {
            httpPost.releaseConnection();
        }
        return result;
    }

    public static  String connectHttpsByPost(String path, File file) throws IOException {
        URL urlObj = new URL(path);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        String result = null;
        con.setDoInput(true);

        con.setConnectTimeout(5000);// 5秒 连接主机的超时时间（单位：毫秒）
        con.setReadTimeout(5000);// 5秒 从主机读取数据的超时时间（单位：毫秒）
        con.setDoOutput(true);

        con.setUseCaches(false); // post方式不能使用缓存

        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type",
                "multipart/form-data; boundary="
                        + BOUNDARY);

        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length() + "\";filename=\""

                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);

        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return result;
    }

    /**
     * post请求
     *
     * @param url
     * @return
     */

    public static JSONObject doPost(String url,String data) {
        HttpClient client = HttpClients.createDefault();
        // 将接口地址和接口方法拼接起来
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(data,"utf-8");
            post.addHeader("Content-Type", "application/json;charset=utf-8");
            post.setEntity(s);
            // 调用Fa接口
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = JSONObject.fromObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

}

