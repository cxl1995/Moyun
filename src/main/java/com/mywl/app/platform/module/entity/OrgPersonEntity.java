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
@TableName(value = "org_person", schema = "supos_dt")
public class OrgPersonEntity {

  @TableId(type = IdType.AUTO)
  private Long id;

  private int valid;

  private String code;

  private String name;

  private Long userId;

}
