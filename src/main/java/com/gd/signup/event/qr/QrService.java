package com.gd.signup.event.qr;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QrService {
	@Value("${app.qr.payload-base:http://signup.app}")
	private String payloadBase;

	public QrLinks issueForEvent(Long eventId) {
		// 실제 QR 안에 들어갈 URL만 생성
		String inviteUrl = "%s/events/%d/invite".formatted(payloadBase, eventId);
		String attendanceUrl = "%s/events/%d/check-in".formatted(payloadBase, eventId);

		return new QrLinks(inviteUrl, attendanceUrl);
	}

}
