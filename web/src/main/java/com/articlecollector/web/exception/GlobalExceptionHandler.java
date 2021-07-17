package com.articlecollector.web.exception;

import com.articlecollector.web.responseEntity.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ServerResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // RequestBody 参数不合法
        log.error("======MethodArgumentNotValidException 异常======");
        log.error(e.getMessage());
        return ServerResponse.badRequest(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ServerResponse> handleConstraintViolationException(ConstraintViolationException e) {
        // Query 参数不合法
        // 使用 @NotBlank 注解校验单个参数，如果传了空值，抛出此异常
        log.error("======ConstraintViolationException 异常======");
        log.error(e.getMessage()); // changeFavoriteStatus.articleId: 文章 ID 不能为空
        return ServerResponse.badRequest(e.getMessage().split(": ")[1]);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ServerResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        // Query 参数不合法
        // 使用 @NotBlank 注解校验单个参数，不传，抛出此异常
        // 使用 @NotNull 注解校验单个参数，不传或者传空值，抛出此异常
        log.error("======MissingServletRequestParameterException 异常======");
        log.error(e.getMessage()); // Required request parameter 'articleId' for method parameter type Long is not present
        return ServerResponse.badRequest(e.getMessage());
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<ServerResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        // 捕获 404 异常
        // 正常情况下 @RestControllerAdvice 只能捕获 Controller 抛出的异常
        // 在配置 spring.mvc.throw-exception-if-no-handler-found=true 之后才能捕获 NoHandlerFoundException 异常
        log.error("======not found 异常======");
        log.error(e.getMessage());
        return ServerResponse.notFound();
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ServerResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        // 捕获 405 异常
        // 一个 Controller 类中没有对应的请求方法，就会抛出此异常
        log.error("======method not allowed 异常======");
        log.error(e.getMessage());
        return ServerResponse.methodNotAllowed();
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ServerResponse> handleRuntimeException(RuntimeException e) {
        log.error("======运行时异常======");
        log.error(e.getMessage());
        return ServerResponse.badRequest(e.getMessage());
    }
}
