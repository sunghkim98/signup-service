package com.gd.signup.jwt;

import com.gd.signup.exception.customException.BadRequestException;
import com.gd.signup.member.MemberRepository;
import com.gd.signup.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;

	public JwtAuthFilter(JwtUtil jwtUtil, MemberRepository memberRepository) {
		this.jwtUtil = jwtUtil;
		this.memberRepository = memberRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		String header = req.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			try {
				Jws<Claims> jws = jwtUtil.parse(token);

				Long memberId = Long.valueOf(jws.getBody().getSubject());
				Member m = memberRepository.findById(memberId).orElse(null);

				if (m != null) {
					UserPrincipal principal = new UserPrincipal(m.getId(), m.getLoginId(),
							List.of(new SimpleGrantedAuthority("ROLE_USER")));
					UsernamePasswordAuthenticationToken auth =
							new UsernamePasswordAuthenticationToken(principal, null, principal.authorities());
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} catch (JwtException | IllegalArgumentException e) {
				SecurityContextHolder.clearContext();
				//Todo 깔끔하게 처리해라
			}
		}
		chain.doFilter(req, res);
	}
}

