package com.mywl.app.platform.module.dto.interapi.instance;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName AddInstanceDTO
 * {
 *   "displayName": "测试新增对象实例",
 *   "parentId": 11208,
 *   "enName": "Instance_203",
 *   "appName": "App_7f011625b3c9b4f9169b4b3d696c5619",
 *   "appAccessMode": "PUBLIC",
 *   "comment": "",
 *   "labelIds": [],
 *   "functionSetIds": []
 * }
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/27 下午4:20
 * @Version 1.0
 */

@Data
@Component
@ConfigurationProperties(prefix = "instance.default")
public class AddInstanceDTO {

  @NotBlank(message = "displayName 不可为空")
  private String displayName;

  private Integer parentId;

  @NotBlank(message = "enName 不可为空")
  private String enName;

  @Value("${ instance.default.appName : App_7f011625b3c9b4f9169b4b3d696c5619 }")
  private String appName;

  @Value("${ instance.default.appAccessMode : PUBLIC }")
  private String appAccessMode;

  @Value("${ instance.default.comment :}")
  private String comment;

  @Value("${ instance.default.labelIds :}")
  private List<String> labelIds;

  @Value("${ instance.default.functionSetIds :}")
  private List<String> functionSetIds;

  @NotBlank(message = "belongerId 不可为空")
  private String belongerId;

  @NotBlank(message = "belongerName 不可为空")
  private String belongerName;

}

