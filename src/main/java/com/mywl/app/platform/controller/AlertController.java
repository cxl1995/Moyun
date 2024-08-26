package com.mywl.app.platform.controller;

import com.mywl.app.platform.mapper.meta.MetaDataMapper;
import com.mywl.app.platform.module.dto.AlertDTO;
import com.mywl.app.platform.module.entity.AlertCurrentEntity;
import com.mywl.app.platform.response.ApiResponse;
import com.mywl.app.platform.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author htf
 * @date 2022/10/24 15:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/alert")
public class AlertController extends BaseController {

  @Autowired
  AlertService alertService;

  @Autowired
  MetaDataMapper metaDataMapper;

  /**
   * 查询所有用户信息
   *
   * @return ApiResponse
   */
  @PostMapping(value = "/queryAll")
  public ApiResponse queryAll(@RequestBody @Validated AlertDTO dto) {
    try {
      List<AlertCurrentEntity> alertCurrentEntities = alertService.queryAll(dto.getTemplateId(),dto.getInstanceId());
      for (AlertCurrentEntity alertCurrentEntity : alertCurrentEntities) {
        System.out.println(alertCurrentEntity);
      }
      return success(alertCurrentEntities);
    } catch (Exception e) {
      return fail("获取alert数据失败:" + e, -1);
    }
  }

}
