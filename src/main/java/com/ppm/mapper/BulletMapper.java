package com.ppm.mapper;

import com.ppm.entity.Bullet;
import com.ppm.vo.req.BulletPageReqVO;

import java.util.List;

public interface BulletMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Bullet record);

    int insertSelective(Bullet record);

    Bullet selectByPrimaryKey(Integer id);

    List<Bullet> findBullet(String userId);

    List<Bullet> selectAll(BulletPageReqVO vo);

    int updateByPrimaryKeySelective(Bullet record);

    int updateByPrimaryKey(Bullet record);
}