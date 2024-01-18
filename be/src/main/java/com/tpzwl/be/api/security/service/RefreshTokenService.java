package com.tpzwl.be.api.security.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tpzwl.be.api.model.User;
import com.tpzwl.be.api.security.exception.TokenRefreshException;
import com.tpzwl.be.api.security.model.EnumDeviceType;
import com.tpzwl.be.api.security.model.RefreshToken;
import com.tpzwl.be.api.security.repository.RefreshTokenRepository;
import com.tpzwl.be.api.security.repository.UserRepository;

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

	@Transactional
	public RefreshToken createRefreshToken(Long userId, EnumDeviceType deviceType) {
		User user = userRepository.findById(userId).get();
		RefreshToken refreshToken = refreshTokenRepository.findByUserAndDeviceType(user, deviceType);
		
		if (refreshToken != null) {
			refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
			refreshToken.setToken(UUID.randomUUID().toString());			
		} else {
			refreshToken = new RefreshToken();
			refreshToken.setUser(user);
			refreshToken.setDeviceType(deviceType);
			refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
			refreshToken.setToken(UUID.randomUUID().toString());
		}
		
		return refreshTokenRepository.save(refreshToken);
	}

	@Transactional
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(),
					"Refresh token was expired. Please make a new signin request");
		}

		return token;
	}

	@Transactional
	public int deleteByUserIdAndDeviceType(Long userId, EnumDeviceType deviceType) {
		return refreshTokenRepository.deleteByUserAndDeviceType(userRepository.findById(userId).get(), deviceType);
	}
}
