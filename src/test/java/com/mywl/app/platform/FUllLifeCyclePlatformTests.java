package com.mywl.app.platform;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.*;
import com.mywl.app.platform.constant.CommonConstant;
import com.mywl.app.platform.module.dto.MessageDTO;
import com.mywl.app.platform.module.dto.MetaTagDTO;
import com.mywl.app.platform.response.ApiResponse;
import com.mywl.app.platform.service.DataUploadService;
import com.mywl.app.platform.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
class FUllLifeCyclePlatformTests {

  @Resource
  private RestTemplate restTemplate;

  @Autowired
  PersonService personService;

  @Autowired
  private DataUploadService dataUploadService;

  @Test
  void contextLoads() {
  }

  @Test
  void t0() {
    try {
      //header参数
      HttpHeaders headers = new HttpHeaders();
      headers.set(CommonConstant.AUTHORIZATION, CommonConstant.ACCESS_TOKEN);
      headers.setContentType(MediaType.APPLICATION_JSON);
      //放入body中的json参数
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("companyId", 1000);
      jsonObject.put("name", "zhaoyi");
      jsonObject.put("code", "zhaoyi");
      jsonObject.put("gender", "sys_gender/male");
      jsonObject.put("mainPosition", 2);
      jsonObject.put("status", "sys_person_status/onWork");
      jsonObject.put("createUser", "true");
      jsonObject.put("phone", "");
      jsonObject.put("email", "");
      jsonObject.put("description", "");
      jsonObject.put("idNumber", null);
      jsonObject.put("userName", "zhaoyi");
      jsonObject.put("password", "12345678");
      ArrayList<Long> arrayList = new ArrayList<Long>();
      arrayList.add(3449320350014480L);
      jsonObject.put("roles", arrayList);
      jsonObject.put("userDescription", "");
      //组装
      HttpEntity<JSONObject> httpEntity = new HttpEntity<>(jsonObject, headers);
//			ResponseEntity<JSONObject> response = restTemplate.exchange("http://124.71.186.8:8080/inter-api/organization/v1/person", HttpMethod.POST, httpEntity, JSONObject.class);
      ResponseEntity<JSONObject> response = restTemplate.postForEntity("http://124.71.186.8:8080/inter-api/organization/v1/person", httpEntity, JSONObject.class);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
    } catch (RestClientException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  void t1() {
    Consumer<HttpHeaders> headersConsumer = httpHeaders -> {
      httpHeaders.set(CommonConstant.AUTHORIZATION, CommonConstant.ACCESS_TOKEN);
      httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    };
    //放入body中的json参数
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("companyId", 1000);
    jsonObject.put("name", "zhaoyi");
    jsonObject.put("code", "zhaoyi");
    jsonObject.put("gender", "sys_gender/male");
    jsonObject.put("mainPosition", 2);
    jsonObject.put("status", "sys_person_status/onWork");
    jsonObject.put("createUser", "true");
    jsonObject.put("phone", "");
    jsonObject.put("email", "");
    jsonObject.put("description", "");
    jsonObject.put("idNumber", null);
    jsonObject.put("userName", "zhaoyi");
    jsonObject.put("password", "12345678");
    ArrayList<Long> arrayList = new ArrayList<Long>();
    arrayList.add(3449320350014480L);
    jsonObject.put("roles", arrayList);
    jsonObject.put("userDescription", "");
    WebClient webClient = WebClient.create();
    try {
      Mono<String> body = webClient.post()
          .uri(URI.create("http://124.71.186.8:8080/inter-api/organization/v1/person"))
          .headers(headersConsumer)
          .syncBody(jsonObject)
          .retrieve() // 获取响应体
//					.onStatus(HttpStatus::is4xxClientError, clientResponse -> {
//						clientResponse.
//
//
//						return Mono.error(new Cum4xxException(clientResponse.statusCode().value() + " error code"));
//					})
          .bodyToMono(String.class);//响应数据类型转换
      String block = body.block();
      System.out.println(block);
    } catch (Exception response) {

    }
  }

  @Test
  void t3() throws ExecutionException {
    personService.getDataGroup("3536390198582368");
  }

  @Test
  void t4() {
    ApiResponse apiResponse = personService.getToken();
    System.out.println(apiResponse.getData());
    System.out.println(apiResponse.getCode());
  }

  @Test
  void t5() {
    MetaTagDTO metaTagDTO = new MetaTagDTO();
    metaTagDTO.setVersion(1);
    metaTagDTO.setName("taisheng");
    dataUploadService.sendMetaData(metaTagDTO);
  }

  @Test
  void t6() {
    List<MessageDTO> messageDTOS = new ArrayList<MessageDTO>();
    MessageDTO messageDTO = new MessageDTO();
    messageDTO.setName("taisheng");
    messageDTO.setValue(String.valueOf(-1632));
    messageDTOS.add(messageDTO);
    dataUploadService.sendRtdDataBatch(messageDTOS);
  }

  public static void main(String[] args) {
    CacheLoader<String, String> loader = new CacheLoader<String, String>() {
      public String load(String key) throws Exception {
        Thread.sleep(1000);
        if ("key".equals(key)) return null;
        System.out.println(key + " is loaded from a cacheLoader!");
        return key + "'s value";
      }
    };

    RemovalListener<String, String> removalListener = new RemovalListener<String, String>() {
      public void onRemoval(RemovalNotification<String, String> removal) {
        System.out.println("[" + removal.getKey() + ":" + removal.getValue() + "] is evicted!");
      }
    };

    LoadingCache<String, String> testCache = CacheBuilder.newBuilder()
        .maximumSize(7)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .removalListener(removalListener)
        .build(loader);

    for (int i = 0; i < 10; i++) {
      String key = "key" + i;
      String value = "value" + i;
      testCache.put(key, value);
      System.out.println("[" + key + ":" + value + "] is put into cache!");
    }

    System.out.println(testCache.getIfPresent("key6"));

    try {
      System.out.println(testCache.get("key"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
//	private String getErrorMessage(
//			int rawStatusCode, String statusText, @Nullable byte[] responseBody, @Nullable Charset charset) {
//
//		String preface = rawStatusCode + " " + statusText + ": ";
//
//		if (ObjectUtils.isEmpty(responseBody)) {
//			return preface + "[no body]";
//		}
//
//		charset = (charset != null ? charset : StandardCharsets.UTF_8);
//
//		String bodyText = new String(responseBody, charset);
//		bodyText = LogFormatUtils.formatValue(bodyText, -1, true);
//
//		return preface + bodyText;
//	}
}
