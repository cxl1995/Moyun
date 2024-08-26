package com.mywl.app.platform.module.dto;

import lombok.Data;

/**
 * @ClassName AlertDTO
 * @Description TODO
 * @Author cxl
 * @Date 2023/12/12 上午10:05
 * @Version 1.0
 */
@Data
public class AlertDTO {

  /**
   * 模板id
   */
  private String templateId;

  /**
   * 对象实例id
   */
  private String instanceId;
}

