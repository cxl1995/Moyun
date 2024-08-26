package com.mywl.app.platform.module.dto.interapi.user;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName UserBindingGroupDTO
 * @Description UserBindingGroupDTO
 * @Author cxl
 * @Date 2023/6/8 下午4:14
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "person.default")
public class UserBindingGroupDTO {

  @NotBlank(message = "controlled 不可为空")
  private boolean controlled;

  @NotBlank(message = "dataResouceVOS 不可为空")
  private List<DataResouceVOS> dataResouceVOS;


}