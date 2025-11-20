package com.gd.signup.regist.Enum;

public enum RegistrationStatus {
	REQUESTED, // 요청 (event가 Auto모드가 아니면 활성화)
	APPROVED, // 승인
	REJECTED, // 거절
	CANCELED, // 취소
	NO_SHOW // 신청하고 참석안함 -> 필요하려나?
}
