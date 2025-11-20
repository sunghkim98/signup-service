package com.gd.signup.auth.social.kakao;

import com.gd.signup.auth.social.SocialUserClient;
import com.gd.signup.auth.social.SocialUserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoUserClient implements SocialUserClient {

	private final WebClient webClient;

	public KakaoUserClient(WebClient.Builder builder) {
		this.webClient = builder
				.baseUrl("https://kapi.kakao.com")
				.build();
	}

	@Override
	public SocialUserInfo getUserInfo(String accessToken) {
		KakaoUserResponse response = webClient.get()
				.uri("/v2/user/me")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.retrieve()
				.bodyToMono(KakaoUserResponse.class)
				.block();

		if (response == null) {
			throw new IllegalStateException("Kakao의 회원 정보가 없습니다.");
		}

		String name = null;
		String email = null;
		String phone = null;
		String nickname = null;

		KakaoUserResponse.KakaoAccount account = response.getKakaoAccount();
		if (account != null) {
			name = account.getName();
			email = account.getEmail();
			phone = account.getPhoneNumber();
			if (account.getProfile() != null) {
				nickname = account.getProfile().getNickname();
			}
		}

		return new SocialUserInfo(
				"kakao",                      // provider
				String.valueOf(response.getId()), // providerUserId
				name,                         // 실명
				nickname,                     // 닉네임
				email,                        // 이메일
				phone                         // 전화번호
		);
	}

}

