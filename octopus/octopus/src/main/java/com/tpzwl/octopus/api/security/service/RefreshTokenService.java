package com.tpzwl.octopus.api.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tpzwl.octopus.api.security.model.EnumDeviceType;
import com.tpzwl.octopus.api.security.model.RefreshToken;
import com.tpzwl.octopus.api.security.repository.RefreshTokenRepository;
import com.tpzwl.octopus.api.security.repository.UserRepository;

@Service
public class RefreshTokenService {
  @Value("${app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId, String token) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setUser(userRepository.findById(userId).get());
    refreshToken.setToken(token);

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

//  public RefreshToken verifyExpiration(RefreshToken token) {
//    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//      refreshTokenRepository.delete(token);
//      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
//    }
//
//    return token;
//  }

  @Transactional
  public int deleteByUserIdAndDeviceType(Long userId, EnumDeviceType deviceType) {
    return refreshTokenRepository.deleteByUserAndDeviceType(userRepository.findById(userId).get(), deviceType);
  }
}
