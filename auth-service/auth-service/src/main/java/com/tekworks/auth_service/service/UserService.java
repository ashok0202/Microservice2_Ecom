package com.tekworks.auth_service.service;

import com.tekworks.auth_service.model.LoginRequest;
import com.tekworks.auth_service.model.UserCerdencialRequest;

public interface UserService {


    int saveUser(UserCerdencialRequest userCerdencialRequest);

    String login(LoginRequest loginRequest);
}
