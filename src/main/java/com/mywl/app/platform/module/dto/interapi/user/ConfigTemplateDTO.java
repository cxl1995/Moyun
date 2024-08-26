package com.mywl.app.platform.module.dto.interapi.user;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
public class ConfigTemplateDTO {

  private int templateId;

  @Value("${ person.default.templateInstanceType : ALL }")
  private String templateInstanceType;


}