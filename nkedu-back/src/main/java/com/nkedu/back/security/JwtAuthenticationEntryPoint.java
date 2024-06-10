package com.nkedu.back.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.nkedu.back.exception.errorCode.AuthErrorCode;
import com.nkedu.back.exception.exception.CustomException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request,
						 HttpServletResponse response,
						 AuthenticationException authException) throws IOException {
		// 유효한 자격증명을 제공하지 않고 접근하려 할 때, 401 (unauthorized) 반환
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 인증에 실패하였습니다.");
	}
}

