package com.mywl.app.platform.controller;

import com.mywl.app.platform.module.dto.MetaDataDTO;
import com.mywl.app.platform.module.dto.MetaTagDTO;
import com.mywl.app.platform.module.dto.WriteDownDTO;
import com.mywl.app.platform.response.ApiResponse;
import com.mywl.app.platform.response.ResultCode;
import com.mywl.app.platform.service.DataUploadService;
import com.mywl.app.platform.service.DataWriteDownService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author htf
 * @date 2022/10/24 15:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/mqtt")
public class MqttController extends BaseController {

  @Autowired
  DataWriteDownService dataWriteDownService;

  @Autowired
  DataUploadService dataUploadService;

  @PostMapping(value = "/sendMessage/alarmRules")
  public ApiResponse sendMessage(@RequestBody @Validated WriteDownDTO dto) {
    Double value = null;
    try {
      value = Double.valueOf(dto.getValue());
    } catch (Exception e) {
      log.info("输入值无法转换成double类型，输入值为={}", dto.getValue());
      return fail("输入值无法转换成double类型，输入值为=" + dto.getValue(), -1);
    }
    int i = dataWriteDownService.writeDownMessage(dto.getMetaInstName(), dto.getMetaTagName(), value);
    if (i == 1) {
      return success();
    } else {
      return fail("未查询到该点位，无法执行下写操作", -1);
    }
  }

  @PostMapping(value = "/add/metaData")
  public ApiResponse addMetaData(@RequestBody @Validated MetaDataDTO dto) {
    String metaFullName = "Property_" + dto.getMetaInstName() + "_" + dto.getMetaTagName();
    /* 添加数据库 */
    try {
      Integer count = dataUploadService.handleDataSource(metaFullName);
      log.info("count====={}", count);
      if (count == 1) {
        Boolean aBoolean = dataUploadService.addRedis(metaFullName);
        if (!aBoolean) {
          log.error("redis添加失败==={}", metaFullName);
        }
        /*上传元数据 */
        MetaTagDTO metaTagDTO = new MetaTagDTO();
        metaTagDTO.setVersion(1);
        metaTagDTO.setName(metaFullName);
        dataUploadService.sendMetaData(metaTagDTO);
        return success();
      } else if (count == 2) {
        return fail("数据库已经存在metaFullName=:" + metaFullName, 2000);
      } else if (count == 0) {
        return fail("添加到数据库失败，metaFullName:" + metaFullName, -1);
      } else {
        return fail("未知错误，添加到数据库失败，metaFullName:" + metaFullName + ",count===" + count, -1);
      }
    } catch (Exception e) {
      return fail("上传元数据失败，metaFullName:" + metaFullName, -1);
    }
  }

  @PostMapping(value = "/delete/metaData")
  public ApiResponse deleteMetaData(@RequestBody @Validated MetaDataDTO dto) {
    String metaFullName = "Property_" + dto.getMetaInstName() + "_" + dto.getMetaTagName();
    try {
      Boolean Boolean_mysql = dataUploadService.deleteMetaData(metaFullName);
      Boolean Boolean_redis = dataUploadService.deleteRedis(metaFullName);
      if (Boolean_mysql && Boolean_redis) {
        log.info("删除成功metaFullName==={}", metaFullName);
        return success();
      } else {
        log.error("数据库或redis未能正确删除,Boolean_mysql=" + Boolean_mysql + "Boolean_redis=" + Boolean_redis);
        return fail("数据库或redis未能正确删除,Boolean_mysql=" + Boolean_mysql + "Boolean_redis=" + Boolean_redis, ResultCode.FAIL.code());
      }
    } catch (Exception e) {
      e.getStackTrace();
      return fail(e.getMessage(), ResultCode.FAIL.code());
    }

  }
}
