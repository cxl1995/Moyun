package com.mywl.app.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.mywl.app.platform.module.dto.interapi.instance.AddInstanceDTO;
import com.mywl.app.platform.response.ApiResponse;
import com.mywl.app.platform.response.ResultCode;
import com.mywl.app.platform.service.InstanceService;
import com.mywl.app.platform.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象实例
 */
@Slf4j
@RestController
@RequestMapping(value = "/instance")
public class InstanceController extends BaseController {

  @Autowired
  InstanceService instanceService;

  @Autowired
  PersonService personService;

  /**
   * 新建对象实例
   *
   * @return ApiResponse
   */
  @PostMapping(value = "/add")
  public ApiResponse addInstance(@RequestBody AddInstanceDTO dto) {
    try {
      log.info("新增对象实例 接收参数==={}", dto);
      /* 新增对象实例 */
      ApiResponse response = instanceService.add(dto);
      if (response.getCode() == -1) {
        return fail(response.getMsg(), ResultCode.FAIL);
      }
      int instanceId = Integer.parseInt(response.getData().toString());
//      Integer instanceId = (Integer) response.getData();
      /* 判断是否存在 belongerId 的数据分组 */
      String belongerId = dto.getBelongerId();
      String belongerName = dto.getBelongerName();
      ApiResponse dataGroup = personService.getDataGroup(belongerId);
      JSONObject data = (JSONObject) dataGroup.getData();
      Integer total = data.getInteger("total");
      Object groupId = 0;
      if (total == 0) {
        log.info("不存在名称为==={}的数据分组，新建该分组", belongerId);
        /* 不存在时新建数据分组  belongerId   belongerName */
        ApiResponse response2 = personService.addDataGroup(belongerId, belongerName);
        if (response2.getCode() == -1) {
          String msg = "新建用户数据分组失败，请联系管理员" + response2.getMsg();
          log.error(msg);
          return fail(msg);
        }
        groupId = response2.getData();
        /* 给分组配置模板-设备数据模板 */
        ApiResponse response3 = personService.configTemplate(String.valueOf(groupId));
        if (response3.getCode() == -1) {
          String msg = "给分组配置模板-设备数据模板失败，请联系管理员" + response3.getMsg();
          log.error(msg);
          return fail(msg);
        }
      } else {
        groupId = data.getInteger("id");
        log.info("已存在名称为==={}的数据分组，读取分组id==={}", belongerId, groupId);
      }

      /* 将对象实例绑定到用户的分组 */
      List<String> list = new ArrayList<String>();
      list.add(String.valueOf(instanceId));
      instanceService.AssignTemplateInstances(list, String.valueOf(groupId));
      return success(list);
    } catch (Exception e) {
      e.printStackTrace();
      return fail("对象实例新增失败，请联系管理员" + e.getMessage());
    }
  }

}

