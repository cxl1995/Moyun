package com.mywl.app.platform.module.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageDTO {

  /**
   * 点位名称
   */
  @NotBlank(message = "name 不可为空")
  private String name;

  /**
   * 点位值
   */
  @NotBlank(message = "value 不可为空")
  private String value;

}
