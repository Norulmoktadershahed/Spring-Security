package com.shahed.SpringSecEx.service;

import com.shahed.SpringSecEx.Repository.UserRepo;
import com.shahed.SpringSecEx.dto.JwtResponse;
import com.shahed.SpringSecEx.dto.UserRequestDto;
import com.shahed.SpringSecEx.dto.UserResponseDto;
import com.shahed.SpringSecEx.model.RefreshToken;
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
    private RefreshTokenService refreshTokenService;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserService(UserRepo repo, AuthenticationManager authenticationManager, JWTService jwtService, RefreshTokenService refreshTokenService) {
        this.repo = repo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public UserResponseDto register(UserRequestDto requestDto){
        Users users = convertToEntity(requestDto);
        return convertToResponse(repo.save(users));
    }

    public JwtResponse verify(Users users) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUserName(),users.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken =
                    refreshTokenService.generateRefreshToken(users.getUserName());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(users.getUserName()))
                    .token(refreshToken.getToken())
                    .build();
        }else {
            throw new RuntimeException("invalid user request!");
        }
    }

    public Users convertToEntity(UserRequestDto requestDto){
        Users entity = new Users();
        entity.setUserName(requestDto.getUsername());
        entity.setPassword(encoder.encode(requestDto.getPassword()));
        return entity;
    }

    public UserResponseDto convertToResponse(Users users){
        return UserResponseDto.builder()
                .id(users.getId())
                .username(users.getUserName())
                .build();
    }

}
