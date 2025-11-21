package com.gd.signup.regist;

import com.gd.signup.event.entity.Event;
import com.gd.signup.regist.Enum.RegistrationStatus;
import com.gd.signup.regist.dto.RegisteredEventDto;
import com.gd.signup.regist.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
	boolean existsByMemberIdAndEventId(Long memberId, Long eventId);
	List<Registration> findByEvent_IdAndStatusOrderByCreateTimeDesc(Long eventId, RegistrationStatus status);
	Optional<Registration> findByEventId(Long Id);

	@Query("SELECT r.event.id FROM Registration r WHERE r.member.id = :memberId")
	List<Long> findEventIdsByMemberId(@Param("memberId") Long memberId);

	//앞으로 7일 이내에 시작하는 이벤트
	@Query("""	
           select r.event
           from Registration r
           where r.member.id = :memberId
             and r.event.startTime between :now and :weekLater
           """)
	List<Event> findUpcomingEventsWithinWeek(@Param("memberId") Long memberId, @Param("now") Date now, @Param("weekLater") Date weekLater);

	@Query(
			value = """
        select new com.gd.signup.regist.dto.RegisteredEventDto(
            e.id,
            e.name,
            e.place,
            e.description,
            e.capacity,
            e.startTime,
            e.endTime,
            e.status,
            e.qrLinks,
            r.status
        )
        from Registration r
        join r.event e
        where r.member.id = :memberId
        order by e.startTime desc
    """,
			countQuery = """
        select count(r)
        from Registration r
        where r.member.id = :memberId
    """
	)
	Page<RegisteredEventDto> findRegisteredEventsByMemberId(
			@Param("memberId") Long memberId,
			Pageable pageable
	);

}
