package com.tpzwl.octopus.api.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpzwl.octopus.api.payload.request.LoginRequest;
import com.tpzwl.octopus.api.payload.request.SignupRequest;
import com.tpzwl.octopus.api.payload.response.MessageResponse;
import com.tpzwl.octopus.api.payload.response.UserInfoResponse;
import com.tpzwl.octopus.api.security.exception.TokenRefreshException;
import com.tpzwl.octopus.api.security.jwt.JwtUtils;
import com.tpzwl.octopus.api.security.model.EnumDeviceType;
import com.tpzwl.octopus.api.security.model.EnumRole;
import com.tpzwl.octopus.api.security.model.RefreshToken;
import com.tpzwl.octopus.api.security.model.Role;
import com.tpzwl.octopus.api.security.model.User;
import com.tpzwl.octopus.api.security.repository.RoleRepository;
import com.tpzwl.octopus.api.security.repository.UserRepository;
import com.tpzwl.octopus.api.security.service.RefreshTokenService;
import com.tpzwl.octopus.api.security.service.UserDetailsImpl;


//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails, loginRequest.getDeviceType());

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());
    
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), jwtCookie.getValue());
    
    return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
              .body(new UserInfoResponse(userDetails.getId(),
                                         userDetails.getUsername(),
                                         userDetails.getEmail(),
                                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(EnumRole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser(HttpServletRequest request) {
    Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principle.toString() != "anonymousUser") {      
      Long userId = ((UserDetailsImpl) principle).getId();
      String jwt = jwtUtils.getJwtFromCookies(request);
      EnumDeviceType deviceType = jwtUtils.getDeviceTypeFromJwtToken(jwt);
      refreshTokenService.deleteByUserIdAndDeviceType(userId, deviceType);
    }
    
    ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }

//  @PostMapping("/refreshtoken")
//  public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
//    String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
//    
//    if ((refreshToken != null) && (refreshToken.length() > 0)) {
//      return refreshTokenService.findByToken(refreshToken)
//          .map(refreshTokenService::verifyExpiration)
//          .map(RefreshToken::getUser)
//          .map(user -> {
//            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
//            
//            return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                .body(new MessageResponse("Token is refreshed successfully!"));
//          })
//          .orElseThrow(() -> new TokenRefreshException(refreshToken,
//              "Refresh token is not in database!"));
//    }
//    
//    return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
//  }
}
