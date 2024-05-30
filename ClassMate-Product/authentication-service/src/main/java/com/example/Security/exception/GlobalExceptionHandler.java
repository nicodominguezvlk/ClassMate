package com.example.Security.exception;

import com.example.Security.service.TokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailNotValidException.class)
    public ResponseEntity<ErrorDetails> handleEmailNotValidException(EmailNotValidException exception, WebRequest request){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .errorCode("EMAIL_FORMAT_NOT_VALID")
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUsernameNotFoundException(EmailNotValidException exception, WebRequest request){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .errorCode("USER_NOT_FOUND")
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ErrorDetails> handleEmailAlreadyTakenException(EmailAlreadyTakenException exception, WebRequest request){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .errorCode("EMAIL_ALREADY_TAKEN")
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleTokenNotFoundException(TokenNotFoundException exception, WebRequest request){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .errorCode("TOKEN_NOT_FOUND")
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenAlreadyConfirmedException.class)
    public ResponseEntity<ErrorDetails> handleTokenAlreadyConfirmedException(TokenAlreadyConfirmedException exception, WebRequest request){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .errorCode("TOKEN_ALREADY_CONFIRMED")
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorDetails> handleTokenExpiredException(TokenExpiredException exception, WebRequest request){
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .path(request.getDescription(false))
                .errorCode("TOKEN_EXPIRED")
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
