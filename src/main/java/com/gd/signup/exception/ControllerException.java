package com.gd.signup.exception;

import com.gd.signup.exception.customException.BadRequestException;
import com.gd.signup.exception.customException.JwtInvalidException;
import com.gd.signup.exception.customException.NotFoundException;
import com.gd.signup.response.Response;
import com.gd.signup.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerException {
	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity handleBadRequestException(BadRequestException e) {
		return new ResponseEntity(Response.res(e.getErrorCode(), e.getMessage()), HttpStatus.valueOf(e.getErrorCode()));
	}

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity handleNotFoundException(NotFoundException e) {
		return new ResponseEntity(Response.res(StatusCode.NOT_FOUND, e.getMessage()), HttpStatus.valueOf(StatusCode.NOT_FOUND));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity handleAll(Exception e) {
		return new ResponseEntity(Response.res(StatusCode.UNEXPECTED_ERROR, e.getMessage()), HttpStatus.valueOf(StatusCode.UNEXPECTED_ERROR));
	}

	@ExceptionHandler(JwtInvalidException.class)
	protected ResponseEntity handleJwtInvalidException(JwtInvalidException e) {
		return new ResponseEntity(Response.res(StatusCode.UNAUTHORIZED, e.getMessage()), HttpStatus.valueOf(StatusCode.UNAUTHORIZED));
	}

}
