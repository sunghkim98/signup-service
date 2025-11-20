package com.gd.signup.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberCreateDto {
	private String name;
	private String loginId;
	private String loginPw;
	private String sign;
	private String phone;
	private String email;
	private int gender;
	private int platform;

}
