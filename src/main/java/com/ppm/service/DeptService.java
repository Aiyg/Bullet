package com.ppm.service;

import com.ppm.entity.SysDept;
import com.ppm.entity.SysUser;
import com.ppm.vo.req.DeptAddReqVO;
import com.ppm.vo.req.DeptPageReqVO;
import com.ppm.vo.req.DeptUpdateReqVO;
import com.ppm.vo.req.UserPageUserByDeptReqVO;
import com.ppm.vo.resp.DeptRespNodeVO;
import com.ppm.vo.resp.PageVO;

import java.util.List;

/**
 * @ClassName: DeptService
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 11:38
 * @UpdateUser: changguanqi
 * @UpdateDate: 2020/05/04 11:38
 * @Version: 0.0.1
 */
public interface DeptService {

    SysDept addDept(DeptAddReqVO vo);

    void updateDept(DeptUpdateReqVO vo);

    SysDept detailInfo(String id);

    void deleted(String id);

    PageVO<SysDept> pageInfo(DeptPageReqVO vo);


    List<DeptRespNodeVO> deptTreeList(String deptId);

    PageVO<SysUser> pageDeptUserInfo(UserPageUserByDeptReqVO vo);

    List<SysDept> selectAll();
}
