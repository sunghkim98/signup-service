package com.gd.signup.event;

import com.gd.signup.event.Enum.ApprovalMode;
import com.gd.signup.event.dto.EventCreateDto;
import com.gd.signup.event.dto.EventEditDto;
import com.gd.signup.event.dto.EventHostResponseDto;
import com.gd.signup.event.dto.EventHostDetailResponseDto;
import com.gd.signup.event.dto.EventResponseDto;
import com.gd.signup.event.dto.ParticipantDto;
import com.gd.signup.event.entity.Event;
import com.gd.signup.event.qr.QrLinks;
import com.gd.signup.event.qr.QrService;
import com.gd.signup.exception.customException.BadRequestException;
import com.gd.signup.regist.Enum.RegistrationStatus;
import com.gd.signup.regist.RegistrationRepository;
import com.gd.signup.regist.dto.RegisteredEventDto;
import com.gd.signup.regist.dto.RegistrationResponseDto;
import com.gd.signup.regist.entity.Registration;
import com.gd.signup.exception.customException.NotFoundException;
import com.gd.signup.member.MemberRepository;
import com.gd.signup.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {
	private final EventRepository eventRepository;
	private final MemberRepository memberRepository;
	private final RegistrationRepository registrationRepository;
	private final QrService qrService;

	@Transactional
	public Long createByHostId(Long hostId, EventCreateDto dto) {
		Member member = memberRepository.findById(hostId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));

		Event event = new Event(
                                member,
                                dto.getEventName(),
                                dto.getPlace(),
                                dto.getDescription(),
                                dto.getCapacity(),
                                dto.getStartTime(),
                                dto.getEndTime(),
				dto.getMode(),
				dto.getCollectedFields()
		);
		eventRepository.save(event);

		QrLinks qrLinks = qrService.issueForEvent(event.getId());
		event.inputQrlink(qrLinks);

		System.out.println("======== createByHostId ========");
		System.out.println(event.getId());
		System.out.println(qrLinks.getInviteUrl());
		System.out.println(qrLinks.getAttendanceUrl());

		return event.getId();
	}

	@Transactional
        public void registAtEvent(Long memberId, Long eventId) {
                Event event = eventRepository.findById(eventId)
                                .orElseThrow(() -> new NotFoundException("이벤트가 존재하지 않습니다."));

		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다."));

		if (eventRepository.existsByHost_IdAndId(memberId, eventId) == true)
			throw new BadRequestException("이벤트 호스트 입니다.");

		if (registrationRepository.existsByMemberIdAndEventId(memberId, eventId) == true)
			throw new BadRequestException("이미 참여하였습니다.");

		Registration registration = new Registration(event, member);

		if (event.getApprovalMode() == ApprovalMode.MANUAL) {
			registration.updateStatus(RegistrationStatus.APPROVED);
		}

                registrationRepository.save(registration);
        }

        @Transactional
        public void cancelRegistration(Long memberId, Long eventId) {
                Registration registration = registrationRepository
                                .findByMember_IdAndEvent_Id(memberId, eventId)
                                .orElseThrow(() -> new NotFoundException("등록하신 이벤트 정보가 없습니다."));

                if (registration.getStatus() == RegistrationStatus.CANCELED) {
                        throw new BadRequestException("이미 취소된 이벤트입니다.");
                }

                registration.updateStatus(RegistrationStatus.CANCELED);
                registration.updateTime();
        }

	@Transactional
	public void runningEvent(Long memberId, Long eventId) {
		//TODO 코드 중복
		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException("이벤트가 존재하지 않습니다."));

		if (eventRepository.existsByHost_IdAndId(memberId, eventId) != true)
			throw new BadRequestException("이벤트 호스트가 아닙니다.");

		event.running();
	}

	@Transactional
	public void editEvent(Long memberId, EventEditDto eventEditDto) {
		Event event = eventRepository.findById(eventEditDto.getEventId())
				.orElseThrow(() -> new NotFoundException("이벤트가 존재하지 않습니다."));

		if (eventRepository.existsByHost_IdAndId(memberId, eventEditDto.getEventId()) != true)
			throw new BadRequestException("이벤트 호스트가 아닙니다.");

		event.edit(eventEditDto);
	}

	@Transactional
	public void closeEvent(Long memberId, Long eventId) {
		//TODO 코드 중복
		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException("이벤트가 존재하지 않습니다."));

		if (eventRepository.existsByHost_IdAndId(memberId, eventId) != true)
			throw new BadRequestException("이벤트 호스트가 아닙니다.");

		event.close();
	}

	@Transactional
	public void deleteEvent(Long memberId, Long eventId){
		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException("이벤트가 존재하지 않습니다."));

		if (eventRepository.existsByHost_IdAndId(memberId, eventId) == true) {
			event.softDelete(); // soft 삭제 (DB에는 남김)
			// 추가로 register 된 것들도 상태 남겨야할듯?
		} else {
			Registration registration = registrationRepository.findByEventId(eventId)
					.orElseThrow(() -> new NotFoundException("등록하신 이벤트 정보가 없습니다."));

			registrationRepository.deleteById(registration.getId()); // DB에서 찐삭제
		}
	}

	@Transactional(readOnly = true)
        public List<EventHostResponseDto> getAllEvents(Long memberId) {
                return eventRepository.findByHostIdAndDeletedFalseOrderByCreateTimeDesc(memberId).stream()
                                .map(event -> EventHostResponseDto.from(
                                                event,
                                                registrationRepository.countByEvent_IdAndStatus(event.getId(), RegistrationStatus.REQUESTED),
                                                registrationRepository.countByEvent_IdAndStatus(event.getId(), RegistrationStatus.APPROVED),
                                                registrationRepository.countByEvent_IdAndStatusNot(event.getId(), RegistrationStatus.CANCELED)
                                ))
                                .toList();
        }

//	@Transactional(readOnly = true)
//	public List<EventResponseDto> getAllRegisterEvents(Long memberId) {
//		List<Long> eventIds = registrationRepository.findEventIdsByMemberId(memberId);
//
//		if (eventIds.isEmpty())
//			return List.of();
//
//		List<Event> events = eventRepository.findAllById(eventIds);
//
//		return events.stream()
//				.map(EventResponseDto::from)
//				.toList();
//	}

	@Transactional(readOnly = true)
	public Page<RegisteredEventDto> getRegisteredEvents(Long memberId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return registrationRepository.findRegisteredEventsByMemberId(memberId, pageable);
	}

	@Transactional(readOnly = true)
	public List<EventResponseDto> getUpcomingRegisterEventsWithinWeek(Long memberId) {
		Date now = new Date(); // 현재 시각
		// 7일 뒤
		Date weekLater = Date.from(now.toInstant().plus(7, java.time.temporal.ChronoUnit.DAYS));

		List<Event> events = registrationRepository.findUpcomingEventsWithinWeek(memberId, now, weekLater);

		if (events.isEmpty()) {
			return List.of();
		}

		return events.stream()
				.map(EventResponseDto::from)
				.toList();
	}

	@Transactional(readOnly = true)
        public List<RegistrationResponseDto> getAllApproveList(Long memberId, Long eventId) {
                if (eventRepository.existsByIdAndDeletedFalse(eventId) != true)
                        throw new BadRequestException("이미 삭제된 이벤트 입니다.");

		if (eventRepository.existsByHost_IdAndId(memberId, eventId) != true)
			throw new BadRequestException("호스트가 아닙니다.");

		return registrationRepository
				.findByEvent_IdAndStatusOrderByCreateTimeDesc(eventId, RegistrationStatus.APPROVED)
				.stream()
				.map(RegistrationResponseDto::from)
				.toList();
	}

        public List<RegistrationResponseDto> getAllPendingList(Long memberId, Long eventId) {
                if (eventRepository.existsByHost_IdAndId(memberId, eventId) != true) {
                        throw new BadRequestException("호스트가 아닙니다.");
                }

		return registrationRepository
				.findByEvent_IdAndStatusOrderByCreateTimeDesc(eventId, RegistrationStatus.REQUESTED)
				.stream()
                                .map(RegistrationResponseDto::from)
                                .toList();
        }

        @Transactional(readOnly = true)
        public EventHostDetailResponseDto getEventDetail(Long memberId, Long eventId) {
                Event event = eventRepository.findById(eventId)
                                .orElseThrow(() -> new NotFoundException("이벤트가 존재하지 않습니다."));

                if (eventRepository.existsByHost_IdAndId(memberId, eventId) != true) {
                        throw new BadRequestException("호스트가 아닙니다.");
                }

                List<ParticipantDto> participants = registrationRepository
                                .findByEvent_IdAndStatusNotOrderByCreateTimeDesc(eventId, RegistrationStatus.CANCELED)
                                .stream()
                                .map(ParticipantDto::from)
                                .collect(Collectors.toList());

                long pendingCount = registrationRepository.countByEvent_IdAndStatus(eventId, RegistrationStatus.REQUESTED);
                long approvedCount = registrationRepository.countByEvent_IdAndStatus(eventId, RegistrationStatus.APPROVED);
                long totalCount = registrationRepository.countByEvent_IdAndStatusNot(eventId, RegistrationStatus.CANCELED);

                return EventHostDetailResponseDto.from(event, participants, pendingCount, approvedCount, totalCount);
        }

}
