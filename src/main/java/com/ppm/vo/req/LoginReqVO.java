package com.ppm.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: LoginReqVO
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 23:03
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 23:03
 * @Version: 0.0.1
 */
@Data
public class LoginReqVO {
    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String username;
    @ApiModelProperty(value = "用户密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    @ApiModelProperty(value = "登录类型(1:pc;2:App)")
    @NotBlank(message = "登录类型不能为空")
    private String type;
}
