package com.example.movies.security;

import com.example.movies.constant.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AuthenticatedUser implements UserDetails {

  private final Long id;
  private final String email;
  private final String password;
  private final Role role;

  public AuthenticatedUser(Long id, String email, Role role) {
    this(id, email, null, role);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }
}