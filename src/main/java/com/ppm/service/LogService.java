package com.ppm.service;


import com.ppm.entity.SysLog;
import com.ppm.vo.req.SysLogPageReqVO;
import com.ppm.vo.resp.PageVO;

import java.util.List;

/**
 * @ClassName: LogService
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 16:17
 * @UpdateUser: changguanqi
 * @UpdateDate: 2020/05/04 16:17
 * @Version: 0.0.1
 */
public interface LogService {

    PageVO<SysLog> pageInfo(SysLogPageReqVO vo);

    void deleted(List<String> logIds);
}
