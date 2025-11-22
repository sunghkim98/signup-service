package com.gd.signup.event.dto;

import com.gd.signup.event.Enum.ApprovalMode;
import com.gd.signup.event.Enum.CollectedField;
import com.gd.signup.event.entity.Event;
import com.gd.signup.event.qr.QrLinks;
import com.gd.signup.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class EventHostResponseDto {
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
        private QrLinks qrLinks;
        private ApprovalMode mode;     // 승인 방식
        private Set<CollectedField> collectedFields; // 수집 항목
        private String status;         // 이벤트 상태
        private long pendingCount;     // 승인 대기 인원 수
        private long approvedCount;    // 승인 완료 인원 수
        private long totalCount;       // 전체 신청 인원 수

        public static EventHostResponseDto from(Event event, long pendingCount, long approvedCount, long totalCount) {
                Member host = event.getHost();
                QrLinks qr = event.getQrLinks();

                return EventHostResponseDto.builder()
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
                                .qrLinks(qr != null ? qr : new QrLinks("", ""))
                                .mode(event.getApprovalMode())
                                .collectedFields(event.getRequiredFields())
                                .status(event.getStatus().name())
                                .pendingCount(pendingCount)
                                .approvedCount(approvedCount)
                                .totalCount(totalCount)
                                .build();
        }
}
