package com.example.movies.exception;

public class InvalidCredentialsException extends LocalizedException {
  public InvalidCredentialsException() {
    super(ErrorCode.INVALID_CREDENTIALS, "error.auth.invalid-credentials");
  }
}
