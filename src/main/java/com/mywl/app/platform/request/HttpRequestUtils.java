package com.mywl.app.platform.request;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mywl.app.platform.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * http请求工具类
 */
@Slf4j
public class HttpRequestUtils {

  public static String post(String url, String param) {
    return post(url, param, null);
  }

  /**
   * 发起POST请求
   *
   * @param url   请求url
   * @param param 请求参数(JSON格式)
   * @param sign  签名
   * @return 请求回参
   */
  public static String post(String url, String param, String sign) {

    String body = null;
    // 创建默认的httpClient实例.
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // 创建http post
    HttpPost httppost = new HttpPost(url);
    //TODO 注意：sign放在请求头里
    httppost.addHeader("sign", sign);

    //参数设置
    RequestConfig requestConfig = RequestConfig.custom()
        .setConnectTimeout(10000)
        .setSocketTimeout(10000).build();
    httppost.setConfig(requestConfig);
    try {
      //TODO 注意：请求格式为JSON,请求内容以UTF-8编码
      StringEntity uefEntity = new StringEntity(param, "UTF-8");
      uefEntity.setContentType("application/json");
      httppost.setEntity(uefEntity);
      CloseableHttpResponse response = httpclient.execute(httppost);
      try {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          body = EntityUtils.toString(entity, "UTF-8");
//					System.out.println("--------------------------------------");
//					System.out.println("Response content: " + body);
//					System.out.println("--------------------------------------");
        }
      } finally {
        response.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // 关闭连接,释放资源
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return body;
  }

  /**
   * @param url    请求地址
   * @param params 请求参数
   * @return String
   * @throws Exception
   */
  public static JSONObject postData(String url, Header[] headers, Map<String, Object> params) throws Exception {
    //创建post请求对象
    HttpPost httppost = new HttpPost(url);
    // 获取到httpclient客户端
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // 请求结果
    JSONObject result = null;
    try {
      // 设置请求的一些配置设置，主要设置请求超时，连接超时等参数
      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(200000).setConnectionRequestTimeout(200000).setSocketTimeout(200000)
          .build();
      httppost.setConfig(requestConfig);
      httppost.setHeaders(headers);
      //添加参数
      String jsonString = JSONObject.toJSONString(params);
      httppost.setEntity(new StringEntity(jsonString, ContentType.create("application/json", "utf-8")));
      //启动执行请求，并获得返回值
      CloseableHttpResponse response = httpclient.execute(httppost);
      HashMap<Object, Object> map = new HashMap<>();
      map.put("message", response.getStatusLine());
      map.put("status", response.getStatusLine().getStatusCode());
      result = JSON.parseObject(JSON.toJSONString(map));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      httpclient.close();
    }
    return result;
  }

  /**
   * @param url     请求地址
   * @param headers 请求参数
   * @return String
   * @throws Exception
   */
  public static JSONObject postData(String url, Header[] headers) throws Exception {
    //创建post请求对象
    HttpPost httppost = new HttpPost(url);
    // 获取到httpclient客户端
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      // 设置请求的一些配置设置，主要设置请求超时，连接超时等参数
      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(200000).setConnectionRequestTimeout(200000).setSocketTimeout(200000)
          .build();
      httppost.setConfig(requestConfig);
      httppost.setHeaders(headers);
      //添加参数
      httppost.setEntity(new StringEntity("", ContentType.create("application/json", "utf-8")));
      // 请求结果
      String resultString = "";
      //启动执行请求，并获得返回值
      CloseableHttpResponse response = httpclient.execute(httppost);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        // 获取请求响应结果
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          // 将响应内容转换为指定编码的字符串
          resultString = EntityUtils.toString(entity, "UTF-8");
//          log.info("Response content:{}", resultString);
          JSONObject jsonObject = JSON.parseObject(resultString);
          return jsonObject;
        }
      } else {
        System.out.println("请求失败！url=" + url);
        JSONObject jsonObject = JSON.parseObject(resultString);
        return jsonObject;
      }
    } finally {
      httpclient.close();
    }
    return null;
  }

  /**
   * @param url    请求地址
   * @param params 请求参数
   * @return String
   * @throws Exception
   */
  public static JSONObject getData(String url, Header[] headers, Map<String, Object> params) throws Exception {
    // 获取到httpclient客户端
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      URIBuilder uriBuilder = new URIBuilder(url);//你的host
      params.forEach((key, value) -> {
        uriBuilder.setParameter(key, (String) value);
      });
//      uriBuilder.setParameter("pageNo", "1");
//      uriBuilder.setParameter("pageSize", "100");
//      uriBuilder.setParameter("selectFields", "name,code");
      //创建post请求对象
      HttpGet httpGet = new HttpGet(uriBuilder.build());
      // 设置请求的一些配置设置，主要设置请求超时，连接超时等参数
      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(200000).setConnectionRequestTimeout(200000).setSocketTimeout(200000)
          .build();
      httpGet.setConfig(requestConfig);
      httpGet.setHeaders(headers);
      // 请求结果
      String resultString = "";
      //启动执行请求，并获得返回值
      CloseableHttpResponse response = httpclient.execute(httpGet);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        // 获取请求响应结果
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          // 将响应内容转换为指定编码的字符串
          resultString = EntityUtils.toString(entity, "UTF-8");
//          log.info("Response content:{}", resultString);
          JSONObject jsonObject = JSON.parseObject(resultString);
          return jsonObject;
        }
      } else {
        System.out.println("请求失败！url=" + url);
        JSONObject jsonObject = JSON.parseObject(resultString);
        return jsonObject;
      }
    }
    return null;
  }

  public static JSONObject getData(String url, Header[] headers) throws IOException {

    //创建post请求对象
    HttpGet httpGet = new HttpGet(url);
    // 获取到httpclient客户端
    // 设置请求的一些配置设置，主要设置请求超时，连接超时等参数
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(200000).setConnectionRequestTimeout(200000).setSocketTimeout(200000)
          .build();
      httpGet.setConfig(requestConfig);
      httpGet.setHeaders(headers);
      // 请求结果
      String resultString = "";
      //启动执行请求，并获得返回值
      CloseableHttpResponse response = httpclient.execute(httpGet);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        // 获取请求响应结果
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          // 将响应内容转换为指定编码的字符串
          resultString = EntityUtils.toString(entity, "UTF-8");
//          log.info("Response content:{}", resultString);
          JSONObject jsonObject = JSON.parseObject(resultString);
          return jsonObject;
        }
      } else {
        System.out.println("请求失败！url=" + url);
        JSONObject jsonObject = JSON.parseObject(resultString);
        return jsonObject;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 修改数据
   *
   * @param url     请求地址
   * @param headers 请求头
   * @param params  请求参数
   * @return 修改结果
   * @throws Exception
   */
  public static JSONObject patchData(String url, Header[] headers, Map<String, Object> params) throws Exception {
    //创建post请求对象
    HttpPatch httpPatch = new HttpPatch(url);
    // 获取到httpclient客户端
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      // 设置请求的一些配置设置，主要设置请求超时，连接超时等参数
      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(200000).setConnectionRequestTimeout(200000).setSocketTimeout(200000)
          .build();
      httpPatch.setConfig(requestConfig);
      httpPatch.setHeaders(headers);
      //添加参数
      httpPatch.setEntity(new StringEntity(JSONObject.toJSONString(params), ContentType.create("application/json", "utf-8")));
      // 请求结果
      String resultString = "";
      //启动执行请求，并获得返回值
      CloseableHttpResponse response = httpclient.execute(httpPatch);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        // 获取请求响应结果
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          // 将响应内容转换为指定编码的字符串
          resultString = EntityUtils.toString(entity, "UTF-8");
//          log.info("Response content:{}", resultString);
          JSONObject jsonObject = JSON.parseObject(resultString);
          return jsonObject;
        }
      } else {
        System.out.println("请求失败！url=" + url);
        JSONObject jsonObject = JSON.parseObject(resultString);
        return jsonObject;
      }
    } finally {
      httpclient.close();
    }
    return null;
  }

  /**
   * 修改数据
   *
   * @param url     请求地址
   * @param headers 请求头
   * @return 修改结果
   * @throws Exception
   */
  public static JSONObject deleteData(String url, Header[] headers) throws Exception {
    //创建post请求对象
    HttpDelete httpDelete = new HttpDelete(url);
    // 获取到httpclient客户端
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      // 设置请求的一些配置设置，主要设置请求超时，连接超时等参数
      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(200000).setConnectionRequestTimeout(200000).setSocketTimeout(200000)
          .build();
      httpDelete.setConfig(requestConfig);
      httpDelete.setHeaders(headers);
      // 请求结果
      String resultString = "";
      //启动执行请求，并获得返回值
      CloseableHttpResponse response = httpclient.execute(httpDelete);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        // 获取请求响应结果
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          // 将响应内容转换为指定编码的字符串
          resultString = EntityUtils.toString(entity, "UTF-8");
          JSONObject jsonObject = JSON.parseObject(resultString);
          return jsonObject;
        }
      } else {
        System.out.println("请求失败！url=" + url);
        JSONObject jsonObject = JSON.parseObject(resultString);
        return jsonObject;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      httpclient.close();
    }
    return null;
  }

  public static String sendPost(String url, String param) {
    PrintWriter out = null;
    BufferedReader in = null;
    String result = "";
    try {
      URL realUrl = new URL(url);
      // 打开和URL之间的连接
      HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
      // 设置通用的请求属性
      conn.setRequestProperty("User-Agent", "Mozilla/5.0");
      conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      conn.setRequestProperty("Content-type", "application/json");
      conn.setRequestProperty("Authorization", "Bearer " + "f0e14fdb-2c3a-43f3-a982-34a12497d618");
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

      conn.setRequestMethod("POST");//默认get

      // 发送POST请求必须设置如下两行
      conn.setDoOutput(true);
      conn.setDoInput(true);
      //post请求不能使用缓存
      conn.setUseCaches(false);

      // 获取URLConnection对象对应的输出流
      out = new PrintWriter(conn.getOutputStream());
      // 发送请求参数
      out.print(param);
      // flush输出流的缓冲
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应
      in = new BufferedReader(
          new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception e) {
      log.info("发送 POST 请求出现异常！" + e);
      e.printStackTrace();
    }
    //使用finally块来关闭输出流、输入流
    finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return result;
  }


  public static void main(String[] args) {
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
    String jsonString = jsonObject.toJSONString();
    System.out.println(jsonString);
    String url = CommonConstant.SUPOS_ADDRESS + CommonConstant.PERSON_ADD;
    String s = sendPost(url, jsonString);
    System.out.println(s);
  }
}