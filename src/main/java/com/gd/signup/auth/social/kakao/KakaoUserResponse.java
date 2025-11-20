package com.gd.signup.auth.social.kakao;

/**
 * Kakao /v2/user/me 응답 JSON 매핑용 DTO
 * 실제 필드는 카카오 문서 참고해서 필요하면 추가/수정하면 됨.
 */
public class KakaoUserResponse {

	private Long id;
	private KakaoAccount kakao_account;

	public Long getId() {
		return id;
	}

	public KakaoAccount getKakaoAccount() {
		return kakao_account;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setKakaoAccount(KakaoAccount kakao_account) {
		this.kakao_account = kakao_account;
	}

	public static class KakaoAccount {
		private String name;           // 실명
		private String email;
		private String phone_number;   // "+82 10-1234-5678" 이런 형식
		private Profile profile;

		public String getName() {
			return name;
		}

		public String getEmail() {
			return email;
		}

		public String getPhoneNumber() {
			return phone_number;
		}

		public Profile getProfile() {
			return profile;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public void setPhoneNumber(String phone_number) {
			this.phone_number = phone_number;
		}

		public void setProfile(Profile profile) {
			this.profile = profile;
		}
	}

	public static class Profile {
		private String nickname;

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
	}
}


