package com.mywl.app.platform.module.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName MywlUserEntity
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/26 下午1:36
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName(value = "oodm_incr_mywl_sys_user", schema = "supos_dt")
public class MywlUserEntity {

//  @TableId(type = IdType.AUTO)
//  private String systemId;

  private String userId;

  private String userName;

  private String belongerType;

  private String belongerName;

  private String belongerId;

  private String roleId;

  private String description;

  private String userInternalId;

  private String gender;
}

