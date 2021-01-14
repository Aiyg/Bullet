package com.ppm.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UserPageReqVO
 * TODO:类文件简单描述
 * @Author: changguangqi
 * @CreateDate: 2020/05/04 16:45
 * @UpdateUser: changguangqi
 * @UpdateDate: 2020/05/04 16:45
 * @Version: 0.0.1
 */
@Data
public class BulletPageReqVO {
    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "活动名称")
    private String title;

}
