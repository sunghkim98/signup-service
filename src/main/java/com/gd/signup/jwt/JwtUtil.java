package com.gd.signup.jwt;

import com.gd.signup.exception.customException.JwtInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// JwtUtil.java
@Component
public class JwtUtil {
	private final Key key;
	private final long accessExpMillis;
	private final long refreshExpMillis;

	public JwtUtil(@Value("${app.jwt.secret}") String secret,
				   @Value("${app.jwt.access-exp-seconds}") long accessExpSeconds,
				   @Value("${app.jwt.refresh-exp-seconds}") long refreshExpSeconds) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.accessExpMillis = accessExpSeconds * 1000;
		this.refreshExpMillis = refreshExpSeconds * 1000;
	}

	public String createAccessToken(Long memberId, String loginId) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + accessExpMillis);
		return Jwts.builder()
				.setSubject(String.valueOf(memberId))
				.claim("loginId", loginId)
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public String createRefreshToken(Long memberId) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + refreshExpMillis);
		return Jwts.builder()
				.setSubject(String.valueOf(memberId))
				.claim("typ", "refresh")
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public Jws<Claims> parse(String token) {
//		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.setAllowedClockSkewSeconds(60)      // 시계 오차 허용 (권장)
					// .requireIssuer("makeco")          // 발급자 고정하면 활성화
					// .requireAudience("app-client")     // 대상 고정하면 활성화
					.build()
					.parseClaimsJws(token);               // 서명 + exp 검증
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			throw new JwtInvalidException("토큰 만료");
		} catch (io.jsonwebtoken.security.SignatureException e) {
			throw new JwtInvalidException("서명 불일치");
		} catch (io.jsonwebtoken.JwtException e) {
			throw new JwtInvalidException("토큰이 유효하지 않음");
		}
	}

	public Long getMemberId(String token) {
		return Long.valueOf(parse(token).getBody().getSubject());
	}

	public boolean isRefresh(String token) {
		Object typ = parse(token).getBody().get("typ");
		return "refresh".equals(typ);
	}
}

