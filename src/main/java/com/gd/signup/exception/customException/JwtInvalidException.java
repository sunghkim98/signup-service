package com.gd.signup.exception.customException;

import com.gd.signup.exception.BaseException;
import com.gd.signup.response.StatusCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class JwtInvalidException extends BaseException {
	public JwtInvalidException(String msg) { super(msg, StatusCode.UNAUTHORIZED);}
}
