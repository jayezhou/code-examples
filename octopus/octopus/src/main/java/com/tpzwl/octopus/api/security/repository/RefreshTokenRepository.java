package com.tpzwl.octopus.api.security.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tpzwl.octopus.api.security.model.EnumDeviceType;
import com.tpzwl.octopus.api.security.model.RefreshToken;
import com.tpzwl.octopus.api.security.model.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByUserAndDeviceType(User user, EnumDeviceType deviceType);
}
