package com.mywl.app.platform.response;

public class ApiResponseUtil {

	public static <T> ApiResponse<T> getResponse(String msg, T data, Integer code) {
		return new ApiResponse<T>(msg, data, code);
	}

	public static <T> ApiResponse<T> getSuccessResponse(String msg, T data) {
		return new ApiResponse<T>(msg, data, ResultCode.SUCCESS.code());
	}

	public static <T> ApiResponse<T> getFailResponse(String msg, T data) {
		return new ApiResponse<T>(msg, data, ResultCode.FAIL.code());
	}

	public static <T> ApiResponse<T> getSuccessResponse(T data) {
		return getSuccessResponse(null,data);
	}

	public static <T> ApiResponse<T> getFailResponse(String msg, Integer code) {
		return new ApiResponse<T>(msg, null, code);
	}

	public static <T> ApiResponse<T> getSuccessResponse(String msg){
		return getSuccessResponse(msg,null);
	}

	public static <T> ApiResponse<T> getFailResponse(String msg){
		return getFailResponse(msg,ResultCode.FAIL.code());
	}

	public static <T> ApiResponse<T> getFailResponse(ResultCode resultCode){
		return getFailResponse(resultCode.message(),resultCode.code());
	}

	public static <T> ApiResponse<T> getSuccessResponse(){
		return getSuccessResponse(null);
	}

	public static Boolean isSuccess(ApiResponse apiResponse) {
		return apiResponse !=null
				&& apiResponse.getCode() !=null
				&& ResultCode.SUCCESS.code().equals(apiResponse.getCode());
	}

}
