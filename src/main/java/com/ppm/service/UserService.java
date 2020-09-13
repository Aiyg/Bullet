package com.ppm.service;

import com.ppm.entity.SysUser;
import com.ppm.vo.req.*;
import com.ppm.vo.resp.LoginRespVO;
import com.ppm.vo.resp.PageVO;
import com.ppm.vo.resp.UserOwnRoleRespVO;

import java.util.List;

/**
 * @ClassName: UserService
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 22:55
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 22:55
 * @Version: 0.0.1
 */
public interface UserService {

    String register(RegisterReqVO vo);

    LoginRespVO login(LoginReqVO vo);


    String refreshToken(String refreshToken, String accessToken);

    void updateUserInfo(UserUpdateReqVO vo, String operationId);


    PageVO<SysUser> pageInfo(UserPageReqVO vo);

    SysUser detailInfo(String userId);

    PageVO<SysUser> selectUserInfoByDeptIds(int pageNum, int pageSize, List<String> deptIds);

    void addUser(UserAddReqVO vo);

    void logout(String accessToken, String refreshToken);

    void updatePwd(UpdatePasswordReqVO vo, String userId, String accessToken, String refreshToken);

    List<SysUser> getUserListByDeptId(String deptId);

    List<SysUser> getUserListByDeptIds(List<String> deptIds);

    void deletedUsers(List<String> userIds, String operationId);

    UserOwnRoleRespVO getUserOwnRole(String userId);

    void setUserOwnRole(String userId, List<String> roleIds);
}
