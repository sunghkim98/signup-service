package com.gd.signup.member.dto;

import com.gd.signup.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
	private final Long id;
	private final String name;
	private final String loginId;
	private final String phone;
	private final String email;
	private String birthDate;
	private final int gender;

	public MemberResponseDto(Member m) {
		// 비밀번호/토큰/내부시간 컬럼 등은 의도적으로 제외
		this.id = m.getId();
		this.name = m.getName();
		this.loginId = m.getLoginId();
		this.phone = m.getPhone();
		this.email = m.getEmail();
		this.birthDate = m.getBirthDate();
		this.gender = m.getGender();
	}

}
