package com.shahed.SpringSecEx.service;

import com.shahed.SpringSecEx.Repository.UserRepo;
import com.shahed.SpringSecEx.model.Users;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepo repo;
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserService(UserRepo repo, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.repo = repo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public Users register(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public String  verify(Users users) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUserName(),users.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(users.getUserName());
        return "fail";
    }
}
