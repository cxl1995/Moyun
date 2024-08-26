package com.mywl.app.platform.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ToString
@ApiModel
public class ApiResponse<T> {
    @ApiModelProperty(value = "返回消息")
    private String msg;
    @ApiModelProperty(value = "数据")
    private T data;
    @ApiModelProperty(value = "状态码")
    private Integer code;

    private String requestId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Boolean isSuccess() {
        return ResultCode.SUCCESS.code().equals(code);
    }

    public ApiResponse(String msg, T data, Integer code) {
        this.msg = msg;
        this.data = data;
        this.code = code;
    }


    public ApiResponse() {
    }
}
