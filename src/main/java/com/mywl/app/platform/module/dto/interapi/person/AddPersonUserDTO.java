package com.mywl.app.platform.module.dto.interapi.person;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName AddPersonUserDTO
 * @Description AddPersonUserDTO
 * @Author cxl
 * @Date 2023/6/8 下午4:14
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "person.default")
public class AddPersonUserDTO {

  @NotBlank(message = "companyId 不可为空")
  @Value("${ person.default.companyId : 1000 }")
  private int companyId;

  @NotBlank(message = "name 不可为空")
  private String name;

  @NotBlank(message = "code 不可为空")
  private String code;

  @NotBlank(message = "gender 不可为空")
  @Value("${person.default.gender: sys_gender/male }")
  private String gender;

  @NotBlank(message = "mainPosition 不可为空")
  @Value("${ person.default.mainPosition:2 }")
  private int mainPosition;

  @NotBlank(message = "status 不可为空")
  @Value("${ person.default.status:status }")
  private String status;

  @Value("${ person.default.createUser : true }")
  private boolean createUser;

  @NotBlank(message = "phone 不可为空")
  @Value("${ person.default.phone : '' }")
  private String phone;

//  @Value("${ person.default.email : '123@qq.com' }")
  private String email;

  @Value("${ person.default.description : '' }")
  private String description;

  private String idNumber;

  @NotBlank(message = "userName 不可为空")
  private String userName;

  @NotBlank(message = "password 不可为空")
  @Value("${ person.default.password : 88888888 }")
  private String password;

  @NotBlank(message = "roles 不可为空")
//  @Value("${ person.default.roles : [3449320350014480] }")
  private List<Long> roles;

  @Value("${ person.default.belongerType : belongerType }")
  private String belongerType;

  @NotBlank(message = "phone 不可为空")
  @Value("${ person.default.belongerId : belongerId }")
  private String belongerId;

  @NotBlank(message = "phone 不可为空")
  @Value("${ person.default.belongerName : belongerName }")
  private String belongerName;


}