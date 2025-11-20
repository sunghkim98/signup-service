package com.gd.signup.member.entity;

import com.gd.signup.auth.social.SocialUserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseMember{
	private String name;
	private String loginId;
	private String sign;
	@Column(nullable = false)
	private String provider;
	@Column(nullable = false, unique = true)
	private String providerUserId;
	private String birthDate;
	private String phone;
	private String email;
	private int gender;

	private Boolean agreeTerms;
	private Boolean agreePrivacy;
	private Boolean agreeMarketing;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	public Member(String loginId,
				  String provider, String providerUserId) {
		this.loginId = loginId;
		this.provider = provider;
		this.providerUserId = providerUserId;
		this.createTime = new Date();
		this.updateTime = new Date();
		this.agreeMarketing = false;
		this.agreeTerms = false;
		this.agreePrivacy = false;
	}

	public void changeName(String name) {this.name = name;}
	public void changeProviderUserId(String providerUserId) { this.providerUserId = providerUserId; }
	public void changeProvider(String provider) { this.provider = provider; }
	public void changeTerm(boolean term) { this.agreeTerms = term; }
	public void changePrivacy(boolean agreePrivacy) { this.agreePrivacy = agreePrivacy; }
	public void changeMaketing(boolean agreeMarketing) { this.agreeMarketing = agreeMarketing; }
	public void changeUpdateTime() {
		this.updateTime = new Date();
	}
	public void changeBirthDate(String birthDate) { this.birthDate = birthDate; }

	public void changeSign(String sign) {
		this.sign = sign;
	}

	public void changePhone(String phone) {
		this.phone = phone;
	}

	public void changeEmail(String email) {
		this.email = email;
	}

	public void changeGender(int gender) {
		this.gender = gender;
	}

	public static Member fromSocial(SocialUserInfo info) {
		// loginId 규칙: "{provider}_{providerUserId}"
		String loginId = info.getProvider() + "_" + info.getProviderUserId();

		// name이 없으면 nickname으로 대체
		String name = info.getName() != null ? info.getName() : info.getNickname();

		return new Member(
				loginId,
				info.getProvider(),
				info.getProviderUserId()
		);
	}
}
