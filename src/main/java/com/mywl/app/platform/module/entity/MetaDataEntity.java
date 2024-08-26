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
@TableName("meta_data")
public class MetaDataEntity {

  @TableId(type = IdType.AUTO)
  private Integer metaId;

  private String metaInstName;

  private String metaTagName;

  private String metaFullName;

//  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private String createTime;
}
