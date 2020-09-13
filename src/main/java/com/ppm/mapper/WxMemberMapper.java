package com.ppm.mapper;

import com.ppm.entity.WxMember;
import org.apache.ibatis.annotations.Param;

public interface WxMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxMember record);

    int insertSelective(WxMember record);

    WxMember selectByPrimaryKey(Integer id);

    WxMember findOne(@Param("openid") String openid);

    int updateByPrimaryKeySelective(WxMember record);

    int updateByPrimaryKey(WxMember record);
}