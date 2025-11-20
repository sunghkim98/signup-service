package com.gd.signup.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class SocialLoginResponse {
	private String accessToken;  // 우리 서비스용 JWT
	private String refreshToken;
	@JsonProperty("isNewUser")
	private boolean isNewUser;   // true면 이번에 회원가입된 유저

}
