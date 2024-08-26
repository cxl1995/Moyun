/**
 * Copyright 2022 bejson.com
 */
package com.mywl.app.platform.module.dto;


import javax.validation.constraints.NotBlank;

/**
 * Auto-generated: 2022-12-08 8:28:25
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@lombok.Data
public class MetaDataDTO {

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


}