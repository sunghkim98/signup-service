package com.gd.signup.response;

public class ResponseMsg {
	/** 성공 **/
	public static final String GET_DATA_SUCCESS = "데이터 조회 성공";
	public static final String ADMIN_LOGIN_SUCCESS = "관리자 로그인 성공";
	public static final String GET_IMAGE_SUCCESS = "이미지 저장 성공";
	public static final String DELETE_MEMBER = "멤버 삭제 성공";
	public static final String APPROVE_EVENT = "이벤트 승인 성공";
	public static final String REGIST_EVENT = "이벤트 참여 성공";
	public static final String LOGIN_SUCCESS = "로그인 성공";
	public static final String LOGOUT_SUCCESS = "로그아웃 성공";
	public static final String CREATE_MEMBER = "멤버 생성 성공";
	public static final String SET_MSG = "상태메시지 저장 성공";
	public static final String CREATE_EVENT = "이벤트 생성 성공";
	public static final String CHANGE_EVENT_NAME = "이벤트 이름 변경 성공";
	public static final String DELETE_EVENT = "이벤트 삭제 성공";
	public static final String CHANGE_EVENT_STATUS = "이벤트 상태 변경 성공";
	public static final String ADD_MEMBER_TO_EVENT = "이벤트에 참여 성공";
	public static final String DELETE_MEMBER_FROM_EVENT = "이벤트에서 멤버 삭제 성공";
	/** 실패 **/
	public static final String NO_TOKEN = "토큰 없음";

	public static final String CANNOT_ACCESS_AGREE = "개인정보제공 동의 화면 접근 불가";
	public static final String LOGIN_FAIL = "로그인 실패";
	public static final String UNREGISTERED = "등록되지 않은 회원";
	public static final String ADMIN_FAIL = "관리자 로그인 실패";
	public static final String NO_SESSION = "세션 없음";

	public static final String DEFAULT_EVENT_NAME = "사용자 설정 불가 이름";
	public static final String JSON_DESERIALIZE_FAILED = "Json 매핑 실패";
	public static final String TOO_MANY_REQUEST = "Api 요청 횟수 초과";
	public static final String BAD_REQUEST = "잘못된 요청";
	public static final String NOT_FOUND = "데이터를 찾을 수 없음";
	public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
	public static final String DB_ERROR = "데이터베이스 에러";
}
