package com.gd.signup.auth;

import com.gd.signup.auth.dto.SignupRequest;
import com.gd.signup.auth.dto.SocialLoginResponse;
import com.gd.signup.jwt.UserPrincipal;
import com.gd.signup.auth.dto.SocialLoginRequest;
import com.gd.signup.jwt.dto.TokenResponse;
import com.gd.signup.member.MemberService;
import com.gd.signup.response.Response;
import com.gd.signup.response.ResponseData;
import com.gd.signup.response.ResponseMsg;
import com.gd.signup.response.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity debugSignup(@AuthenticationPrincipal UserPrincipal me,
									  @Valid @RequestBody SignupRequest signupRequest) {
		log.info("Signup request: provider={}, name={}, phone={}, email={}, birthdate={}",
				signupRequest.getProvider(), signupRequest.getName(), signupRequest.getPhone(),
				signupRequest.getEmail(), signupRequest.getBirthdate());

		log.info("Agreements: terms={}, privacy={}, marketing={}",
				signupRequest.getAgreeTerms(), signupRequest.getAgreePrivacy(), signupRequest.getAgreeMarketing());

		log.info("SignatureImage length: {}",
				signupRequest.getSignatureImage() != null ? signupRequest.getSignatureImage().length() : 0);

		authService.signup(me.id(), signupRequest);

		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.CREATE_MEMBER), HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<SocialLoginResponse> login(@Valid @RequestBody SocialLoginRequest req) {
		//TODO 소셜 로그인 채워넣기
		SocialLoginResponse response = authService.login(req);

		return new ResponseEntity(ResponseData.res(StatusCode.OK, ResponseMsg.LOGIN_SUCCESS, response), HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity refresh(@RequestBody Map<String, String> body) {
		String refreshToken = body.get("refreshToken");

		return new ResponseEntity(ResponseData.res(StatusCode.OK, ResponseMsg.GET_DATA_SUCCESS, authService.refresh(refreshToken)), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@AuthenticationPrincipal UserPrincipal me) {
		authService.logout(me.id());

		return ResponseEntity.noContent().build();
	}

}

