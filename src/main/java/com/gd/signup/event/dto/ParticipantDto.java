package com.gd.signup.event.dto;

import com.gd.signup.member.entity.Member;
import com.gd.signup.regist.entity.Registration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantDto {
        private Long registrationId;
        private Long memberId;
        private String name;
        private String phone;
        private String status;
        private String birthdate;
        private String email;
        private Integer gender;

        public static ParticipantDto from(Registration registration) {
                Member member = registration.getMember();

                return ParticipantDto.builder()
                                .registrationId(registration.getId())
                                .memberId(member != null ? member.getId() : null)
                                .name(member != null ? member.getName() : "Unknown")
                                .phone(member != null ? member.getPhone() : null)
                                .status(registration.getStatus().name())
                                .birthdate(member != null ? member.getBirthDate() : null)
                                .email(member != null ? member.getEmail() : null)
                                .gender(member != null ? member.getGender() : null)
                                .build();
        }
}
