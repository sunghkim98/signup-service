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
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class EventHostDetailResponseDto {
        private Long eventId;
        private String hostName;
        private String eventName;
        private String place;
        private String description;
        private Long capacity;
        private Date startTime;
        private Date endTime;
        private ApprovalMode mode;
        private Set<CollectedField> collectedFields;
        private String status;
        private String eventStatus;
        private long pendingCount;
        private long approvedCount;
        private long totalCount;
        private QrLinks qrLinks;
        private List<ParticipantDto> participants;

        public static EventHostDetailResponseDto from(Event event,
                                                                 List<ParticipantDto> participants,
                                                                 long pendingCount,
                                                                 long approvedCount,
                                                                 long totalCount) {
                Member host = event.getHost();
                QrLinks qr = event.getQrLinks();

                return EventHostDetailResponseDto.builder()
                                .eventId(event.getId())
                                .hostName(host != null ? host.getName() : "Unknown")
                                .eventName(event.getName())
                                .place(event.getPlace())
                                .description(event.getDescription())
                                .capacity(event.getCapacity())
                                .startTime(event.getStartTime())
                                .endTime(event.getEndTime())
                                .mode(event.getApprovalMode())
                                .collectedFields(event.getRequiredFields())
                                .status(event.getStatus().name())
                                .eventStatus(event.getStatus().name())
                                .pendingCount(pendingCount)
                                .approvedCount(approvedCount)
                                .totalCount(totalCount)
                                .qrLinks(qr != null ? qr : new QrLinks("", ""))
                                .participants(participants)
                                .build();
        }
}
