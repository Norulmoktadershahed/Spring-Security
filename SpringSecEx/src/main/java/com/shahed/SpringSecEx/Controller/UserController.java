package com.shahed.SpringSecEx.Controller;

import com.shahed.SpringSecEx.dto.JwtResponse;
import com.shahed.SpringSecEx.dto.RefreshTokenRequestDto;
import com.shahed.SpringSecEx.dto.UserRequestDto;
import com.shahed.SpringSecEx.dto.UserResponseDto;
import com.shahed.SpringSecEx.model.RefreshToken;
import com.shahed.SpringSecEx.model.Users;
import com.shahed.SpringSecEx.service.JWTService;
import com.shahed.SpringSecEx.service.RefreshTokenService;
import com.shahed.SpringSecEx.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;
    private RefreshTokenService refreshTokenService;
    private JWTService jwtService;

    public UserController(UserService userService, RefreshTokenService refreshTokenService, JWTService jwtService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRequestDto userRequestDto) {
        return userService.register(userRequestDto);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody Users users) {
        return userService.verify(users);
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequestDto tokenRequestDto) {
        return refreshTokenService.findByToken(tokenRequestDto.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsers)
                .map(users -> {
            String accessToken = jwtService.generateToken(users.getUserName());
            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .token(tokenRequestDto.getToken())
                    .build();
        }).orElseThrow(() -> new RuntimeException(
                "Refresh token is not in database"));
    }
}

