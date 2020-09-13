package com.ppm.vo.resp;

import com.ppm.entity.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: UserOwnRoleRespVO
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2019/11/3 21:50
 * @UpdateUser: changguangqi
 * @UpdateDate: 2019/11/3 21:50
 * @Version: 0.0.1
 */
@Data
public class UserOwnRoleRespVO {
    @ApiModelProperty("所有角色集合")
    private List<SysRole> allRole;
    @ApiModelProperty(value = "用户所拥有角色集合")
    private List<String> ownRoles;
}
