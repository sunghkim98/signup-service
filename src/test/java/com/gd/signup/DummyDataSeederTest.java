package com.gd.signup;

import com.gd.signup.event.EventRepository;
import com.gd.signup.init.DummyDataInitializer;
import com.gd.signup.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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

        @Autowired
        private MemberRepository memberRepository;

        @Autowired
        private EventRepository eventRepository;

        @Test
        @DisplayName("시작 시 멤버 5명과 각 멤버별 30개의 이벤트가 준비된다")
        void seedMembersAndEvents() {
                assertThat(memberRepository.count())
                                .as("멤버 수는 최소 %s명 이상", DummyDataInitializer.DUMMY_MEMBER_COUNT)
                                .isGreaterThanOrEqualTo(DummyDataInitializer.DUMMY_MEMBER_COUNT);

                assertThat(eventRepository.count())
                                .as("이벤트 수는 최소 %s개 이상", (long) DummyDataInitializer.DUMMY_MEMBER_COUNT * DummyDataInitializer.DUMMY_EVENTS_PER_MEMBER)
                                .isGreaterThanOrEqualTo(
                                                (long) DummyDataInitializer.DUMMY_MEMBER_COUNT * DummyDataInitializer.DUMMY_EVENTS_PER_MEMBER
                                );
        }
}
