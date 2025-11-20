package com.gd.signup.member;

import com.gd.signup.exception.customException.BadRequestException;
import com.gd.signup.exception.customException.NotFoundException;
import com.gd.signup.member.dto.MemberCreateDto;
import com.gd.signup.member.dto.MemberResponseDto;
import com.gd.signup.member.entity.Member;
import com.gd.signup.response.StatusCode;
import com.gd.signup.signature.SignatureStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final SignatureStorageService signatureStorageService;

	@Transactional(readOnly = true)
	public MemberResponseDto getMemberInfo(Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다."));

		MemberResponseDto memberResponseDto = new MemberResponseDto(member);

		return memberResponseDto;
	}

	@Transactional
	public byte[] getSignById(Long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다."));

		byte[] imageBytes = signatureStorageService.loadSignature(member);

		return imageBytes;
	}

	public int checkValidMember(String id, String phone) {
		if (memberRepository.existsByLoginId(id)) {
			throw new BadRequestException("이미 존재하는 ID입니다.");
		}

		if (memberRepository.existsByPhone(phone)) {
			throw new BadRequestException("이미 존재하는 전화번호입니다.");
		}

		return 0;
	}

}
