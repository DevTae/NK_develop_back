package com.nkedu.back.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nkedu.back.dto.LoginDto;
import com.nkedu.back.dto.TokenDto;
import com.nkedu.back.security.JwtFilter;
import com.nkedu.back.security.TokenProvider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
	
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
    	
    	System.out.println(loginDto.toString());
    	
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        System.out.println("authenticationToken: " + authenticationToken.toString());
        
        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
        Authentication authentication = null;
        try {
        	authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);        	
        } catch (UsernameNotFoundException e) {
        	System.out.println(e.getMessage());
        }
        
        System.out.println("authentication: " + authentication.toString());

        // 해당 객체를 SecurityContextHolder에 저장하고
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
        String jwt = tokenProvider.createToken(authentication);
        if (jwt == null) {
            System.out.println("authentication은 null입니다.");
        } else {
            System.out.println("authentication은 null이 아닙니다.");
        }

        /*
        HttpHeaders httpHeaders = new HttpHeaders();
        // response header에 jwt token에 넣어줌
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        */

        // tokenDto를 이용해 response body에도 넣어서 리턴
        //return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
        return new ResponseEntity<>(new TokenDto(jwt), HttpStatus.OK);
    }
    
    @GetMapping("/login_check")
    @PreAuthorize("ROLE_USER")
    public ResponseEntity<Void> login_check() {
    	return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
}
