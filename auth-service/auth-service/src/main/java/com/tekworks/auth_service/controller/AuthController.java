package com.tekworks.auth_service.controller;


import com.tekworks.auth_service.model.LoginRequest;
import com.tekworks.auth_service.model.UserCerdencialRequest;
import com.tekworks.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/saveUser")
    public ResponseEntity<Integer> saveUser(@RequestBody UserCerdencialRequest userCerdencialRequest) {

        int userId = userService.saveUser(userCerdencialRequest);
        return ResponseEntity.ok(userId);

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        String token = userService.login(loginRequest);
        return ResponseEntity.ok(token);
    }
}
