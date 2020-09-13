package com.ppm.mapper;

import com.ppm.entity.BulletSendRecord;

import java.util.List;

public interface BulletSendRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BulletSendRecord record);

    int insertSelective(BulletSendRecord record);

    BulletSendRecord selectByPrimaryKey(Integer id);

    List<BulletSendRecord> findBulletList(BulletSendRecord bulletSendRecord);

    int updateByPrimaryKeySelective(BulletSendRecord record);

    int updateByPrimaryKey(BulletSendRecord record);
}