package com.articlecollector.web.responseEntity;

import lombok.Getter;

@Getter
public enum ResultEnum {
    OK(true, 200, "成功"),
    CREATED(true, 201, "成功"),
    NO_CONTENT(true, 204, "成功"),
    BAD_REQUEST(false, 400, "参数错误"),
    UNAUTHORIZED(false, 401, "未授权，请登录"),
    NOT_FOUND(false, 404, "找不到请求的资源"),
    METHOD_NOT_ALLOWED(false, 405, "方法不允许"),
    INTERNAL_SERVER_ERROR(false, 500, "服务器内部错误"),

    UNKNOWN_ERROR(false,20001,"未知错误"),
    ILLEGAL_ARGUMENT(false, 20002, "参数非法"),
    NULL_POINT(false,20003,"空指针异常"),
    HTTP_CLIENT_ERROR(false,20004,"客户端连接异常");

    private Boolean success;
    private Integer code;
    private String message;

    // 枚举类型的构造方法默认就是 private
    ResultEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
