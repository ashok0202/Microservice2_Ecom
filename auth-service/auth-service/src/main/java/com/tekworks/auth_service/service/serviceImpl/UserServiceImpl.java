package com.tekworks.auth_service.service.serviceImpl;


import com.tekworks.auth_service.entity.UserCerdencials;
import com.tekworks.auth_service.model.LoginRequest;
import com.tekworks.auth_service.model.UserCerdencialRequest;
import com.tekworks.auth_service.repository.UserCerdencialsRepository;
import com.tekworks.auth_service.service.JWTService;
import com.tekworks.auth_service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserCerdencialsRepository userCerdencialsRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JWTService jwtService ;


    @Override
    public int saveUser(UserCerdencialRequest userCerdencialRequest) {

        UserCerdencials userCerdencials = modelMapper.map(userCerdencialRequest, UserCerdencials.class);
        userCerdencials.setPassword(passwordEncoder.encode(userCerdencials.getPassword()));
        userCerdencials = userCerdencialsRepo.save(userCerdencials);
        return userCerdencials.getUserId();

    }

    @Override
    public String login(LoginRequest loginRequest) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (authentication.isAuthenticated()) {

            return jwtService.generateToken(loginRequest.getUsername());
        }
        return "Login Failed";
    }
}
