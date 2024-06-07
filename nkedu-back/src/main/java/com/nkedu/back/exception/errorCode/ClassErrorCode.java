package com.nkedu.back.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClassErrorCode implements ErrorCode {

    DUPLICATE_CLASSROOM(HttpStatus.CONFLICT,"이미 등록된 수업 입니다."),
    TEACHING_TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 티칭 선생님입니다."),
    ASSISTANT_TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 조교 선생님입니다."),
    CLASSROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"수업이 존재하지 않습니다."),

    CLASSROOM_NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND,"수업 공지가 존재하지 않습니다."),
    ADMIN_NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND,"관리자 공지가 존재하지 않습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}

