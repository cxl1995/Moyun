package com.mywl.app.platform.module.dto.interapi.user;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

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
public class AddUserGroupDTO {

  @NotBlank(message = "displayName 不可为空")
  private String displayName;

  private String comment;


}