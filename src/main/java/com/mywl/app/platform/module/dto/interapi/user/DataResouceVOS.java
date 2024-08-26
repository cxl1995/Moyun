package com.mywl.app.platform.module.dto.interapi.user;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName DataResouceVOS
 * @Description TODO
 * @Author cxl
 * @Date 2023/7/3 下午5:00
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "person.default")
public class DataResouceVOS {

  @NotBlank(message = "resourceCode 不可为空")
  private String resourceCode;

  @NotBlank(message = "resourceName 不可为空")
  private String resourceName;

  private String resourceType;
}

