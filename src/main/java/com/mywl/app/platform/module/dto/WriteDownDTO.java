package com.mywl.app.platform.module.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WriteDownDTO {
  /**
   * 设备id
   */
  @NotBlank(message = "metaInstName 不可为空")
  private String metaInstName;

  /**
   * 点位名称
   */
  @NotBlank(message = "metaTagName 不可为空")
  private String metaTagName;


  /**
   * 点位值
   */
  @NotBlank(message = "value 不可为空")
  private String value;
}
