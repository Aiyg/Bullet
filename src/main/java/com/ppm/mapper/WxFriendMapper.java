package com.ppm.mapper;

import com.ppm.entity.WxFriend;

import java.util.List;

public interface WxFriendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxFriend record);

    int insertSelective(WxFriend record);

    WxFriend selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxFriend record);

    int updateByPrimaryKey(WxFriend record);

    List<WxFriend> chatList(WxFriend record);

    List<WxFriend> friendList(WxFriend record);
}