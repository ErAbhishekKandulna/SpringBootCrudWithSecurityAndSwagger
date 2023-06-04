package com.app.abhi.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.abhi.exception.UserNotFoundException;

@RestControllerAdvice
public class MyCustomExceptionHandler
{
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> showUserNotFoundError(UserNotFoundException unfe)
	{
		return new ResponseEntity<String>(unfe.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
