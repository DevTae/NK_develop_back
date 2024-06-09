package com.nkedu.back.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeworkErrorCode implements ErrorCode {

    // NOT_FOUND : 404
    // CONFLICT : 409
    HOMEWORK_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 숙제 정보입니다."),
    
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
