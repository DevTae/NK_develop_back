package com.nkedu.back.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // NOT_FOUND : 404
    // CONFLICT : 409
    DUPLICATE_USERNAME(HttpStatus.CONFLICT,"이미 등록된 유저 입니다."),
    DUPLICATE_SCHOOL(HttpStatus.CONFLICT,"이미 등록된 학교 입니다."),
    SCHOOL_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 학교 이름입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),
    
    GET_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보를 불러오는 데에 실패하였습니다."),
    UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보를 수정하는 데에 실패하였습니다."),
    DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보를 삭제하는 데에 실패하였습니다."),
    CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보를 생성하는 데에 실패하였습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
