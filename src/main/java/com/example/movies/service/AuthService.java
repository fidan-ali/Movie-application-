package com.example.movies.service;

import com.example.movies.constant.Role;
import com.example.movies.dao.entity.RefreshTokenEntity;
import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dao.repository.RefreshTokenRepository;
import com.example.movies.dao.repository.UserRepository;
import com.example.movies.dto.AuthResponseDto;
import com.example.movies.dto.LoginRequestDto;
import com.example.movies.dto.RefreshRequestDto;
import com.example.movies.dto.RegisterRequestDto;
import com.example.movies.dto.UserResponseDto;
import com.example.movies.exception.DuplicateResourceException;
import com.example.movies.exception.InvalidCredentialsException;
import com.example.movies.exception.InvalidRefreshTokenException;
import com.example.movies.mapper.UserMapper;
import com.example.movies.security.AuthenticatedUser;
import com.example.movies.security.JwtService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.movies.constant.Constant.EMAIL;
import static com.example.movies.constant.Constant.TOKEN_TYPE_BEARER;
import static com.example.movies.constant.Constant.USER;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private static final  UserMapper userMapper = UserMapper.INSTANCE;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${security.jwt.refresh-token-validity-days}")
  private long refreshTokenValidityDays;

  @Transactional
  public UserResponseDto register(RegisterRequestDto request) {
    UserEntity user = userMapper.toEntity(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.ROLE_USER);

    try {
      UserEntity saved = userRepository.saveAndFlush(user);
      return userMapper.toResponse(saved);
    } catch (DataIntegrityViolationException ex) {
      throw new DuplicateResourceException(USER, EMAIL, request.getEmail());
    }
  }

  @Transactional
  public AuthResponseDto login(LoginRequestDto request) {
    Authentication authentication;
    try {
      authentication = authenticationManager.authenticate(
          UsernamePasswordAuthenticationToken.unauthenticated(
              request.email(), request.password()));
    } catch (AuthenticationException ex) {
      throw new InvalidCredentialsException();
    }

    AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
    return issueTokens(principal);
  }

  @Transactional
  public AuthResponseDto refresh(RefreshRequestDto request) {
    RefreshTokenEntity stored = refreshTokenRepository
        .findByTokenForUpdate(request.refreshToken())
        .orElseThrow(InvalidRefreshTokenException::new);

    if (!stored.isUsable()) {
      throw new InvalidRefreshTokenException();
    }

    stored.setRevoked(true);

    UserEntity user = stored.getUserEntity();
    return issueTokens(new AuthenticatedUser(user.getId(), user.getEmail(), user.getRole()));
  }

  @Transactional
  public void logout(RefreshRequestDto request) {
    refreshTokenRepository.findByTokenForUpdate(request.refreshToken())
        .ifPresent(token -> token.setRevoked(true));
  }

  private AuthResponseDto issueTokens(AuthenticatedUser principal) {
    RefreshTokenEntity refreshToken = new RefreshTokenEntity();
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setUserEntity(userRepository.getReferenceById(principal.getId()));
    refreshToken.setExpiresAt(Instant.now().plus(refreshTokenValidityDays, ChronoUnit.DAYS));
    refreshTokenRepository.save(refreshToken);

    return new AuthResponseDto(
        jwtService.generateAccessToken(principal),
        refreshToken.getToken(),
        TOKEN_TYPE_BEARER);
  }

}