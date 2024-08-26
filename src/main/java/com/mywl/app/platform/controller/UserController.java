package com.mywl.app.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.mywl.app.platform.constant.CommonConstant;
import com.mywl.app.platform.mapper.meta.MetaDataMapper;
import com.mywl.app.platform.module.dto.openapi.personcurd.add.AddPersonDTO;
import com.mywl.app.platform.module.dto.openapi.personcurd.add.AddPersons;
import com.mywl.app.platform.module.dto.openapi.usercurd.update.LockAccountDTO;
import com.mywl.app.platform.module.entity.AuthUserEntity;
import com.mywl.app.platform.request.HttpRequestUtils;
import com.mywl.app.platform.response.ApiResponse;
import com.mywl.app.platform.response.ResultCode;
import com.mywl.app.platform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author htf
 * @date 2022/10/24 15:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

  @Autowired
  UserService userService;

  @Autowired
  MetaDataMapper metaDataMapper;

  /**
   * 查询所有用户信息
   *
   * @return ApiResponse
   */
  @PostMapping(value = "/queryAll")
  public ApiResponse queryAll() {
    try {
      List<AuthUserEntity> authUserEntities = userService.queryAll();
      for (AuthUserEntity authUserEntity : authUserEntities) {
        System.out.println(authUserEntity);
      }
      return success();
    } catch (Exception e) {
      return fail("获取user数据失败:" + e, -1);
    }
  }

  /**
   * 通过personId批量锁定账号
   *
   * @param lockAccountDTO lockAccountDTO
   * @return ApiResponse
   */
  @PostMapping(value = "/lock/account")
  public ApiResponse lockAccount(@RequestBody LockAccountDTO lockAccountDTO) {
    Timestamp timestamp = new Timestamp(new Date().getTime());
    List<Long> personIds = lockAccountDTO.getPersonIds();
    log.info("账号锁定,lockAccountDTO==={}", lockAccountDTO);
    Integer integer = userService.lockAccount(1, 1, timestamp, personIds);
    if (integer > 0) {
      log.info("账号锁定成功,personIds==={}", personIds);
      return success("锁定成功");
    }
    return fail("锁定失败，请联系管理员,integer", ResultCode.FAIL);
  }

  /**
   * 通过personId批量锁定账号
   *
   * @param lockAccountDTO lockAccountDTO
   * @return ApiResponse
   */
  @PostMapping(value = "/unlock/account")
  public ApiResponse unlockAccount(@RequestBody LockAccountDTO lockAccountDTO) {
    Timestamp timestamp = new Timestamp(new Date().getTime());
    List<Long> personIds = lockAccountDTO.getPersonIds();
    log.info("账号解锁,lockAccountDTO==={}", lockAccountDTO);
    Integer integer = userService.lockAccount(0, null, timestamp, personIds);
    if (integer > 0) {
      log.info("账号解锁成功,personIds==={}", personIds);
      return success("解锁成功");
    }
    return fail("解锁失败，请联系管理员,integer", ResultCode.FAIL);
  }

  /**
   * 新增人员
   *
   * @return ApiResponse
   */
  @PostMapping(value = "/add")
  public ApiResponse addPerson(@RequestBody AddPersonDTO dto) {
    try {
      System.out.println(dto);
      dto.getAddPersons().get(0).setStatus("onWork");
      dto.getAddPersons().get(0).setMainPositionCode("standard_position");
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.PERSON_CURD;
      Map<String, Object> hashMap = BeanUtil.beanToMap(dto);
//      HttpPostRequest.sendPostRequest(url, hashMap.toString());
      Header[] headers = new Header[]{
          new BasicHeader("Content-type", "application/json"),
          new BasicHeader("Authorization", "Bearer " + "f0e14fdb-2c3a-43f3-a982-34a12497d618")
      };
      JSONObject jsonObject = HttpRequestUtils.postData(url, headers, hashMap);
      if (jsonObject == null) {
        log.error("添加person数据失败==={}", hashMap);
        return fail("添加person数据失败:", -1);
      }
      return success();
    } catch (Exception e) {
      return fail("添加person数据失败:" + e, -1);
    }
  }

  public static void main(String[] args) throws Exception {
//    JSONObject jsonObject = new JSONObject();
//    jsonObject.put("code", "123");
//    jsonObject.put("name", "cxl");
//    jsonObject.put("gender", "male");
//    System.out.println(jsonObject.toJSONString());
//    System.out.println(jsonObject.toString());
//    String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.PERSON_QUERY_ALL;
//    String s = HttpPostRequest.sendPostRequest(url, "");
//    System.out.println(s);

    ArrayList<AddPersons> addPersons = new ArrayList<AddPersons>();
    AddPersons addPersons1 = new AddPersons();
    addPersons1.setCode("1");
    addPersons1.setGender("male");
    addPersons1.setName("cxl");
    addPersons.add(addPersons1);
    System.out.println(addPersons);
  }

}
