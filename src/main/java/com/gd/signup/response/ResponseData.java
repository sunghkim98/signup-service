package com.gd.signup.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ResponseData<T> {
	private int statusCode;
	private String responseMsg;
	private T data;

	public static<T> ResponseData<T> res(final int statusCode, final String responseMsg, final T t) {
		return ResponseData.<T>builder()
				.data(t)
				.statusCode(statusCode)
				.responseMsg(responseMsg)
				.build();
	}
}
