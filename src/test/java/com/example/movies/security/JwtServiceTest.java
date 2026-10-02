package com.example.movies.security;

import com.example.movies.constant.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import static com.example.movies.constant.Constant.CLAIM_EMAIL;
import static com.example.movies.constant.Constant.CLAIM_ROLE;
import static com.example.movies.constant.MovieApiTestConstants.EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private static final String SECRET = "test-secret-that-is-at-least-32-bytes-long!!";
    private static final String OTHER_SECRET = "a-completely-different-secret-also-32-bytes!";
    private static final long FIFTEEN_MINUTES = 15L;
    private static final long ALREADY_EXPIRED = -1L;

    private final JwtService jwtService = new JwtService(SECRET, FIFTEEN_MINUTES);

    private AuthenticatedUser principal() {
        return new AuthenticatedUser(ID, EMAIL, Role.ROLE_USER);
    }

    @Test
    void generatedToken_shouldCarryIdEmailAndRole() {
        String token = jwtService.generateAccessToken(principal());

        Claims claims = jwtService.parseAccessToken(token);

        assertEquals(String.valueOf(ID), claims.getSubject());
        assertEquals(EMAIL, claims.get(CLAIM_EMAIL, String.class));
        assertEquals(Role.ROLE_USER.name(), claims.get(CLAIM_ROLE, String.class));
    }

    @Test
    void generatedToken_shouldHaveThreeDotSeparatedParts() {
        String token = jwtService.generateAccessToken(principal());

        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void generatedToken_shouldExpireInTheFuture() {
        String token = jwtService.generateAccessToken(principal());

        Claims claims = jwtService.parseAccessToken(token);

        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }

    @Test
    void parse_shouldReject_whenPayloadWasTampered() {
        String token = jwtService.generateAccessToken(principal());
        String[] parts = token.split("\\.");
        String tampered = parts[0] + "." + parts[1] + "x." + parts[2];

        assertThrows(JwtException.class, () -> jwtService.parseAccessToken(tampered));
    }

    @Test
    void parse_shouldReject_whenSignedWithAnotherSecret() {
        JwtService attacker = new JwtService(OTHER_SECRET, FIFTEEN_MINUTES);
        String forged = attacker.generateAccessToken(principal());

        assertThrows(JwtException.class, () -> jwtService.parseAccessToken(forged));
    }

    @Test
    void parse_shouldReject_whenTokenHasExpired() {
        JwtService expiring = new JwtService(SECRET, ALREADY_EXPIRED);
        String expired = expiring.generateAccessToken(principal());

        assertThrows(ExpiredJwtException.class, () -> jwtService.parseAccessToken(expired));
    }

    @Test
    void parse_shouldReject_whenTokenIsGarbage() {
        assertThrows(RuntimeException.class, () -> jwtService.parseAccessToken("not.a.token"));
    }
}
