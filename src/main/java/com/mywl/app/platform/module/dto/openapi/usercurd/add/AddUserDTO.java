package com.mywl.app.platform.module.dto.openapi.usercurd.add;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName AddPersonDTO
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/8 下午4:14
 * @Version 1.0
 */
@Data
public class AddUserDTO {

  /**
   * 人员名称
   */
  @NotBlank(message = "username 不可为空")
  private String username;

  /**
   * 人员密码
   */
  @NotBlank(message = "password 不可为空")
  @Value("${ user.password : 12345678 }")
  private String password;

  /**
   * 时区
   */
  @NotBlank(message = "timeZone 不可为空")
  @Value("${ user.timeZone : GMT+0800 }")
  private String timeZone;

  /**
   * 人员编码
   */
  @NotBlank(message = "personCode 不可为空")
  private String personCode;

  /**
   * 公司编码
   */
  @NotBlank(message = "companyCode 不可为空")
  @Value("${ user.companyCode : default_org_company }")
  private String companyCode;

  /**
   * 用户类型
   */
  @NotBlank(message = "accountType 不可为空")
  @Value("${ user.accountType : 0 }")
  private String accountType;

  /**
   * 角色编码集合
   */
  private List<String> roleNameList;

  /**
   * 是否恢复已删除的用户 默认值为false，不恢复已删除的用户
   */
  @NotBlank(message = "recoveryDeleted 不可为空")
  @Value("${ person.recoveryDeleted : false }")
  private String recoveryDeleted;

}

