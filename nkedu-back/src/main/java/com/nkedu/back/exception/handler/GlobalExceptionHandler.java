package com.nkedu.back.exception.handler;

import com.nkedu.back.exception.errorCode.CommonErrorCode;
import com.nkedu.back.exception.errorCode.ErrorCode;
import com.nkedu.back.exception.exception.CustomException;
import com.nkedu.back.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class) // ①
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("Failed: " + e.getErrorCode().getMessage(), e);
//        log.info("Failed: " + e.getErrorCode().getMessage(), e);
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler({Exception.class}) // ②
    public ResponseEntity<Object> handleIllegalArgument(Exception e) {
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Failed: " + e.getMessage(), e);
//        log.info("Failed: " + e.getMessage(), e);
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(ArithmeticException.class) // ③
    public ResponseEntity<Object> handleArithmeticException(ArithmeticException e) {
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Failed: " + e.getMessage(), e);
//        log.info("Failed: " + e.getMessage(), e);
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().value())
                .message(message)
                .build();
    }
}