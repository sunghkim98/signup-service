// src/main/java/com/gd/signup/event/entity/Event.java
package com.gd.signup.event.entity;

import com.gd.signup.event.Enum.ApprovalMode;
import com.gd.signup.event.Enum.CollectedField;
import com.gd.signup.event.EventStatus;
import com.gd.signup.event.dto.EventEditDto;
import com.gd.signup.event.qr.QrLinks;
import com.gd.signup.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "events")
@SequenceGenerator(
		name = "EVENT_SEQ_GENERATOR",
		sequenceName = "EVENT_SEQ",
		initialValue = 1, allocationSize = 50
)
@SQLDelete(sql = "UPDATE events SET deleted = true, update_time = now() WHERE event_id = ?")
@Where(clause = "deleted = false") // 기본 조회에서 삭제건 제외
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_SEQ_GENERATOR")
	@Column(name = "event_id")
	private Long id;

//	 호스트(Member) FK — Member의 PK 컬럼명이 member_id 이므로 referencedColumnName 지정
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "host_id", referencedColumnName = "member_id",
			foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
	private Member host;

	@Column(nullable = false, length = 200)
	private String name;

	@Column(length = 200)
	private String place;

	// 수용 인원(의미를 명확히 capacity로)
	@Column(name = "capacity")
	private Long capacity;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", nullable = false)
	private Date startTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", nullable = false)
	private Date endTime;

	// 상태
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EventStatus status = EventStatus.SCHEDULED;

	@Column(nullable = false)
	private boolean deleted = false;

	// 취소 메타(이력 보존용)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "canceled_at")
	private Date canceledAt;

	@Column(name = "canceled_reason")
	private String canceledReason;

	// 생성/수정 시각
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false)
	private Date createTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", nullable = false)
	private Date updateTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApprovalMode approvalMode;

	@Enumerated(EnumType.STRING)
	@Column(name = "collect_field") // ex) "NAME", "PHONE" 등
	private Set<CollectedField> requiredFields = new HashSet<>();

	@Embedded
	private QrLinks qrLinks;

	public Event(Member host, String name, String place,
				 Long capacity, Date startTime, Date endTime,
				 ApprovalMode mode, Set<CollectedField> requiredFields) {
		this.host = host;
		this.name = name;
		this.place = place;
		this.capacity = capacity;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = EventStatus.SCHEDULED;
		this.deleted = false;
		this.approvalMode = mode;
		this.requiredFields = requiredFields;
	}

	public void inputQrlink(QrLinks qrLinks) {
		this.qrLinks = new QrLinks(qrLinks.getInviteUrl(), qrLinks.getAttendanceUrl());
	}
	public void running() {
		this.status = EventStatus.RUNNING;
	}

	public void edit(EventEditDto e) {
		this.name = e.getEventName();
		this.place = e.getPlace();
		this.capacity = e.getCapacity();
		this.startTime = e.getStartTime();
		this.endTime = e.getEndTime();
	}

	public void close() {
		this.status = EventStatus.CLOSE;
	}
	public void cancel(String reason) {
		this.status = EventStatus.CANCELED;
		this.canceledAt = new Date();
		this.canceledReason = reason;
		this.updateTime = new Date();
	}

	public void softDelete() {
		this.deleted = true;
		this.updateTime = new Date();
	}

	@PrePersist
	protected void onCreate() {
		Date now = new Date();
		this.createTime = (this.createTime == null) ? now : this.createTime;
		this.updateTime = (this.updateTime == null) ? now : this.updateTime;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updateTime = new Date();
	}
}
