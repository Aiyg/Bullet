package com.ppm.service;


import com.ppm.entity.SysPermission;
import com.ppm.vo.req.PermissionAddReqVO;
import com.ppm.vo.req.PermissionPageReqVO;
import com.ppm.vo.req.PermissionUpdateReqVO;
import com.ppm.vo.resp.PageVO;
import com.ppm.vo.resp.PermissionRespNode;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: PermissionService
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 11:39
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 11:39
 * @Version: 0.0.1
 */
public interface PermissionService {

    List<SysPermission> getPermission(String userId);

    SysPermission addPermission(PermissionAddReqVO vo);

    SysPermission detailInfo(String permissionId);

    void updatePermission(PermissionUpdateReqVO vo);

    void deleted(String permissionId);

    PageVO<SysPermission> pageInfo(PermissionPageReqVO vo);

    List<SysPermission> selectAll();

    Set<String> getPermissionsByUserId(String userId);

    List<PermissionRespNode> permissionTreeList(String userId);

    List<PermissionRespNode> selectAllByTree();

    List<PermissionRespNode> selectAllMenuByTree(String permissionId);

}
