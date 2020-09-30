package com.ppm.mapper;

import com.ppm.entity.Advert;
import com.ppm.vo.req.AdvertPageReqVO;
import com.ppm.vo.req.UserPageReqVO;

import java.util.List;

public interface AdvertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advert record);

    int insertSelective(Advert record);

    Advert selectByPrimaryKey(Integer id);

    List<Advert> findAvert(String userId);

    List<Advert> selectAll(AdvertPageReqVO vo);

    int updateByPrimaryKeySelective(Advert record);

    int updateByPrimaryKey(Advert record);
}