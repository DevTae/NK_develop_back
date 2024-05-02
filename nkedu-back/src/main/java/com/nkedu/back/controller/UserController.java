package com.nkedu.back.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nkedu.back.api.UserService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
	
    private final UserService userService;

    /*
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody UserDTO userDTO) {
    	return userService.signup(userDTO) ? new ResponseEntity<>(null, HttpStatus.OK): new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    */

    /*
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }
    */
}