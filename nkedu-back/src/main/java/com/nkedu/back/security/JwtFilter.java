package com.nkedu.back.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	public static final String AUTHORIZATION_HEADER = "Authorization";
	private final TokenProvider tokenProvider;

	// 토큰의 인증 정보를 SecurityContext 에 저장
	@Override
	public void doFilter(ServletRequest servletRequest,
						 ServletResponse servletResponse,
						 FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		String jwt = resolveToken(httpServletRequest);
		String requestURI = httpServletRequest.getRequestURI();

		if (StringUtils.hasText(jwt) && tokenProvider.validateAccessToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication); // 저장
			logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
		} else {
			logger.debug("유효한 JWT 토큰이 없습니다. uri: {}", requestURI);
			
	        httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT 토큰이 유효하지 않습니다.");
	        return;
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	// Request Header 에서 토큰 정보를 꺼내오는 메소드
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}

