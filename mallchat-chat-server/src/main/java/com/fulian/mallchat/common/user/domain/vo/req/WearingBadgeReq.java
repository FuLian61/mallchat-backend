package com.fulian.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class WearingBadgeReq {
    @ApiModelProperty("徽章id")
    @NotNull
    private Long itemId;

}
