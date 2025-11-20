package com.gd.signup.jwt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.POST, "/auth/login/kakao","/auth/login", "/auth/refresh").permitAll()
				.anyRequest().authenticated()
		);
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling(e -> e
				.authenticationEntryPoint((req, res, ex) -> {
					res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					res.setContentType("application/json;charset=UTF-8");
					res.getWriter().write("{\"code\":401,\"message\":\"토큰이 유효하지 않음\"}");
				})
//				.accessDeniedHandler((req, res, ex) -> {
//					res.setStatus(HttpServletResponse.SC_FORBIDDEN);
//					res.setContentType("application/json;charset=UTF-8");
//					res.getWriter().write("{\"code\":403,\"message\":\"Forbidden\"}");
//				})
		);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
