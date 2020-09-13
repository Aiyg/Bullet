package com.ppm.mapper;

import com.ppm.entity.Bullet;

public interface BulletMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Bullet record);

    int insertSelective(Bullet record);

    Bullet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Bullet record);

    int updateByPrimaryKey(Bullet record);
}