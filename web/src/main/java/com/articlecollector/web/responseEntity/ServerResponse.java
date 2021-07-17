package com.articlecollector.web.responseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerResponse {
    private Boolean success;
    private Integer code;
    private String message;
    private Object data;

    /**
     * 200 OK
     * @param params 传给前端的参数
     * @return ResponseEntity<ServerResponse>
     */
    public static ResponseEntity<ServerResponse> ok(Object params) {
        return new ResponseEntity<>(new ServerResponse(
                ResultEnum.OK.getSuccess(),
                ResultEnum.OK.getCode(),
                ResultEnum.OK.getMessage(), // 成功展示默认提示信息
                params // 传给前端的参数
        ), HttpStatus.OK); // 使用 ResponseEntity 对象设置响应状态码
    }

    /**
     * 201 Created
     * @return ResponseEntity<ServerResponse>
     */
    public static ResponseEntity<ServerResponse> created() {
        return new ResponseEntity<>(new ServerResponse(
                ResultEnum.CREATED.getSuccess(),
                ResultEnum.CREATED.getCode(),
                ResultEnum.CREATED.getMessage(),
                null
        ), HttpStatus.CREATED);
    }

    /**
     * 204 请求成功，没有响应体
     * @return ResponseEntity<ServerResponse>
     */
    public static ResponseEntity<ServerResponse> noContent() {
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * 400 参数错误
     * @param message 自定义错误信息
     * @return ResponseEntity<ServerResponse>
     */
    public static ResponseEntity<ServerResponse> badRequest(@Nullable String message) {
        return new ResponseEntity<>(new ServerResponse(
                ResultEnum.BAD_REQUEST.getSuccess(),
                ResultEnum.BAD_REQUEST.getCode(),
                message != null ? message : ResultEnum.BAD_REQUEST.getMessage(), // 校验失败传入指定的提示信息
                null // 校验失败不返回参数
        ), HttpStatus.BAD_REQUEST);
    }

    /**
     * 401 未授权，请登录
     * @return ResponseEntity<ServerResponse>
     */
    public static ResponseEntity<ServerResponse> unauthorized() {
        return new ResponseEntity<>(new ServerResponse(
                ResultEnum.UNAUTHORIZED.getSuccess(),
                ResultEnum.UNAUTHORIZED.getCode(),
                ResultEnum.UNAUTHORIZED.getMessage(),
                null
        ), HttpStatus.UNAUTHORIZED);
    }

    /**
     * 404 找不到请求的资源
     * @return ResponseEntity<ServerResponse>
     */
    public static ResponseEntity<ServerResponse> notFound() {
        return new ResponseEntity<>(new ServerResponse(
                ResultEnum.NOT_FOUND.getSuccess(),
                ResultEnum.NOT_FOUND.getCode(),
                ResultEnum.NOT_FOUND.getMessage(),
                null
        ), HttpStatus.NOT_FOUND);
    }

    /**
     * 405 方法不允许
     * @return ResponseEntity<ServerResponse>
     */
    public static ResponseEntity<ServerResponse> methodNotAllowed() {
        return new ResponseEntity<>(new ServerResponse(
                ResultEnum.METHOD_NOT_ALLOWED.getSuccess(),
                ResultEnum.METHOD_NOT_ALLOWED.getCode(),
                ResultEnum.METHOD_NOT_ALLOWED.getMessage(),
                null
        ), HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 服务器内部错误
     * @return ResponseEntity<ServerResponse>
     */
    public static ResponseEntity<ServerResponse> internalServerError() {
        return new ResponseEntity<>(new ServerResponse(
                ResultEnum.INTERNAL_SERVER_ERROR.getSuccess(),
                ResultEnum.INTERNAL_SERVER_ERROR.getCode(),
                ResultEnum.INTERNAL_SERVER_ERROR.getMessage(),
                null
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
