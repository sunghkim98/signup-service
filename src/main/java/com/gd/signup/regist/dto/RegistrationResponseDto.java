package com.gd.signup.regist.dto;

import com.gd.signup.member.entity.Member;
import com.gd.signup.regist.entity.Registration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class RegistrationResponseDto {
	private Long registrationId;     // 등록 ID
	private Long eventId;            // 이벤트 ID
	private String memberName;       // 참가자 이름
	private String status;           // 상태 (REQUESTED, APPROVED 등)
	private Date createTime;         // 신청 시각

	public static RegistrationResponseDto from(Registration r) {
		Member m = r.getMember();
		return RegistrationResponseDto.builder()
				.registrationId(r.getId())
				.eventId(r.getEvent().getId())
				.memberName(m != null ? m.getName() : "Unknown")
				.status(r.getStatus().name())
				.createTime(r.getCreateTime())
				.build();
	}

}
