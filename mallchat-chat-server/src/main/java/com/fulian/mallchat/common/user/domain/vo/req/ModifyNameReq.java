package com.fulian.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ModifyNameReq {
    @ApiModelProperty("用户名")
    @NotNull
    @Length(max = 6, message = "用户名可别取太长，不然我记不住噢")
    private String name;

}
