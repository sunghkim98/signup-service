package com.gd.signup.auth.social.naver;

import com.gd.signup.auth.social.SocialUserClient;
import com.gd.signup.auth.social.SocialUserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NaverUserClient implements SocialUserClient {

	private final WebClient webClient;

	public NaverUserClient(WebClient.Builder builder) {
		this.webClient = builder
				.baseUrl("https://openapi.naver.com")
				.build();
	}

	@Override
	public SocialUserInfo getUserInfo(String accessToken) {
		NaverUserResponse response = webClient.get()
				.uri("/v1/nid/me")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.retrieve()
				.bodyToMono(NaverUserResponse.class)
				.block();

		if (response == null || response.getResponse() == null) {
			throw new IllegalStateException("Naver user info response is null");
		}

		NaverUserResponse.Response profile = response.getResponse();

		String provider = "naver";
		String providerUserId = profile.getId();        // 네이버가 주는 앱별 유니크 ID
		String name = profile.getName();                // 실명
		String nickname = profile.getNickname();        // 별명
		String email = profile.getEmail();
		// mobile_e164가 있으면 그걸 우선, 없으면 mobile 사용
		String phone = profile.getMobileE164() != null
				? profile.getMobileE164()
				: profile.getMobile();

		return new SocialUserInfo(
				provider,
				providerUserId,
				name,
				nickname,
				email,
				phone
		);
	}
}
