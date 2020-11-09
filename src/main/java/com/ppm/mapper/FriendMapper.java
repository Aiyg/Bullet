package com.ppm.mapper;

import com.ppm.entity.Friend;

public interface FriendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Friend record);

    int insertSelective(Friend record);

    Friend getFriend(Friend record);

    int updateByPrimaryKeySelective(Friend record);

    int updateByPrimaryKey(Friend record);
}