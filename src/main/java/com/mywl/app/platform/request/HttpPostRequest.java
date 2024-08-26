package com.mywl.app.platform.request;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName HttpPostRequest
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/8 下午1:33
 * @Version 1.0
 */
public class HttpPostRequest {
  public static String sendPostRequest(String url, String postData) {
    StringBuffer response = null;
    try {
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      // 设置请求方法为POST
      con.setRequestMethod("POST");

      // 添加请求头
      con.setRequestProperty("User-Agent", "Mozilla/5.0");
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      con.setRequestProperty("Content-type", "application/json");
      con.setRequestProperty("Authorization", "Bearer " + "f0e14fdb-2c3a-43f3-a982-34a12497d618");

      // 发送POST数据
      con.setDoOutput(true);
      OutputStream os = con.getOutputStream();
      os.write(postData.getBytes(StandardCharsets.UTF_8));
      os.flush();
      os.close();

      // 获取响应结果
      int responseCode = con.getResponseCode();
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
      int count = 0;
      while (e != null) {
        Throwable cause = e.getCause();
        if (cause == null) {
          System.out.println(count);
          System.out.println(e.getClass());
        }
        count++;
        e = (Exception) cause;
        System.out.println(e);
      }
    }
    return response.toString();
  }
}

