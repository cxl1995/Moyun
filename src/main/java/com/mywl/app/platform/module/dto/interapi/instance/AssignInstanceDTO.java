package com.mywl.app.platform.module.dto.interapi.instance;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName AssignInstanceDTO
 * {
 * instanceIds:[
 * "1","2","3"
 * ]
 * }
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/27 下午4:20
 * @Version 1.0
 */

@Data
@Component
@ConfigurationProperties(prefix = "instance.default")
public class AssignInstanceDTO {

  @NotBlank(message = "instanceIds 不可为空")
  private List<String> instanceIds;

}

