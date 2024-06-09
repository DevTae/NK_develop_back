package com.nkedu.back.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	/**
	 * 인증 관련 Error Code 입니다. 
	 */
	
	// UNAUTHORIZED : 401
	// FORBIDDEN : 403
	ACCESS_JWT_TIME_EXPIRED(HttpStatus.FORBIDDEN, "Access JWT 유효기간이 초과되었습니다."),
	ACCESS_JWT_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "JWT 인증 자격에 실패하였습니다."),
	REFRESH_JWT_TIME_EXPIRED(HttpStatus.FORBIDDEN, "Refresh JWT 유효기간이 초과되었습니다."),
	REFRESH_JWT_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "Refresh JWT 인증 자격에 실패하였습니다."),
	NOT_ADMIN(HttpStatus.UNAUTHORIZED, "관리자 계정이 아닙니다."),
	NOT_TEACHER(HttpStatus.UNAUTHORIZED, "선생님 계정이 아닙니다."),
	NOT_STUDENT(HttpStatus.UNAUTHORIZED, "학생 계정이 아닙니다."),
	NOT_PARENT(HttpStatus.UNAUTHORIZED, "부모님 계정이 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
