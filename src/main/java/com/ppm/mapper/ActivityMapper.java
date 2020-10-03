package com.ppm.mapper;

import com.ppm.entity.Activity;
import com.ppm.vo.req.ActivityPageReqVO;

import java.util.List;

public interface ActivityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Activity record);

    int insertSelective(Activity record);

    Activity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKey(Activity record);

    List<Activity> selectAll(ActivityPageReqVO vo);
}