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
@TableName(value = "supngin_oodm_alert_current", schema = "supos_dt")
public class AlertCurrentEntity {

//  @TableId(value = "id",type = IdType.AUTO)
//  private String id;

  private String recordId;

  private String description;

  private String startDatatimestamp;

  private String source;

  private String sourcePropertyName;

  private String sourcePropShowName;

  private String alertType;

  private String newValue;

}

