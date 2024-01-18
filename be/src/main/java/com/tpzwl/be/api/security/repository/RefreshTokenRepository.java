package com.tpzwl.be.api.security.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tpzwl.be.api.model.User;
import com.tpzwl.be.api.security.model.EnumDeviceType;
import com.tpzwl.be.api.security.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
  Optional<RefreshToken> findByToken(String token);
  
  RefreshToken findByUserAndDeviceType(User user, EnumDeviceType deviceType);

  @Modifying
  int deleteByUserAndDeviceType(User user, EnumDeviceType deviceType);
}
