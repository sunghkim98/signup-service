package com.gd.signup.auth.social.naver;


public class NaverUserResponse {

	private String resultcode;
	private String message;
	private Response response;

	public String getResultcode() {
		return resultcode;
	}

	public String getMessage() {
		return message;
	}

	public Response getResponse() {
		return response;
	}

	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public static class Response {
		private String id;
		private String name;
		private String nickname;
		private String email;
		private String mobile;       // "010-1234-5678"
		private String mobile_e164;  // "+821012345678"
		private String gender;
		private String age;
		private String birthday;
		private String birthyear;

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getNickname() {
			return nickname;
		}

		public String getEmail() {
			return email;
		}

		public String getMobile() {
			return mobile;
		}

		public String getMobileE164() {
			return mobile_e164;
		}

		public String getGender() {
			return gender;
		}

		public String getAge() {
			return age;
		}

		public String getBirthday() {
			return birthday;
		}

		public String getBirthyear() {
			return birthyear;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public void setMobileE164(String mobile_e164) {
			this.mobile_e164 = mobile_e164;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public void setBirthyear(String birthyear) {
			this.birthyear = birthyear;
		}
	}
}
