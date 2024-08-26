package com.mywl.app.platform.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author htf
 * @date 2022/10/26 15:25
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("auth_user")
public class AuthUserEntity {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String userName;

  private Long personId;

  private int valid;

  private String personCode;

  private String personName;


}
