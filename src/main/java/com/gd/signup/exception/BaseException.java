package com.gd.signup.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException{
	private int errorCode;

	public BaseException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
}
