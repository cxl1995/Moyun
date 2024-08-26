package com.mywl.app.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.mywl.app.platform.mapper.meta.MetaDataMapper;
import com.mywl.app.platform.module.dto.interapi.person.AddPersonUserDTO;
import com.mywl.app.platform.module.dto.openapi.personcurd.add.DeletePersonDTO;
import com.mywl.app.platform.module.dto.openapi.personcurd.add.EditPersonDTO;
import com.mywl.app.platform.module.entity.MetaDataEntity;
import com.mywl.app.platform.module.entity.OrgPersonEntity;
import com.mywl.app.platform.response.ApiResponse;
import com.mywl.app.platform.response.ResultCode;
import com.mywl.app.platform.service.PersonService;
import com.mywl.app.platform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cxl
 */
@Slf4j
@RestController
@RequestMapping(value = "/person")
public class PersonController extends BaseController {

  @Autowired
  PersonService personService;

  @Autowired
  UserService userService;

  @Autowired
  MetaDataMapper metaDataMapper;


  /**
   * 查询所有人员信息
   *
   * @return ApiResponse
   */
  @PostMapping(value = "/queryAll")
  public ApiResponse queryAll() {
    try {
      List<OrgPersonEntity> orgPersonEntities = personService.queryAll();
      for (OrgPersonEntity orgPersonEntity : orgPersonEntities) {
        System.out.println(orgPersonEntity);
      }
      MetaDataEntity dataEntity = metaDataMapper.queryByMetaFullName("Property_301322070986_A1");
      System.out.println(dataEntity);
      return success();
    } catch (Exception e) {
      return fail("获取person数据失败:" + e, -1);
    }
  }
  /**
   * 新建用户、客户、租户,同时绑定分组，若没有则创建
   * 弃用状态
   *
   * @return ApiResponse
   */
  @PostMapping(value = "/add")
  public ApiResponse addUser(@RequestBody AddPersonUserDTO dto) {
    try {
      log.info("新建用户、客户、租户 接收参数==={}", dto);

      /* 添加到系统用户以及人员表 */
      ApiResponse response = personService.add(dto);
      if (response.getCode() == -1) {
        return fail(response.getMsg(), ResultCode.FAIL);
      }
      String personId = String.valueOf(response.getData());

      /* 添加到魔云自建的用户表 */
      ApiResponse response1 = personService.addMywlUser(dto, personId);
      if (response1.getCode() == -1) {
        return fail("用户新增失败，请联系管理员" + response1.getMsg());
      }
      return success(personId);
    } catch (Exception e) {
      e.printStackTrace();
      return fail("用户新增失败，请联系管理员" + e.getMessage());
    }
  }

  /**
   * 新建用户、客户、租户,同时绑定分组，若没有则创建
   * 弃用状态
   *
   * @return ApiResponse
   */
//  @PostMapping(value = "/add")
  public ApiResponse addUserOld(@RequestBody AddPersonUserDTO dto) {
    try {
      log.info("新建用户、客户、租户 接收参数==={}", dto);

      /* 添加到系统用户以及人员表 */
      ApiResponse response = personService.add(dto);
      if (response.getCode() == -1) {
        return fail(response.getMsg(), ResultCode.FAIL);
      }
      String personId = String.valueOf(response.getData());

      /* 添加到魔云自建的用户表 */
      ApiResponse response1 = personService.addMywlUser(dto, personId);
      if (response1.getCode() == -1) {
        return fail("用户新增失败，请联系管理员" + response1.getMsg());
      }

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

      /* 通过人员id查询用户id */
      Long userId = personService.getUserId(personId);
      if (userId == null) {
        return fail("通过人员id:" + personId + "查询用户id未查找到，请联系管理员");
      }

      /* 将所建的数据或者查询到的分组绑定到用户上 */
      ApiResponse response4 = personService.UserBindingGroup(groupId.toString(), personId, userId.toString());
      if (response4.getCode() == -1) {
        return fail("将所建的数据分组绑定到用户上失败，请联系管理员" + response4.getMsg());
      }
      return success(personId);
    } catch (Exception e) {
      e.printStackTrace();
      return fail("用户新增失败，请联系管理员" + e.getMessage());
    }
  }

  /**
   * 修改联系方式、即修改账号名称，根据id修改用户表和人员表的user_name字段
   *
   * @param dto EditPersonDTO
   * @return ApiResponse
   */
  @PostMapping(value = "/update/username")
  public ApiResponse editUserName(@RequestBody EditPersonDTO dto) {
    Integer integer2 = userService.updateByPrimaryKey(dto.getUserInternalId(), dto.getUserName());
    Integer integer1 = personService.updateByPrimaryKey(dto.getUserInternalId(), dto.getUserName());
    if (integer1 == 1 && integer2 == 1) {
      log.info("修改成功,dto==={}", dto);
      return success("修改成功");
    }
    return fail("修改失败，请联系管理员", ResultCode.FAIL);
  }


  /**
   * 根据id删除用户表和人员表的数据
   *
   * @param dto dto
   * @return ApiResponse
   */
  @PostMapping(value = "/delete")
  public ApiResponse deleteById(@RequestBody DeletePersonDTO dto) {
    log.info("执行删除操作，删除id为==={}的数据", dto.getId());
    Integer integer = personService.deleteByPrimaryKey(dto.getId());
    if (integer == 1) {
      log.info("删除成功,id==={}", dto.getId());
      return success("删除成功");
    }
    log.error("删除失败，integer==={}", integer);
    return fail("删除失败，请联系管理员:" + integer, ResultCode.FAIL);
  }


  public static void main(String[] args) throws Exception {
//    JSONObject jsonObject = new JSONObject();
//    AddPersonDTO addPersonDTO = new AddPersonDTO();
//    AddPersons addPersons = new AddPersons();
//    addPersons.setCode("zhaoyi");
//    addPersons.setName("赵一");
//    addPersons.setGender("male");
//    addPersons.setMainPositionCode("standard_position");
//    addPersons.setStatus("onWork");
//    List<AddPersons> addPersons1 = new ArrayList<>();
//    addPersons1.add(addPersons);
//    addPersonDTO.setAddPersons(addPersons1);
//    Map<String, Object> hashMap = BeanUtil.beanToMap(addPersonDTO);
//    String jsonString = JSONObject.toJSONString(hashMap);
////    jsonObject.put("code", "123");
////    jsonObject.put("name", "cxl");
////    jsonObject.put("gender", "male");
//    System.out.println(jsonString);
//    String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.PERSON_ADD;
//    String s = HttpPostRequest.sendPostRequest(url, jsonString);
//    System.out.println(s);

//    ArrayList<AddPersons> addPersons = new ArrayList<AddPersons>();
//    AddPersons addPersons1 = new AddPersons();
//    addPersons1.setCode("1");
//    addPersons1.setGender("male");
//    addPersons1.setName("cxl");
//    addPersons.add(addPersons1);
//    System.out.println(addPersons);
  }

}
