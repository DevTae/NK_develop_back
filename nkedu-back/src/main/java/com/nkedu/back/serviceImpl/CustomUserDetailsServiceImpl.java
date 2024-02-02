package com.nkedu.back.serviceImpl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nkedu.back.model.User;
import com.nkedu.back.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	
    private final UserRepository userRepository;

    @Override
    @Transactional
    // �α��νÿ� DB���� ���������� ���������� �����ͼ� �ش� ������ ������� userdetails.User ��ü�� ������ ����
    public UserDetails loadUserByUsername(final String username) {
    	System.out.println("loadUserByUsername");
    	
    	/*
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> �����ͺ��̽����� ã�� �� �����ϴ�."));
        */
    	
    	User user_tmp = userRepository.findOneWithAuthoritiesByUsername(username).get();
    	//User user_tmp = userRepository.find(username).get();
    	System.out.println("user.getAuthorities(): " + user_tmp.toString() + user_tmp.getAuthorities());
    	
    	UserDetails userDetails = userRepository.findOneWithAuthoritiesByUsername(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> �����ͺ��̽����� ã�� �� �����ϴ�."));
    	
    	System.out.println("userDetails: " + userDetails.toString());
    	
    	return userDetails;
    	
    }

    private org.springframework.security.core.userdetails.User createUser(String username, User user) {
    	
    	System.out.println("user: " + user.toString());
    	
        if (!user.isActivated()) {
            throw new RuntimeException(username + " -> Ȱ��ȭ�Ǿ� ���� �ʽ��ϴ�.");
        }

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}