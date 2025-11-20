package com.gd.signup.event.dto;

import com.gd.signup.event.entity.Event;
import com.gd.signup.member.entity.Member;
import lombok.*;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
public class EventEditDto {
	private Long eventId;          // 이벤트 ID
	private String eventName;      // 이벤트 이름
	private String place;          // 장소
	private Long capacity;         // 최대 인원
	private Date startTime;        // 시작 시간
	private Date endTime;          // 종료 시간

}
