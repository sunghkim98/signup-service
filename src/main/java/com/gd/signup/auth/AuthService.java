package com.gd.signup.auth;

import com.gd.signup.auth.dto.*;
import com.gd.signup.auth.social.SocialUserInfo;
import com.gd.signup.auth.social.kakao.KakaoUserClient;
import com.gd.signup.auth.social.naver.NaverUserClient;
import com.gd.signup.exception.customException.BadRequestException;
import com.gd.signup.jwt.JwtUtil;
import com.gd.signup.jwt.RefreshTokenRepository;
import com.gd.signup.jwt.dto.TokenResponse;
import com.gd.signup.jwt.entity.RefreshToken;
import com.gd.signup.member.MemberRepository;
import com.gd.signup.member.entity.Member;
import com.gd.signup.signature.SignatureStorageService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final SignatureStorageService signatureStorageService;
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final KakaoUserClient kakaoClient;   // 또는 SocialUserClient 인터페이스
	private final NaverUserClient naverClient;
	private final JwtUtil jwt;

	@Transactional
	public SocialLoginResponse login(SocialLoginRequest request) {

		SocialUserInfo socialUser = loadSocialUser(
				request.provider(),
				request.accessToken()
		);

		Member member;
		boolean isNewUser;

		var existing = memberRepository.findByProviderAndProviderUserId(
				socialUser.getProvider(),
				socialUser.getProviderUserId()
		);

		if (existing.isPresent()) {
			member = existing.get();
			isNewUser = false;

			if (socialUser.getPhone() != null) {
				member.changePhone(socialUser.getPhone());
			}
			if (socialUser.getEmail() != null) {
				member.changeEmail(socialUser.getEmail());
			}
			member.changeUpdateTime();

		} else {
			member = Member.fromSocial(socialUser);
			member = memberRepository.save(member);
			isNewUser = true;
		}

		String access = jwt.createAccessToken(member.getId(), member.getLoginId());
		String refresh = jwt.createRefreshToken(member.getId());

		refreshTokenRepository.deleteByMemberId(member.getId());
		RefreshToken rt = new RefreshToken();
		rt.setToken(refresh);
		rt.setMemberId(member.getId());
		rt.setExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 14));
		refreshTokenRepository.save(rt);

		// 5. 응답 DTO로 리턴
		return new SocialLoginResponse(access, refresh, isNewUser);
	}

	@Transactional
	public void signup(Long memberId, SignupRequest request) {
		// 1. 멤버 조회
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new BadRequestException("존재하지 않는 회원입니다."));

		// 2. 서명 이미지 저장
		String signaturePath = signatureStorageService
				.saveSignature(memberId, request.getSignatureImage());

		// 3. 회원/별도 엔티티에 경로 저장 (예시)
		member.changeSign(signaturePath);
		// 또는 별도의 Signature 엔티티에 저장

		// 4. 나머지 필드 업데이트도 여기서
		member.changeName(request.getName());
		member.changeTerm(request.getAgreeTerms());
		member.changePrivacy(request.getAgreePrivacy());
		member.changeMaketing(request.getAgreeMarketing());
		member.changeBirthDate(request.getBirthdate().toString());
		member.changePhone(request.getPhone());
		member.changeEmail(request.getEmail());
		member.changeUpdateTime();
	}

	private SocialUserInfo loadSocialUser(String provider, String accessToken) {
		if ("kakao".equalsIgnoreCase(provider)) {
			return kakaoClient.getUserInfo(accessToken);
		} else if ("naver".equalsIgnoreCase(provider)) {
			return naverClient.getUserInfo(accessToken);
		} else {
			throw new IllegalArgumentException("Unsupported provider: " + provider);
		}
	}

	@Transactional
	public TokenResponse refresh(String refreshToken) {
		Jws<Claims> parsed = jwt.parse(refreshToken);
		if (!jwt.isRefresh(refreshToken)) {
			throw new BadRequestException("리프레시 토큰이 아닙니다.");
		}
		Long memberId = Long.valueOf(parsed.getBody().getSubject());

		RefreshToken saved = refreshTokenRepository.findByToken(refreshToken)
				.orElseThrow(() -> new BadRequestException("유효하지 않은 리프레시 토큰입니다."));

		Member m = memberRepository.findById(memberId)
				.orElseThrow(() -> new BadRequestException("회원 없음"));

		if (m.getId() != memberId) {
			throw new BadRequestException("리프레시 토큰이 멤버의 소유가 아닙니다.");
		}

		String newAccess = jwt.createAccessToken(m.getId(), m.getLoginId());
		refreshTokenRepository.deleteByMemberId(memberId);
		String newRefresh = jwt.createRefreshToken(memberId);
		RefreshToken rt = new RefreshToken();
		rt.setToken(newRefresh);
		rt.setMemberId(memberId);
		rt.setExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 14));
		refreshTokenRepository.save(rt);

		return new TokenResponse(newAccess, newRefresh);
	}

	@Transactional
	public void logout(Long memberId) {
		refreshTokenRepository.deleteByMemberId(memberId);
	}
}

