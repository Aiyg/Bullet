package com.ppm.mapper;

import com.ppm.entity.Advert;

import java.util.List;

public interface AdvertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advert record);

    int insertSelective(Advert record);

    Advert selectByPrimaryKey(Integer id);

    List<Advert> findAvert(String userId);

    int updateByPrimaryKeySelective(Advert record);

    int updateByPrimaryKey(Advert record);
}