package com.nkedu.back.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // NOT_FOUND : 404
    // CONFLICT : 409
    DUPLICATE_USERNAME(HttpStatus.CONFLICT,"이미 등록된 username 입니다."),
    DUPLICATE_SCHOOL(HttpStatus.CONFLICT,"이미 등록된 school 입니다."),
    SCHOOL_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 학교 이름입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Internal server error");
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
