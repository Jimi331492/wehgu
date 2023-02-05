package com.wehgu.admin.entities.query;

import com.wehgu.admin.entities.query.BasePageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerPageQuery extends BasePageQuery {

    @ApiModelProperty(value = "微信昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "账号状态")
    private Integer status;

    private String roleUID;

    private String deptUID;
}
