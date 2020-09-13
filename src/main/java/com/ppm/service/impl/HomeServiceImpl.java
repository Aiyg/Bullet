package com.ppm.service.impl;

import com.ppm.entity.SysDept;
import com.ppm.entity.SysUser;
import com.ppm.service.DeptService;
import com.ppm.service.HomeService;
import com.ppm.service.PermissionService;
import com.ppm.service.UserService;
import com.ppm.vo.resp.HomeRespVO;
import com.ppm.vo.resp.PermissionRespNode;
import com.ppm.vo.resp.UserInfoRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: HomeServiceImpl
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 21:27
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 21:27
 * @Version: 0.0.1
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public HomeRespVO getHomeInfo(String userId) {


        SysUser sysUser=userService.detailInfo(userId);
        UserInfoRespVO vo=new UserInfoRespVO();

        if(sysUser!=null){
            BeanUtils.copyProperties(sysUser, vo);
            SysDept sysDept = deptService.detailInfo(sysUser.getDeptId());
            if(sysDept!=null){
                vo.setDeptId(sysDept.getId());
                vo.setDeptName(sysDept.getName());
            }

        }

        List<PermissionRespNode> menus = permissionService.permissionTreeList(userId);

        HomeRespVO respVO=new HomeRespVO();
        respVO.setMenus(menus);
        respVO.setUserInfo(vo);

        return respVO;
    }
}
