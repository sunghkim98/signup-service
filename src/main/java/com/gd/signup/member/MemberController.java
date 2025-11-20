package com.gd.signup.member;

import com.gd.signup.jwt.UserPrincipal;
import com.gd.signup.member.dto.MemberCreateDto;
import com.gd.signup.member.dto.MemberResponseDto;
import com.gd.signup.member.entity.Member;
import com.gd.signup.response.Response;
import com.gd.signup.response.ResponseData;
import com.gd.signup.response.ResponseMsg;
import com.gd.signup.response.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	private final MemberService memberService;

	/**
	 멤버 데이터(참여 이력) 조회
	 **/
	@GetMapping
	public ResponseEntity memberEventHistory() {
		int dummy = 3;
		// DB에서 멤버 참여 이력 조회 (리스트)
		// 없는 멤버면 에러 처리
		return new ResponseEntity(ResponseData.res(StatusCode.OK, ResponseMsg.GET_DATA_SUCCESS, dummy), HttpStatus.OK);
	}

	/**
	 멤버 데이터(참여 이력) 조회
	 **/
	@GetMapping("/sign")
	public ResponseEntity<byte[]> getSign(@AuthenticationPrincipal UserPrincipal me) {
		byte[] image = memberService.getSignById(me.id());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		headers.setContentLength(image.length);

		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}

	/**
		멤버 데이터(멤버 정보) 조회
	 **/
	@GetMapping("/me")
	public ResponseEntity memberInformation(@AuthenticationPrincipal UserPrincipal me) {
		MemberResponseDto memberResponseDto = memberService.getMemberInfo(me.id());

		return new ResponseEntity(ResponseData.res(StatusCode.OK, ResponseMsg.GET_DATA_SUCCESS, memberResponseDto), HttpStatus.OK);
	}

}
