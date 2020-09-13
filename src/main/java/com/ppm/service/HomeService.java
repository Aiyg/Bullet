package com.ppm.service;


import com.ppm.vo.resp.HomeRespVO;

/**
 * @ClassName: HomeService
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 21:27
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 21:27
 * @Version: 0.0.1
 */
public interface HomeService {

    HomeRespVO getHomeInfo(String userId);
}
