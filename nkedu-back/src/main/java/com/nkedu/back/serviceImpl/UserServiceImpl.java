package com.nkedu.back.serviceImpl;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nkedu.back.api.UserService;
import com.nkedu.back.model.Authority;
import com.nkedu.back.model.User;
import com.nkedu.back.model.UserDto;
import com.nkedu.back.repository.UserRepository;
import com.nkedu.back.security.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public boolean signup(UserDto userDto) {
        try{

	        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
	            throw new RuntimeException("�̹� ���ԵǾ� �ִ� �����Դϴ�.");
	        }
	
	        System.out.println(userDto.getUsername());
	        // ���ԵǾ� ���� ���� ȸ���̸�,
	        // ���� ���� �����
	        Authority authority = Authority.builder()
	                .authorityName("ROLE_USER")
	                .build();
	
	        // ���� ������ ���� save
	        User user = User.builder()
	                .username(userDto.getUsername())
	                .password(passwordEncoder.encode(userDto.getPassword()))
	                .nickname(userDto.getNickname())
	                .authorities(Collections.singleton(authority))
	                .activated(true)
	                .build();
	
	        userRepository.save(user);
	        return true;
        } catch(Exception e) {
        	log.error("Failed: " + e.getMessage(),e);
        }
        return false;
    
    }

    // ����,���� ������ �������� �޼ҵ�
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    // ���� securityContext�� ����� username�� ������ �������� �޼ҵ�
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}