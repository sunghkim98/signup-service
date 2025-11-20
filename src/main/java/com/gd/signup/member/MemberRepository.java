package com.gd.signup.member;

import com.gd.signup.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByLoginId(String loginId);
	boolean existsByPhone(String phone);
	boolean existsByLoginIdOrPhone(String loginId, String phone);
	boolean existsByLoginIdOrEmail(String loginId, String email);
	Optional<Member> findByLoginId(String loginId);
	Optional<Member> findByProviderAndProviderUserId(String provider, String providerUserId);
}
