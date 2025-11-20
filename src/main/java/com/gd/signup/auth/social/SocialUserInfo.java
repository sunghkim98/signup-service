package com.gd.signup.auth.social;

public class SocialUserInfo {

	private final String provider;        // "kakao" / "naver"
	private final String providerUserId;  // kakao id, naver id
	private final String name;            // 실명 (가능하면)
	private final String nickname;        // 별명
	private final String email;
	private final String phone;

	public SocialUserInfo(String provider, String providerUserId, String name, String nickname, String email, String phone) {
		this.provider = provider;
		this.providerUserId = providerUserId;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.phone = phone;
	}


	public String getProvider() { return provider; }
	public String getProviderUserId() { return providerUserId; }
	public String getName() { return name; }
	public String getNickname() { return nickname; }
	public String getEmail() { return email; }
	public String getPhone() { return phone; }
}
