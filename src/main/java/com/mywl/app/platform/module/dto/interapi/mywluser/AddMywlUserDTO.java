package com.mywl.app.platform.module.dto.interapi.mywluser;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName AddMywlUserDTO
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/27 下午4:20
 * @Version 1.0
 */

@Data
@Component
@ConfigurationProperties(prefix = "person.default")
public class AddMywlUserDTO {

  @NotBlank(message = "companyId 不可为空")
  @Value("${ person.default.companyId : 1000 }")
  private String userId;

  @NotBlank(message = "name 不可为空")
  private String userName;

  @Value("${ person.default.belongerType : belongerType }")
  private String belongerType;

  @Value("${ person.default.belongerId : belongerId }")
  private String belongerId;

  @Value("${ person.default.belongerName : belongerName }")
  private String belongerName;


  @NotBlank(message = "gender 不可为空")
  @Value("${person.default.gender: sys_gender/male }")
  private String gender;

  @NotBlank(message = "roleId 不可为空")
//  @Value("${ person.default.roleId : [3449320350014480] }")
  private List<Long> roleId;

  @Value("${ person.default.description : '' }")
  private String description;

  @NotBlank(message = "userInternalId 不可为空")
  private String userInternalId;
}

