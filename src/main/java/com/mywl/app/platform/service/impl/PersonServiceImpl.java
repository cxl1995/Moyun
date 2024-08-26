package com.mywl.app.platform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mywl.app.platform.constant.CommonConstant;
import com.mywl.app.platform.mapper.user.OrgPersonMapper;
import com.mywl.app.platform.module.dto.interapi.mywluser.AddMywlUserDTO;
import com.mywl.app.platform.module.dto.interapi.person.AddPersonUserDTO;
import com.mywl.app.platform.module.dto.interapi.user.AddUserGroupDTO;
import com.mywl.app.platform.module.dto.interapi.user.ConfigTemplateDTO;
import com.mywl.app.platform.module.dto.interapi.user.DataResouceVOS;
import com.mywl.app.platform.module.dto.interapi.user.UserBindingGroupDTO;
import com.mywl.app.platform.module.entity.OrgPersonEntity;
import com.mywl.app.platform.response.ApiResponse;
import com.mywl.app.platform.response.ResultCode;
import com.mywl.app.platform.service.PersonService;
import com.mywl.app.platform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.mywl.app.platform.service.impl.InstanceServiceImpl.loadingCache;

/**
 * @ClassName PersonServiceImpl
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/7 下午3:26
 * @Version 1.0
 */
@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

  @Autowired
  private OrgPersonMapper orgPersonMapper;

  @Autowired
  UserService userService;

  @Autowired
  AddPersonUserDTO addPersonUserDTO;

  @Autowired
  AddMywlUserDTO addMywlUserDTO;

  @Autowired
  ConfigTemplateDTO configTemplateDTO;

  @Autowired
  UserBindingGroupDTO userBindingGroupDTO;
  @Resource
  private RestTemplate restTemplate;

  @Override
  public List<OrgPersonEntity> queryAll() {
    List<OrgPersonEntity> orgPersonEntities = orgPersonMapper.selectAllPerson();
    return orgPersonEntities;
  }

  /**
   * 通过人员id更新用户账号名称（手机号）
   *
   * @param personId 人员id
   * @param userName 账号名称（手机号）
   * @return
   */
  @Override
  public Integer updateByPrimaryKey(String personId, String userName) {
    int i = orgPersonMapper.updateByPrimaryKey(personId, userName);
    return i;
  }

  /**
   * 通过人员id删除人员，同时删除用户以及自建表的用户
   *
   * @param id 人员id
   * @return Integer
   */
  @Override
  public Integer deleteByPrimaryKey(Long id) {
    int i = orgPersonMapper.deleteByPrimaryKey(id);
    return i;
  }

  /**
   * 新建人员和用户，同时将人员与用户绑定
   *
   * @param dto AddPersonUserDTO
   * @return ApiResponse
   */
  @Override
  public ApiResponse add(AddPersonUserDTO dto) throws ExecutionException {
    //header参数
    HttpHeaders headers = getHeaders();
    ApiResponse apiResponse = null;
    try {
      addPersonUserDTO.setName(dto.getName());
      addPersonUserDTO.setUserName(dto.getPhone());
      addPersonUserDTO.setCode(dto.getCode());
      addPersonUserDTO.setRoles(dto.getRoles());
      addPersonUserDTO.setGender(dto.getGender());
      addPersonUserDTO.setPhone(dto.getPhone());
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.PERSON_ADD;
//      Map<String, Object> hashMap = BeanUtil.beanToMap(addPersonUserDTO);
      log.info("添加到系统用户、人员表==={}", addPersonUserDTO);
      //组装
      HttpEntity<AddPersonUserDTO> httpEntity = new HttpEntity<>(addPersonUserDTO, headers);
      ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, httpEntity, JSONObject.class);

      HttpStatus statusCode = response.getStatusCode();
      Object data = Objects.requireNonNull(response.getBody()).getObject("data", Object.class);
      apiResponse = new ApiResponse(response.getBody().toString(), data, ResultCode.SUCCESS.code());
      return apiResponse;
    } catch (Exception e) {
      e.printStackTrace();
      String message = e.getMessage();
      apiResponse = new ApiResponse(e.getMessage(), "error", ResultCode.FAIL.code());
      return apiResponse;
    }

  }

  /**
   * 新建用户分组
   *
   * @param personId 人员id
   * @return ApiResponse
   */
  @Override
  public ApiResponse addDataGroup(String personId, String comment) throws ExecutionException {
    //header参数
    HttpHeaders headers = getHeaders();
    ApiResponse apiResponse = null;
    try {
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.ADD_DATA_GROUP;
      //组装
      AddUserGroupDTO dto = new AddUserGroupDTO();
      dto.setDisplayName(personId);
      dto.setComment(comment);
      log.info("新增数据分组dto==={},url==={}", dto, url);
      HttpEntity<AddUserGroupDTO> httpEntity = new HttpEntity<>(dto, headers);
      ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
      Object groupId = Objects.requireNonNull(response.getBody()).getObject("data", Object.class);
      apiResponse = new ApiResponse(response.getBody().toString(), groupId, ResultCode.SUCCESS.code());
      return apiResponse;
    } catch (Exception e) {
      e.printStackTrace();
      apiResponse = new ApiResponse(e.getMessage(), "error", ResultCode.FAIL.code());
      return apiResponse;
    }
  }

  /**
   * 给用户分组配置对象实例模板
   *
   * @param groupId 分组id
   * @return ApiResponse
   */
  @Override
  public ApiResponse configTemplate(String groupId) throws ExecutionException {
    HttpHeaders headers = getHeaders();
    ApiResponse apiResponse = null;
    try {
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.CONFIG_TEMPLATE;
      url = url.replace("groupId", groupId);
      ArrayList<ConfigTemplateDTO> arrayList = new ArrayList<>();
      configTemplateDTO.setTemplateId(11208);
      configTemplateDTO.setTemplateInstanceType("PART");
      arrayList.add(configTemplateDTO);
      log.info("数据分组配置模板arrayList==={},url==={}", arrayList, url);
      HttpEntity<List<ConfigTemplateDTO>> httpEntity = new HttpEntity<>(arrayList, headers);
      ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
      apiResponse = new ApiResponse(Objects.requireNonNull(response.getBody()).toString(), null, ResultCode.SUCCESS.code());
      return apiResponse;
    } catch (Exception e) {
      e.printStackTrace();
      apiResponse = new ApiResponse(e.getMessage(), "error", ResultCode.FAIL.code());
      return apiResponse;
    }
  }

  /**
   * 通过人员id查询用户id
   *
   * @param personId 人员id
   * @return Long
   */
  @Override
  public Long getUserId(String personId) {
    OrgPersonEntity personEntity = orgPersonMapper.getUserId(personId);
    if (personEntity != null) {
      return personEntity.getUserId();
    }
    return null;
  }

  /**
   * 给用户绑定分组，控制用户权限
   *
   * @param groupId   分组id
   * @param groupName 分组名称
   * @param userId    用户id
   * @return ApiResponse
   */
  @Override
  public ApiResponse UserBindingGroup(String groupId, String groupName, String userId) throws ExecutionException {
    HttpHeaders headers = getHeaders();
    ApiResponse apiResponse = null;
    try {
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.USER_BINDING_GROUP;
      url = url.replace("userId", userId);
      userBindingGroupDTO.setControlled(true);
      DataResouceVOS dataResouceVOS = new DataResouceVOS();
      dataResouceVOS.setResourceCode(groupId);
      dataResouceVOS.setResourceName(groupName);
      dataResouceVOS.setResourceType(null);
      ArrayList<DataResouceVOS> arrayList = new ArrayList<>();
      arrayList.add(dataResouceVOS);
      userBindingGroupDTO.setDataResouceVOS(arrayList);
      log.info("用户绑定数据分组，权限设置userBindingGroupDTO==={},url==={}", userBindingGroupDTO, url);
      HttpEntity<UserBindingGroupDTO> httpEntity = new HttpEntity<>(userBindingGroupDTO, headers);
      ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
      apiResponse = new ApiResponse(Objects.requireNonNull(response.getBody()).toString(), null, ResultCode.SUCCESS.code());
      return apiResponse;
    } catch (Exception e) {
      e.printStackTrace();
      apiResponse = new ApiResponse(e.getMessage(), "error", ResultCode.FAIL.code());
      return apiResponse;
    }
  }


  /**
   * 关键字查询数据分组
   *
   * @param groupName 分组名称关键字
   * @return
   */
  @Override
  public ApiResponse getDataGroup(String groupName) throws ExecutionException {
    HttpHeaders headers = getHeaders();
    ApiResponse apiResponse = null;
    try {
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.GET_DATA_GROUP;
      url = url.replace("groupName", groupName);
      HttpEntity<UserBindingGroupDTO> httpEntity = new HttpEntity<>(headers);
      ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
      JSONObject data = Objects.requireNonNull(response.getBody()).getJSONObject("data");
      Integer total = data.getInteger("total");
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("total", total);
      if (total != 0) {
        Integer id = (Integer) ((HashMap) (data.getJSONArray("data").get(0))).get("id");
        jsonObject.put("id", id);
      }
      log.info("关键字查询数据分组，groupName==={},jsonObject==={},url==={}", groupName, jsonObject, url);
      apiResponse = new ApiResponse(Objects.requireNonNull(response.getBody()).toString(), jsonObject, ResultCode.SUCCESS.code());
      return apiResponse;
    } catch (Exception e) {
      e.printStackTrace();
      apiResponse = new ApiResponse(e.getMessage(), "error", ResultCode.FAIL.code());
      return apiResponse;
    }
  }

  @Override
  public ApiResponse getToken() {
    //header参数
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("Accept", "application/json, text/plain, */*");
    headers.add("Accept-Language", "zh-cn");
    headers.add("Cache-Control", "no-cache, no-store");
    headers.add("Pragma", "no-cache");
    ApiResponse apiResponse = null;
    try {
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.AUTH_LOGIN;
      HashMap<String, String> hashMap = new HashMap<>();
      hashMap.put("userName", "cxl");
      hashMap.put("password", "Chenxvlin.1");
      hashMap.put("clientId", "ms-content-sample");
      hashMap.put("userAgent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
      log.info("获取token==={},url==={}", hashMap, url);
      HttpEntity<HashMap<String, String>> httpEntity = new HttpEntity<>(hashMap, headers);
      ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
      String data = response.getBody().getString("ticket");
      apiResponse = new ApiResponse(Objects.requireNonNull(response.getBody()).toString(), data, ResultCode.SUCCESS.code());
      return apiResponse;
    } catch (Exception e) {
      e.printStackTrace();
      apiResponse = new ApiResponse(e.getMessage(), "error", ResultCode.FAIL.code());
      return apiResponse;
    }
  }

  /**
   * 自检表添加用户记录
   *
   * @param dto      AddPersonUserDTO
   * @param personId 人员id
   * @return
   */
  @Override
  public ApiResponse addMywlUser(AddPersonUserDTO dto, String personId) throws ExecutionException {
    //header参数
    HttpHeaders headers = getHeaders();
    ApiResponse apiResponse = null;
    try {
      addMywlUserDTO.setUserId(dto.getPhone());
      addMywlUserDTO.setUserName(dto.getName());
      addMywlUserDTO.setDescription(dto.getDescription());
      addMywlUserDTO.setRoleId(dto.getRoles());
      addMywlUserDTO.setGender(dto.getGender());
      addMywlUserDTO.setUserInternalId(personId);
      addMywlUserDTO.setBelongerId(dto.getBelongerId());
      addMywlUserDTO.setBelongerName(dto.getBelongerName());
      addMywlUserDTO.setBelongerType(dto.getBelongerType());
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.USER_ADD;
//      Map<String, Object> hashMap = BeanUtil.beanToMap(addPersonUserDTO);
      //组装
      log.info("添加到魔云用户表==={}", addMywlUserDTO);
      HttpEntity<AddMywlUserDTO> httpEntity = new HttpEntity<>(addMywlUserDTO, headers);
      ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
      HttpStatus statusCode = response.getStatusCode();
      Object res = Objects.requireNonNull(response.getBody()).getObject("data", Object.class);
      apiResponse = new ApiResponse(response.getBody().toString(), res, ResultCode.SUCCESS.code());
      return apiResponse;
    } catch (Exception e) {
      e.printStackTrace();
      String message = e.getMessage();
      apiResponse = new ApiResponse(e.getMessage(), "error", ResultCode.FAIL.code());
      return apiResponse;
    }
  }

  private HttpHeaders getHeaders() throws ExecutionException {
    // 获取value的值，如果key不存在，调用collable方法获取value值加载到key中再返回
    String token = loadingCache.get("token", new Callable<String>() {
      @Override
      public String call() {
        ApiResponse apiResponse = getToken();
        String token = apiResponse.getData().toString();
        log.info("未从缓存中获取到token，重新获取token==={}", token);
        loadingCache.put("token", token);
        return token;
      }
    });
    HttpHeaders headers = new HttpHeaders();
    if (!token.isEmpty()) {
      headers.set(CommonConstant.AUTHORIZATION, CommonConstant.ACCESS_TOKEN.replace("ticket", token));
      headers.setContentType(MediaType.APPLICATION_JSON);
      return headers;
    }
    return null;
  }


}

