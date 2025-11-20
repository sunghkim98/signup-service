package com.gd.signup.regist.entity;

import com.gd.signup.event.Enum.ApprovalMode;
import com.gd.signup.regist.Enum.RegistrationStatus;
import com.gd.signup.event.entity.Event;
import com.gd.signup.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "registration",
		uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "member_id"}))
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_participant_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "event_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id", referencedColumnName = "member_id",
			foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
	private Member member;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "joined_at", nullable = false)
	private Date joinedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 20, nullable = false)
	private RegistrationStatus status = RegistrationStatus.REQUESTED;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false)
	private Date createTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", nullable = false)
	private Date updateTime;

	public Registration(Event event, Member member) {
		this.event = event;
		this.member = member;
		this.joinedAt = new Date();
//		this.eventTitleSnapshot = event.getName();
//		this.scheduledStartSnapshot = event.getStartTime();
//		this.scheduledEndSnapshot = event.getEndTime();
		this.createTime = new Date();
		this.updateTime = createTime;
	}

	public void updateStatus(RegistrationStatus status) {
		this.status = status;
	}

	public void updateTime() {this.updateTime = new Date();}

}
