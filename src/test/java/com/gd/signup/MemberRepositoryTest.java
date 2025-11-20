package com.gd.signup;

import com.gd.signup.member.MemberController;
import com.gd.signup.member.MemberRepository;
import com.gd.signup.member.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

//@SpringBootTest
//@ActiveProfiles("test") // 위의 application-test.properties 적용
//class SchemaSmokeTest {
//
//	@Autowired
//	JdbcTemplate jdbc;
//
//	@Test
//	void member_table_is_created() {
//		// PostgreSQL 전용: 테이블 존재하면 OID 문자열, 없으면 null
//		String regclass = jdbc.queryForObject(
//				"select to_regclass('public.member')", String.class);
//
//		assertThat(regclass)
//				.as("member 테이블이 생성되어야 함")
//				.isNotNull();
//	}
//}


@SpringBootTest
@Transactional
public class MemberRepositoryTest {
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	MemberRepository memberRepository;

	@BeforeEach
	public void before() {
		System.out.println("\nTest Before");
	}

	@AfterEach
	public void after() {
		System.out.println("Test After");
	}

	@Test
	@DisplayName("Test Case !!!")
	void memberTest() {
		System.out.println("==== MemberRepositoryTest 시작 ====");

		// given: 더미 데이터 생성
		Member dummy = new Member(
				"홍길동",
				"testId",
				"testPw",
				"basic",
				"010-1234-5678",
				"test@example.com",
				1,
				0
		);

		// when: 저장
		Member saved = memberRepository.save(dummy);
		System.out.println("저장된 Member ID: " + saved.getId());
		System.out.println("저장된 loginId: " + saved.getLoginId());
		System.out.println("저장된 email: " + saved.getEmail());
		System.out.println("createTime: " + saved.getCreateTime());
		System.out.println("updateTime: " + saved.getUpdateTime());

		// then: DB 전체 조회 후 출력
		List<Member> all = memberRepository.findAll();
		System.out.println("DB 전체 회원 수: " + all.size());
		for (Member m : all) {
			System.out.println(" - ID: " + m.getId() +
					", loginId: " + m.getLoginId() +
					", email: " + m.getEmail() +
					", createTime: " + m.getCreateTime() +
					", updateTime: " + m.getUpdateTime());
		}

		System.out.println("==== MemberRepositoryTest 종료 ====");
	}

	@Test
	@Disabled // 테스트 실행X
	void test3() {
		System.out.println("## test3 시작 ##");
		System.out.println();
	}

}
