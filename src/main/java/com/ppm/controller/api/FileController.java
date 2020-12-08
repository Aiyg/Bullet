/*******************************************************************************
 * Copyright (c) 2010, 2015 git@git.oschina.net:kaiwill/springstage.git
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ppm.controller.api;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName: FileUploadController <br/>
 * Function: 文件上传 [] <br/>
 * date: 2020-02-18 <br/>
 *
 * @author yangkai
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/img")
public class FileController {

	private static final Log log = LogFactory.getLog(FileController.class);

    @RequestMapping(value = "/upload")
    @ResponseBody
    public String uploadImage(MultipartFile file){
        //获取文件名称
        String orgFileName = file.getOriginalFilename();
        /*String date = new SimpleDateFormat("yyyyMM").format(new Date());
        String path = filePath+"/"+date+"/image";*/
        //使用随机数生成文件名称,加点和文件的后缀
        String fileName = new Date().getTime()+orgFileName;
        String path = "/home/image";
        try {
            //新建一个File实例,放入要存的抽象路径,文件的名字
            File targetFile = new File(path, fileName);
            //使用注解流的方式把图片写入文件中
            FileUtils.writeByteArrayToFile(targetFile, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    @RequestMapping(value = "/{fileName}")
    public void readImageFile(HttpServletResponse response, @PathVariable String fileName) throws IOException, URISyntaxException {
        ServletOutputStream os = null;
        try {
            String downLoadPath = "/home/image/" + fileName;
            File file = new File(downLoadPath);
            InputStream imgInputStream = null;
            if (file.exists() && file.canRead()) {
                imgInputStream = new BufferedInputStream(new FileInputStream(file));
            }
            response.reset();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "must-revalidate, no-transform");
            response.setDateHeader("Expires", 0L);
            os = response.getOutputStream();
            IOUtils.copy((InputStream)imgInputStream, os);
            imgInputStream.close();
        }catch (Exception e){
            log.error(e.getStackTrace(),e);
        }finally {
            IOUtils.closeQuietly(os);
            os.close();
        }
    }


}