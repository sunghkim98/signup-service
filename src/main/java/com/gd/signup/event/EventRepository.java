package com.gd.signup.event;

import com.gd.signup.event.entity.Event;
import com.gd.signup.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findByHostIdAndDeletedFalseOrderByCreateTimeDesc(Long hostId);
	boolean existsByHost_IdAndId(Long memberId, Long eventId);
	boolean existsByIdAndDeletedFalse(Long id);
}
