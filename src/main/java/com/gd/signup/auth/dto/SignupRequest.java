package com.gd.signup.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
public class SignupRequest {

	@NotBlank
	private String provider;          // kakao / naver 등

	@NotBlank
	private String name;

	@NotBlank
	private String phone;

	@Email
	@NotBlank
	private String email;

	// "19980330" 형식 → LocalDate 로 받고 싶으면 이렇게
	@NotNull
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate birthdate;

	@NotNull
	private Boolean agreeTerms;

	@NotNull
	private Boolean agreePrivacy;

	@NotNull
	private Boolean agreeMarketing;

	/**
	 * 서명 이미지 (PNG base64 문자열)
	 * data:image/png;base64, … 이런 prefix 없이 순수 base64 라고 가정
	 */
	@NotBlank
	private String signatureImage;

}