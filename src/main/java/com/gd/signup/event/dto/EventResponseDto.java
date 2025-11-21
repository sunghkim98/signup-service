package com.gd.signup.event.dto;

import com.gd.signup.event.entity.Event;
import com.gd.signup.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@Builder
@AllArgsConstructor
public class EventResponseDto {

	private Long eventId;          // 이벤트 ID
        private String hostName;       // 주최자 이름
        private String eventName;      // 이벤트 이름
        private String place;          // 장소
        private String description;    // 설명
        private Long capacity;         // 최대 인원
        private Date startTime;        // 시작 시간
        private Date endTime;          // 종료 시간
	private Date createTime;       // 생성 시각
	private Date updateTime;       // 수정 시각

	public static EventResponseDto from(Event event) {
		Member host = event.getHost();

		return EventResponseDto.builder()
				.eventId(event.getId())
                                .hostName(host != null ? host.getName() : "Unknown") // host null일 경우 대비
                                .eventName(event.getName())
                                .place(event.getPlace())
                                .description(event.getDescription())
                                .capacity(event.getCapacity())
                                .startTime(event.getStartTime())
                                .endTime(event.getEndTime())
				.createTime(event.getCreateTime())
				.updateTime(event.getUpdateTime())
				.build();
	}
}
