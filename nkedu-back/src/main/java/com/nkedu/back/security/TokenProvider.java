package com.nkedu.back.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.nkedu.back.exception.errorCode.AuthErrorCode;
import com.nkedu.back.exception.exception.CustomException;
import com.nkedu.back.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * JWT 시스템 구현을 위한 TokenProvider 클래스 구현 코드입니다.
 * application.yml 에서의 jwt 옵션을 바탕으로 디테일한 옵션 수정이 가능합니다.
 * Refresh Token 과 Access Token 을 제공하며, 장기간의 Refresh Token 을 바탕으로 Access Token 을 발행하도록 구현하였습니다.
 * 
 * @author devtae
 */

@Component
public class TokenProvider implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
	private final UserDetailsService userDetailsService;
	
	private static final String AUTHORITES_KEY = "auth";
	private static final String REFRESH_TOKEN_KEY = "refresh";
	private static final String ACCESS_TOKEN_KEY = "access";
	private final String secret;
	private final long refreshTokenValidityInMilliseconds;
	private final long accessTokenValidityInMilliseconds;
	private Key key;

	public TokenProvider(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
			@Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds,
			UserDetailsService userDetailsService) {
		this.secret = secret;
		this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
		this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}
	
	// refresh token 생성
	public String createRefreshToken(Authentication authentication) {
			
		long now = (new Date()).getTime();
		Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);
			
		return Jwts.builder()
				.setSubject(authentication.getName())
				.claim(REFRESH_TOKEN_KEY, true)
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.compact();
	}

	// access token 생성
	public String createAccessToken(String refreshToken) {
		Claims claims = Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(refreshToken)
				.getBody();
		
		// refresh token 이 아닐 경우, access token 반환 실패
		if(!claims.containsKey(REFRESH_TOKEN_KEY)) {
			System.out.println("refreshtokenkey not find");
			return null;
		}
		System.out.println("refreshtokenkey find");
		
		String authorities = userDetailsService.loadUserByUsername(claims.getSubject()).getAuthorities()
								.stream().map(GrantedAuthority::getAuthority)
								.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.accessTokenValidityInMilliseconds);

		return Jwts.builder()
				.setSubject(claims.getSubject())
				.claim(ACCESS_TOKEN_KEY, true)
				.claim(AUTHORITES_KEY, authorities)
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.compact();
	}

	// 생성한 Access JWT 토큰을 바탕으로 Authentication 객체 생성 진행
	public Authentication getAuthentication(String accessToken) {
		// 생성된 token body 내용에 대한 parse 진행
		Claims claims = Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(accessToken)
				.getBody();

		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITES_KEY).toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
	}

	// JwtFilter 에서 사용하는 함수 (Access Token 검증 역할)
	public boolean validateAccessToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			
			if (claims.containsKey(ACCESS_TOKEN_KEY)) {
				return true;
			}
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			logger.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			logger.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			logger.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			logger.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}
}

