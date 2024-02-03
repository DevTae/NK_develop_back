package com.nkedu.back.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf(CsrfConfigurer::disable)
				.exceptionHandling(authenticationManager -> authenticationManager
						.authenticationEntryPoint(jwtAuthenticationEntryPoint)
						.accessDeniedHandler(jwtAccessDeniedHandler))

				.sessionManagement(configurer -> configurer
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// 모든 HttpServletRequest 에 접근 제한을 걸어둠
				.authorizeHttpRequests(authorize -> authorize
						// 회원가입
						.requestMatchers("/api/parent/signup").permitAll()
						.requestMatchers("/api/student/signup").permitAll()
						.requestMatchers("/api/teacher/signup").permitAll()
						
						// 로그인
						.requestMatchers("/api/login").permitAll()

						// favicon.ico 파일
						.requestMatchers("/favicon.ico").permitAll()
						
						// 그 외 인증 없이 접근 불가능						
						.anyRequest().authenticated() 
				)
				
				.addFilterBefore(new JwtFilter(tokenProvider),
						UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}
}

