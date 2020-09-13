package com.ppm.service.impl;

import com.github.pagehelper.PageHelper;
import com.ppm.entity.SysLog;
import com.ppm.mapper.SysLogMapper;
import com.ppm.service.LogService;
import com.ppm.utils.PageUtils;
import com.ppm.vo.req.SysLogPageReqVO;
import com.ppm.vo.resp.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: LogServiceImpl
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 16:18
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 16:18
 * @Version: 0.0.1
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public PageVO<SysLog> pageInfo(SysLogPageReqVO vo) {

        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<SysLog> sysLogs = sysLogMapper.selectAll(vo);
        return PageUtils.getPageVO(sysLogs);
    }

    @Override
    public void deleted(List<String> logIds) {
        sysLogMapper.batchDeletedLog(logIds);
    }
}
