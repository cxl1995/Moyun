package com.mywl.app.platform.module.dto.openapi.personcurd.add;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName EditPersonDTO
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/29 上午9:38
 * @Version 1.0
 */

@Data
public class EditPersonDTO {

  /**
   * 人员id
   */
  @NotBlank(message = "userInternalId 不可为空")
  private String userInternalId;

  /**
   * 账号名称
   */

  private String userName;

  /**
   * 账号角色
   */

  private List<Long> roles;


}

