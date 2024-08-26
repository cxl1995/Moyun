package com.mywl.app.platform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mywl.app.platform.constant.CommonConstant;
import com.mywl.app.platform.module.dto.interapi.instance.AddInstanceDTO;
import com.mywl.app.platform.module.dto.interapi.instance.AssignInstanceDTO;
import com.mywl.app.platform.response.ApiResponse;
import com.mywl.app.platform.response.ResultCode;
import com.mywl.app.platform.service.InstanceService;
import com.mywl.app.platform.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName InstanceServiceImpl
 * @Description TODO
 * @Author cxl
 * @Date 2023/7/5 下午2:30
 * @Version 1.0
 */
@Slf4j
@Service
public class InstanceServiceImpl implements InstanceService {

  @Autowired
  private InstanceService instanceService;

  @Autowired
  private PersonService personService;

  @Autowired
  AddInstanceDTO addInstanceDTO;

  @Autowired
  AssignInstanceDTO assignInstanceDTO;

  @Resource
  private RestTemplate restTemplate;

  //创建guava cache
  static Cache<String, String> loadingCache = CacheBuilder.newBuilder()
//      cache的初始容量
      .initialCapacity(5)
      //cache最大缓存数
      .maximumSize(10)
      //设置写缓存后n秒钟过期
      .expireAfterWrite(3, TimeUnit.DAYS)
      //设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite
      //.expireAfterAccess(17, TimeUnit.SECONDS)
      .build();


  @Override
  public ApiResponse add(AddInstanceDTO dto) throws ExecutionException {
    //header参数
    HttpHeaders headers = getHeaders();
    headers.add("x-app", "App_7f011625b3c9b4f9169b4b3d696c5619");
    headers.add("x-namespace", "mywl_equipment");
    ApiResponse apiResponse = null;
    try {
      //组装
      addInstanceDTO.setDisplayName(dto.getDisplayName());
      addInstanceDTO.setEnName(dto.getEnName());
      addInstanceDTO.setParentId(11208);
      addInstanceDTO.setAppAccessMode("PUBLIC");
      addInstanceDTO.setAppName("App_7f011625b3c9b4f9169b4b3d696c5619");
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.ADD_INSTANCE;
      log.info("添加对象实例==={}", addInstanceDTO);
      HttpEntity<AddInstanceDTO> httpEntity = new HttpEntity<>(addInstanceDTO, headers);
      ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
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

  /**
   * 给数据分组配置对象实例
   *
   * @param list 对象实例id
   * @return ApiResponse
   */
  @Override
  public ApiResponse AssignTemplateInstances(List<String> list, String groupId) throws ExecutionException {
    //header参数
    HttpHeaders headers = getHeaders();
    ApiResponse apiResponse = null;
    try {
      String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.ASSIGN_INSTANCES;
      url = url.replace("groupId", groupId);
      url = url.replace("templateId", "11208");
      assignInstanceDTO.setInstanceIds(list);
      log.info("给数据分组配置对象实例userBindingGroupDTO==={},url==={}", assignInstanceDTO, url);
      HttpEntity<AssignInstanceDTO> httpEntity = new HttpEntity<>(assignInstanceDTO, headers);
      ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
      apiResponse = new ApiResponse(Objects.requireNonNull(response.getBody()).toString(), null, ResultCode.SUCCESS.code());
      return apiResponse;
    } catch (Exception e) {
      e.printStackTrace();
      apiResponse = new ApiResponse(e.getMessage(), "error", ResultCode.FAIL.code());
      return apiResponse;
    }
  }

  private HttpHeaders getHeaders() throws ExecutionException {
    // 获取value的值，如果key不存在，调用collable方法获取value值加载到key中再返回
    String token = loadingCache.get("token", new Callable<String>() {
      @Override
      public String call() {
        ApiResponse apiResponse = personService.getToken();
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

