package com.mywl.app.platform.module.dto.openapi.personcurd.add;

import lombok.Data;

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
public class AddPersonDTO {

  /**
   * 人员信息
   */
  @NotBlank(message = "addPersons 不可为空")
  private List<AddPersons> addPersons ;
}

