package com.example.movies.security;

import com.example.movies.constant.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.movies.constant.Constant.BEARER_PREFIX;
import static com.example.movies.constant.Constant.CLAIM_EMAIL;
import static com.example.movies.constant.Constant.CLAIM_ROLE;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !header.startsWith(BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      Claims claims = jwtService.parseAccessToken(header.substring(BEARER_PREFIX.length()));

      AuthenticatedUser principal = new AuthenticatedUser(
          Long.valueOf(claims.getSubject()),
          claims.get(CLAIM_EMAIL, String.class),
          Role.valueOf(claims.get(CLAIM_ROLE, String.class)));

      SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(
              principal, null, principal.getAuthorities()));

    } catch (JwtException | IllegalArgumentException ex) {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }
}