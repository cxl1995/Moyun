package com.mywl.app.platform.module.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MetaTagDTO {
  /**
   * 位号版本，位号信息发生变化时进行更新
   */
  @NotBlank(message = "version 不可为空")
  private int version;

  /**
   * 位号名称（字符开头，不能包含非法字符）
   */
  @NotBlank(message = "name 不可为空")
  private String name;

  /**
   * 位号可读名称 (任意字符，可选)
   */
  @NotBlank(message = "showName 不可为空")
  private String showName;

  /**
   * 位号描述 (可选)
   */
  @NotBlank(message = "description 不可为空")
  private String description;

  /**
   * 位号值类型
   */
  @NotBlank(message = "type 不可为空")
  private String type;

  /**
   * 位号单位 (可选)
   */
  @NotBlank(message = "unit 不可为空")
  private String unit;

  /**
   * 位号量程(下限-上限)，值域取闭区间，如0-100 (可选)
   */
  @NotBlank(message = "range 不可为空")
  private String range;

  /**
   * 位号默认值 (可选)
   */
  @NotBlank(message = "defaultValue 不可为空")
  private String defaultValue;

  /**
   * 位号存档置，true:表示记存档，false:表示不记存档 (可选)
   */
  @NotBlank(message = "storage 不可为空")
  private String storage;

  /**
   * 位号压缩设置 (可选)
   */
  @NotBlank(message = "compress 不可为空")
  private String compress;

}
