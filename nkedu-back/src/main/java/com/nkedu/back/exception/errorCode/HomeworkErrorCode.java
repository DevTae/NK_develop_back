package com.nkedu.back.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeworkErrorCode implements ErrorCode {

    // NOT_FOUND : 404
    // CONFLICT : 409
	DUPLICATE_HOMEWORK(HttpStatus.CONFLICT,"숙제가 중복되어 추가가 불가능합니다."),
	DUPLICATE_HOMEWORK_OF_STUDENT(HttpStatus.CONFLICT,"학생에게 숙제가 중복하여 추가할 수 없습니다."),
    HOMEWORK_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 숙제 정보입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
