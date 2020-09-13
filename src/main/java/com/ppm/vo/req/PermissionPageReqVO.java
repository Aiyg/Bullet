package com.ppm.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PermissionPageReqVO
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 13:59
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 13:59
 * @Version: 0.0.1
 */
@Data
public class PermissionPageReqVO {
    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;
}
