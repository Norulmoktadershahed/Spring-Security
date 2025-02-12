package com.shahed.SpringSecEx.service;

import com.shahed.SpringSecEx.Repository.RefreshTokenRepository;
import com.shahed.SpringSecEx.Repository.UserRepo;
import com.shahed.SpringSecEx.model.RefreshToken;
import jdk.dynalink.Operation;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;

    private UserRepo userRepo;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepo userRepo) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepo = userRepo;
    }

    public RefreshToken generateRefreshToken(String username){
        RefreshToken refreshToken =
                RefreshToken.builder()
                        .users(userRepo.findByUserName(username))
                        .token(UUID.randomUUID().toString())
                        .expiryDate(Instant.now().plusMillis(10 * 60 * 1000))
                        .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token was expired" +
                    ".Please make a new sigh in request");
        }
        return token;
    }
}
