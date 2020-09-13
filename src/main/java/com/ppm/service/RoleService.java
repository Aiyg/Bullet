package com.ppm.service;

import com.ppm.entity.SysRole;
import com.ppm.vo.req.RoleAddReqVO;
import com.ppm.vo.req.RolePageReqVO;
import com.ppm.vo.req.RoleUpdateReqVO;
import com.ppm.vo.resp.PageVO;

import java.util.List;

/**
 * @ClassName: RoleService
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 11:38
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 11:38
 * @Version: 0.0.1
 */
public interface RoleService {

    SysRole addRole(RoleAddReqVO vo);

    void updateRole(RoleUpdateReqVO vo, String accessToken);

    SysRole detailInfo(String id);

    void deletedRole(String id);

    PageVO<SysRole> pageInfo(RolePageReqVO vo);

    List<SysRole> getRoleInfoByUserId(String userId);

    List<String> getRoleNames(String userId);

    List<SysRole> selectAllRoles();
}
