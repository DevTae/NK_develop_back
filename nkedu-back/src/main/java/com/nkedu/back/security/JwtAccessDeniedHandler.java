package com.nkedu.back.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException {
		// 권한 없이 접근할 때 403(FORBIDDEN) 반환
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT 인증 정보 없이 API 호출은 불가능합니다.");
	}
}

