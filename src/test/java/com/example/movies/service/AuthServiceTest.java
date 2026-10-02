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
import com.example.movies.exception.DuplicateResourceException;
import com.example.movies.exception.InvalidCredentialsException;
import com.example.movies.exception.InvalidRefreshTokenException;
import com.example.movies.security.AuthenticatedUser;
import com.example.movies.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.example.movies.constant.MovieApiTestConstants.BIRTH_DATE;
import static com.example.movies.constant.MovieApiTestConstants.EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.FIRST_NAME;
import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.LAST_NAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String RAW_PASSWORD = "Password123";
    private static final String HASHED_PASSWORD = "$2a$10$hashed";
    private static final String ACCESS_TOKEN = "generated.access.token";
    private static final String OLD_REFRESH_TOKEN = "old-refresh-token";
    private static final long VALIDITY_DAYS = 7L;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "refreshTokenValidityDays", VALIDITY_DAYS);
    }

    private UserEntity user() {
        UserEntity user = new UserEntity();
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setRole(Role.ROLE_USER);
        return user;
    }

    private RegisterRequestDto registerRequest() {
        return new RegisterRequestDto(FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE, RAW_PASSWORD);
    }

    private RefreshTokenEntity storedToken(boolean revoked, Instant expiresAt) {
        RefreshTokenEntity token = new RefreshTokenEntity();
        token.setId(ID);
        token.setToken(OLD_REFRESH_TOKEN);
        token.setUserEntity(user());
        token.setRevoked(revoked);
        token.setExpiresAt(expiresAt);
        return token;
    }

    // ---------- register ----------

    @Test
    void register_shouldHashPasswordAndAssignUserRole() {
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(HASHED_PASSWORD);
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenAnswer(inv -> {
            UserEntity saved = inv.getArgument(0);
            saved.setId(ID);
            return saved;
        });

        authService.register(registerRequest());

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).saveAndFlush(captor.capture());

        assertAll(
                () -> assertEquals(HASHED_PASSWORD, captor.getValue().getPassword()),
                () -> assertNotEquals(RAW_PASSWORD, captor.getValue().getPassword()),
                () -> assertEquals(Role.ROLE_USER, captor.getValue().getRole())
        );
    }

    @Test
    void register_shouldThrowDuplicate_whenDatabaseRejectsEmail() {
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(HASHED_PASSWORD);
        when(userRepository.saveAndFlush(any(UserEntity.class)))
                .thenThrow(new DataIntegrityViolationException("unique violation"));

        assertThrows(DuplicateResourceException.class,
                () -> authService.register(registerRequest()));
    }

    // ---------- login ----------

    @Test
    void login_shouldThrowInvalidCredentials_whenAuthenticationFails() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad"));

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new LoginRequestDto(EMAIL, RAW_PASSWORD)));

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnBothTokens_whenCredentialsAreValid() {
        AuthenticatedUser principal = new AuthenticatedUser(ID, EMAIL, Role.ROLE_USER);
        Authentication authentication = new org.springframework.security.authentication
                .UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateAccessToken(principal)).thenReturn(ACCESS_TOKEN);
        when(userRepository.getReferenceById(ID)).thenReturn(user());
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        AuthResponseDto result = authService.login(new LoginRequestDto(EMAIL, RAW_PASSWORD));

        assertAll(
                () -> assertEquals(ACCESS_TOKEN, result.accessToken()),
                () -> assertFalse(result.refreshToken().isBlank()),
                () -> assertEquals("Bearer", result.tokenType())
        );
    }

    // ---------- refresh ----------

    @Test
    void refresh_shouldUseLockingQuery() {
        when(refreshTokenRepository.findByTokenForUpdate(OLD_REFRESH_TOKEN))
                .thenReturn(Optional.empty());

        assertThrows(InvalidRefreshTokenException.class,
                () -> authService.refresh(new RefreshRequestDto(OLD_REFRESH_TOKEN)));

        // kilidsiz oxuma istifadə olunmamalıdır
        verify(refreshTokenRepository).findByTokenForUpdate(OLD_REFRESH_TOKEN);
    }

    @Test
    void refresh_shouldThrow_whenTokenIsAlreadyRevoked() {
        when(refreshTokenRepository.findByTokenForUpdate(OLD_REFRESH_TOKEN))
                .thenReturn(Optional.of(storedToken(true, Instant.now().plus(1, ChronoUnit.DAYS))));

        assertThrows(InvalidRefreshTokenException.class,
                () -> authService.refresh(new RefreshRequestDto(OLD_REFRESH_TOKEN)));

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void refresh_shouldThrow_whenTokenHasExpired() {
        when(refreshTokenRepository.findByTokenForUpdate(OLD_REFRESH_TOKEN))
                .thenReturn(Optional.of(storedToken(false, Instant.now().minus(1, ChronoUnit.DAYS))));

        assertThrows(InvalidRefreshTokenException.class,
                () -> authService.refresh(new RefreshRequestDto(OLD_REFRESH_TOKEN)));

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void refresh_shouldRotate_revokingOldTokenAndIssuingNewOne() {
        RefreshTokenEntity stored = storedToken(false, Instant.now().plus(1, ChronoUnit.DAYS));

        when(refreshTokenRepository.findByTokenForUpdate(OLD_REFRESH_TOKEN))
                .thenReturn(Optional.of(stored));
        when(jwtService.generateAccessToken(any())).thenReturn(ACCESS_TOKEN);
        when(userRepository.getReferenceById(ID)).thenReturn(user());
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        AuthResponseDto result = authService.refresh(new RefreshRequestDto(OLD_REFRESH_TOKEN));

        assertAll(
                () -> assertTrue(stored.isRevoked(), "köhnə token ləğv olunmalıdır"),
                () -> assertNotEquals(OLD_REFRESH_TOKEN, result.refreshToken(), "yeni token verilməlidir"),
                () -> assertEquals(ACCESS_TOKEN, result.accessToken())
        );
    }

    // ---------- logout ----------

    @Test
    void logout_shouldRevokeToken() {
        RefreshTokenEntity stored = storedToken(false, Instant.now().plus(1, ChronoUnit.DAYS));
        when(refreshTokenRepository.findByTokenForUpdate(OLD_REFRESH_TOKEN))
                .thenReturn(Optional.of(stored));

        authService.logout(new RefreshRequestDto(OLD_REFRESH_TOKEN));

        assertTrue(stored.isRevoked());
    }

    @Test
    void logout_shouldBeIdempotent_whenTokenDoesNotExist() {
        when(refreshTokenRepository.findByTokenForUpdate(OLD_REFRESH_TOKEN))
                .thenReturn(Optional.empty());

        authService.logout(new RefreshRequestDto(OLD_REFRESH_TOKEN));

        verify(refreshTokenRepository, never()).save(any());
    }
}
