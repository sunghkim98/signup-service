package com.gd.signup;

import com.gd.signup.event.Enum.ApprovalMode;
import com.gd.signup.event.Enum.CollectedField;
import com.gd.signup.event.EventRepository;
import com.gd.signup.event.entity.Event;
import com.gd.signup.member.MemberRepository;
import com.gd.signup.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
                "spring.datasource.url=${TEST_DB_URL}",
                "spring.datasource.username=${TEST_DB_USERNAME}",
                "spring.datasource.password=${TEST_DB_PASSWORD}",
                "spring.test.database.replace=NONE",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.jpa.properties.hibernate.format_sql=false",
                "spring.jpa.properties.hibernate.show_sql=false",
                "logging.level.org.hibernate.SQL=debug",
                "spring.jpa.open-in-view=false",
                "spring.sql.init.mode=never",
                "app.jwt.secret=${APP_JWT_SECRET}",
                "app.jwt.access-exp-seconds=900",
                "app.jwt.refresh-exp-seconds=1209600",
                "app.qr.payload-base=http://192.168.35.55:8080",
                "app.storage.signature-dir=/Users/kimseonghun/signature",
                "spring.security.filter.dispatch=true",
                "server.port=8080"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DummyDataSeederTest {

        private static final int MEMBER_COUNT = 5;
        private static final int EVENTS_PER_MEMBER = 30;

        @Autowired
        private MemberRepository memberRepository;

        @Autowired
        private EventRepository eventRepository;

        @Test
        @DisplayName("시작 시 멤버 5명과 각 멤버별 30개의 이벤트를 생성한다")
        void seedMembersAndEvents() {
                List<Member> members = createMembers();
                createEventsForMembers(members);

                assertThat(memberRepository.count()).isGreaterThanOrEqualTo(MEMBER_COUNT);
                assertThat(eventRepository.count()).isGreaterThanOrEqualTo((long) MEMBER_COUNT * EVENTS_PER_MEMBER);
        }

        private List<Member> createMembers() {
                List<Member> members = new ArrayList<>();
                for (int i = 1; i <= MEMBER_COUNT; i++) {
                        Member member = new Member(
                                        "dummy_user_" + i,
                                        "TEST_PROVIDER",
                                        "provider-user-" + i
                        );
                        member.changeName("Dummy User " + i);
                        member.changeEmail("dummy" + i + "@example.com");
                        member.changePhone("010-0000-00" + String.format("%02d", i));
                        member.changeGender(i % 2);
                        member.changeBirthDate("1990-01-0" + i);
                        members.add(memberRepository.save(member));
                }
                return members;
        }

        private void createEventsForMembers(List<Member> members) {
                Set<CollectedField> requiredFields = EnumSet.of(CollectedField.NAME, CollectedField.PHONE);
                for (Member member : members) {
                        for (int i = 1; i <= EVENTS_PER_MEMBER; i++) {
                                Date startTime = Date.from(Instant.now().plus(Duration.ofDays(i)));
                                Date endTime = Date.from(startTime.toInstant().plus(Duration.ofHours(2)));
                                Event event = new Event(
                                                member,
                                                "Demo Event " + i + " for " + member.getLoginId(),
                                                "Online",
                                                100L,
                                                startTime,
                                                endTime,
                                                ApprovalMode.AUTO,
                                                requiredFields
                                );
                                eventRepository.save(event);
                        }
                }
        }
}
