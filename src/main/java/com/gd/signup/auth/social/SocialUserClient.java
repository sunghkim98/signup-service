package com.gd.signup.auth.social;

/**
 * Kakao, Naver 등 "소셜 사용자 정보 조회"를 위한 공통 인터페이스
 */
public interface SocialUserClient {

	/**
	 * 주어진 accessToken으로 provider 쪽 사용자 정보를 조회해서
	 * 우리 서비스에서 쓰기 좋은 형태(SocialUserInfo)로 변환해서 리턴.
	 */
	SocialUserInfo getUserInfo(String accessToken);
}
