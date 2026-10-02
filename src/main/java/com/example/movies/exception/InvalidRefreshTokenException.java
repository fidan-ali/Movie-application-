package com.example.movies.exception;

public class InvalidRefreshTokenException extends LocalizedException {
  public InvalidRefreshTokenException() {
    super(ErrorCode.INVALID_REFRESH_TOKEN, "error.auth.invalid-refresh-token");
  }
}