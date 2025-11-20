package com.gd.signup.event.dto;

import com.gd.signup.event.Enum.ApprovalMode;
import com.gd.signup.event.Enum.CollectedField;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
public class EventCreateDto {
	private String eventName;
	private String place;
	private Long capacity;
	private Date startTime;
	private Date endTime;
	private ApprovalMode mode;
	private Set<CollectedField> collectedFields;
}
