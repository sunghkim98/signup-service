package com.gd.signup;

import com.gd.signup.member.MemberRepository;
import com.gd.signup.member.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {
	@Autowired
	MemberService memberService;

	@BeforeEach
	public void before() {
		System.out.println("\nTest Before");
	}

	@AfterEach
	public void after() {
		System.out.println("Test After");
	}

	@Test
	@DisplayName("memberService 테스트")
	public void test(){

	}
}
