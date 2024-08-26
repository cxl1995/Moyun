package com.mywl.app.platform.module.dto.openapi.personcurd.add;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;


/**
 * @ClassName AddPersonDTO
 * @Description 添加人员信息DTO
 * @Author cxl
 * @Date 2023/6/8 下午3:17
 * @Version 1.0
 */
@Data
public class AddPersons {

  /**
   * 人员编码
   */
  @NotBlank(message = "code 不可为空")
  private String code;

  /**
   * 人员名称
   */
  @NotBlank(message = "name 不可为空")
  private String name;

  /**
   * 人员性别
   */
  @NotBlank(message = "gender 不可为空")
  private String gender;

  /**
   * 人员状态
   */
  @NotBlank(message = "status 不可为空")
  @Value("${ person.status : onWork }")
  private String status;


  /**
   * 人员主岗编码
   */
  @NotBlank(message = "mainPositionCode 不可为空")
  @Value("${ person.mainPositionCode : standard_position }")
  private String mainPositionCode;



}

