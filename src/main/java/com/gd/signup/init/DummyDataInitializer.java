package com.gd.signup.init;

import com.gd.signup.event.Enum.ApprovalMode;
import com.gd.signup.event.Enum.CollectedField;
import com.gd.signup.event.EventRepository;
import com.gd.signup.event.entity.Event;
import com.gd.signup.member.MemberRepository;
import com.gd.signup.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
public class DummyDataInitializer implements ApplicationRunner {

        public static final int DUMMY_MEMBER_COUNT = 5;
        public static final int DUMMY_EVENTS_PER_MEMBER = 30;
        private static final String PROVIDER = "TEST_PROVIDER";

        private final MemberRepository memberRepository;
        private final EventRepository eventRepository;

        @Override
        public void run(ApplicationArguments args) {
                List<Member> members = prepareMembers();
                createEventsForMembers(members);
        }

        private List<Member> prepareMembers() {
                List<Member> members = new ArrayList<>();
                for (int i = 1; i <= DUMMY_MEMBER_COUNT; i++) {
                        final int memberIndex = i;
                        String providerUserId = "provider-user-" + memberIndex;
                        Member member = memberRepository.findByProviderAndProviderUserId(PROVIDER, providerUserId)
                                        .orElseGet(() -> memberRepository.save(buildMember(memberIndex)));
                        members.add(member);
                }
                return members;
        }

        private Member buildMember(int index) {
                Member member = new Member(
                                "dummy_user_" + index,
                                PROVIDER,
                                "provider-user-" + index
                );
                member.changeName("Dummy User " + index);
                member.changeEmail("dummy" + index + "@example.com");
                member.changePhone("010-0000-00" + String.format("%02d", index));
                member.changeGender(index % 2);
                member.changeBirthDate("1990-01-0" + index);
                return member;
        }

        private void createEventsForMembers(List<Member> members) {
                Set<CollectedField> requiredFields = EnumSet.of(CollectedField.NAME, CollectedField.PHONE);
                for (Member member : members) {
                        long existingCount = eventRepository.countByHost_Id(member.getId());
                        if (existingCount >= DUMMY_EVENTS_PER_MEMBER) {
                                continue;
                        }
                        for (int i = 1; i <= DUMMY_EVENTS_PER_MEMBER - existingCount; i++) {
                                long eventIndex = existingCount + i;
                                Date startTime = Date.from(Instant.now().plus(Duration.ofDays(eventIndex)));
                                Date endTime = Date.from(startTime.toInstant().plus(Duration.ofHours(2)));
                                Event event = new Event(
                                                member,
                                                "Demo Event " + eventIndex + " for " + member.getLoginId(),
                                                "Online",
                                                "Demo description for event " + eventIndex,
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
