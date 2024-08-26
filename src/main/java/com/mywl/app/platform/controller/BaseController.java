package com.mywl.app.platform.controller;


import com.mywl.app.platform.response.ApiResponse;
import com.mywl.app.platform.response.ApiResponseUtil;

/**
 * 控制器基类
 *
 * @author andongdogn
 */
//@RestController
public abstract class BaseController {

  protected <T> ApiResponse<T> success() {
    return ApiResponseUtil.getSuccessResponse(null);
  }

  protected <T> ApiResponse<T> success(String msg) {
    return ApiResponseUtil.getSuccessResponse(msg);
  }

  protected <T> ApiResponse<T> success(String msg, T data) {
    return ApiResponseUtil.getSuccessResponse(msg, data);
  }

  protected <T> ApiResponse<T> success(T data) {
    return ApiResponseUtil.getSuccessResponse(data);
  }

  protected <T> ApiResponse<T> fail(String msg) {
    return ApiResponseUtil.getFailResponse(msg, null);
  }

  protected <T> ApiResponse<T> fail(String msg, int code) {
    return ApiResponseUtil.getFailResponse(msg, code);
  }

  protected <T> ApiResponse<T> fail(String msg, T data) {
    return ApiResponseUtil.getFailResponse(msg, data);
  }

  protected <T> ApiResponse<T> response(Integer count, String msg, T data) {
    return ApiResponseUtil.getResponse(msg, data, count);
  }


}
