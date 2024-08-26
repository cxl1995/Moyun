package com.mywl.app.platform.module.dto.openapi.personcurd.add;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName EditPersonDTO
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/29 上午9:38
 * @Version 1.0
 */

@Data
public class DeletePersonDTO {

  /**
   * 人员id
   */
  @NotBlank(message = "id 不可为空")
  private Long id;



}

