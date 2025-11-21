package com.gd.signup.regist.dto;

import com.gd.signup.event.Enum.CollectedField;
import com.gd.signup.event.EventStatus;
import com.gd.signup.event.qr.QrLinks;
import com.gd.signup.regist.Enum.RegistrationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RegisteredEventDto {

	private Long eventId;
        private String eventName;
        private String place;
        private String description;
        private Long capacity;
        private Date startTime;
        private Date endTime;
	private EventStatus eventStatus;              // Event.status
	private QrLinks qrLinks;                      // Event.qrLinks
	private RegistrationStatus registrationStatus; // Registration.status

        public RegisteredEventDto(Long eventId,
                                                          String eventName,
                                                          String place,
                                                          String description,
                                                          Long capacity,
                                                          Date startTime,
                                                          Date endTime,
							  EventStatus eventStatus,
							  QrLinks qrLinks,
							  RegistrationStatus registrationStatus) {
                this.eventId = eventId;
                this.eventName = eventName;
                this.place = place;
                this.description = description;
                this.capacity = capacity;
                this.startTime = startTime;
                this.endTime = endTime;
		this.eventStatus = eventStatus;
		this.qrLinks = qrLinks;
		this.registrationStatus = registrationStatus;
	}

}

