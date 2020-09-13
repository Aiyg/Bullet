package com.ppm.mapper;

import com.ppm.entity.WxFriend;

public interface WxFriendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxFriend record);

    int insertSelective(WxFriend record);

    WxFriend selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxFriend record);

    int updateByPrimaryKey(WxFriend record);
}