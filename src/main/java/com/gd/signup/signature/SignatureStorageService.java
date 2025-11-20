package com.gd.signup.signature;

import com.gd.signup.exception.customException.BadRequestException;
import com.gd.signup.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignatureStorageService {

	@Value("${app.storage.signature-dir}")
	private String signatureBaseDir;

	/**
	 * base64 PNG 문자열을 받아서 서버 디스크에 저장하고,
	 * DB에 넣을 상대 경로를 리턴한다.
	 *
	 * 예) /signatures/123/20251118_112301_abcd.png
	 */
	public String saveSignature(Long memberId, String base64Png) {
		try {
			byte[] bytes = Base64.getDecoder().decode(base64Png);

			Path memberDir = Paths.get(signatureBaseDir, String.valueOf(memberId));
			if (!Files.exists(memberDir)) {
				Files.createDirectories(memberDir);
			}

			String filename = LocalDateTime.now()
					.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
					+ "_" + UUID.randomUUID() + ".png";

			Path filePath = memberDir.resolve(filename);

			Files.write(filePath, bytes, StandardOpenOption.CREATE_NEW);

			String relativePath = memberId + "/" + filename;
			return relativePath;

		} catch (IllegalArgumentException e) {
			// base64 decode 실패
			throw new IllegalArgumentException("잘못된 서명 이미지 데이터입니다.", e);
		} catch (IOException e) {
			// 파일 저장 실패
			throw new RuntimeException("서명 이미지를 저장하는 중 오류가 발생했습니다.", e);
		}
	}

	public byte[] loadSignature(Member member) {
		String signPath = member.getSign();

		if (signPath == null || signPath.isBlank()) {
			throw new BadRequestException("서명 이미지 경로가 없습니다.");
		}

		String relative = signPath;
		Path filePath = Paths.get(signatureBaseDir, relative);

		if (!Files.exists(filePath)) {
			throw new BadRequestException("서명 이미지 파일이 존재하지 않습니다: " + filePath);
		}

		try {
			return Files.readAllBytes(filePath);
		} catch (IOException e) {
			throw new RuntimeException("서명 이미지를 읽는 중 오류가 발생했습니다.", e);
		}
	}
}
