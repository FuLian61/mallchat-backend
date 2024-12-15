package com.fulian.mallchat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户背包表
 * @TableName user_backpack
 */
@TableName(value ="user_backpack")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBackpack implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * uid
     */
    private Long uid;

    /**
     * 物品id
     */
    private Long itemId;

    /**
     * 使用状态 0.待使用 1已使用
     */
    private Integer status;

    /**
     * 幂等号
     */
    private String idempotent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}