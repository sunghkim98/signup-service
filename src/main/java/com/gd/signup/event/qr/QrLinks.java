package com.gd.signup.event.qr;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class QrLinks {
	private String inviteUrl;
	private String attendanceUrl;

	public QrLinks(String inviteUrl, String attendanceUrl) {
		this.inviteUrl = inviteUrl;
		this.attendanceUrl = attendanceUrl;
	}

	public static QrLinks from(QrLinks other) {
		return new QrLinks(other.getInviteUrl(), other.getAttendanceUrl());
	}

	// 값 객체 비교를 위한 equals/hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof QrLinks qr)) return false;
		return java.util.Objects.equals(inviteUrl, qr.inviteUrl)
				&& java.util.Objects.equals(attendanceUrl, qr.attendanceUrl);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(inviteUrl, attendanceUrl);
	}

}
