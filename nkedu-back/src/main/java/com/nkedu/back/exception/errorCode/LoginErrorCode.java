package com.nkedu.back.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LoginErrorCode implements ErrorCode {

    // NOT_FOUND : 404
    // CONFLICT : 409
	LOGIN_FAILED(HttpStatus.NOT_FOUND, "입력한 정보와 일치하는 계정이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
