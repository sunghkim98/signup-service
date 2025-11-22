package com.gd.signup.event.dto;

import com.gd.signup.member.entity.Member;
import com.gd.signup.regist.entity.Registration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantSimpleDto {
        private final String name;
        private final String phone;

        public static ParticipantSimpleDto from(Registration registration) {
                Member member = registration.getMember();

                return ParticipantSimpleDto.builder()
                                .name(member != null ? member.getName() : "Unknown")
                                .phone(member != null ? member.getPhone() : null)
                                .build();
        }
}
