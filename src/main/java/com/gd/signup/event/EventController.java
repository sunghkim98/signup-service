package com.gd.signup.event;

import com.gd.signup.event.dto.EventCreateDto;
import com.gd.signup.event.dto.EventEditDto;
import com.gd.signup.event.dto.EventHostResponseDto;
import com.gd.signup.event.dto.EventResponseDto;
import com.gd.signup.regist.dto.RegisteredEventDto;
import com.gd.signup.regist.dto.RegistrationResponseDto;
import com.gd.signup.response.Response;
import com.gd.signup.response.ResponseData;
import com.gd.signup.response.ResponseMsg;
import com.gd.signup.response.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
	private final EventService eventService;

	/**
	 * 이벤트 생성
	 **/
	@PostMapping
	public ResponseEntity eventCreate(@AuthenticationPrincipal(expression = "id") Long memberId,
									  @RequestBody EventCreateDto eventCreateDto) {
		System.out.println("memberId = " + memberId);
		System.out.println("mode = " + eventCreateDto.getCollectedFields());
		eventService.createByHostId(memberId, eventCreateDto);

		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.CREATE_EVENT), HttpStatus.OK);
	}

	/**
	 * 이벤트 참여
	 **/
	@PostMapping("/join/{eventId}")
	public ResponseEntity eventJoin(@AuthenticationPrincipal(expression = "id") Long memberId,
									@PathVariable Long eventId) {
		System.out.println("memberId = " + memberId);
		eventService.registAtEvent(memberId, eventId);

		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.REGIST_EVENT), HttpStatus.OK);
	}

	/**
	 * 모든 이벤트 기록 호출 (호스트용)
	 **/
	@GetMapping("/host")
	public ResponseEntity getAllEvents(@AuthenticationPrincipal(expression = "id") Long memberId) {
		System.out.println("memberId = " + memberId);
		List<EventHostResponseDto> res = eventService.getAllEvents(memberId);

		return new ResponseEntity(ResponseData.res(StatusCode.OK, ResponseMsg.GET_DATA_SUCCESS, res), HttpStatus.OK);
	}


	/**
	 * 모든 이벤트 기록 호출 (참가자용)
	 **/
	@GetMapping("/register")
	public ResponseEntity getMyRegisteredEvents(
			@AuthenticationPrincipal(expression = "id") Long memberId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Page<RegisteredEventDto> data = eventService.getRegisteredEvents(memberId, page, size);

		return new ResponseEntity(ResponseData.res(StatusCode.OK, ResponseMsg.GET_DATA_SUCCESS, data), HttpStatus.OK);
	}

	/**
	 * 7일 이내에 시작하는 이벤트 호출 (참가자용)
	 **/
	@GetMapping("/register/upcoming")
	public ResponseEntity getUpcomingEvents(@AuthenticationPrincipal(expression = "id") Long memberId) {
		List<EventResponseDto> data = eventService.getUpcomingRegisterEventsWithinWeek(memberId);

		return new ResponseEntity(ResponseData.res(StatusCode.OK, ResponseMsg.GET_DATA_SUCCESS, data), HttpStatus.OK);
	}

	/**
	 * 이벤트에 참여한 승인 완료한 명단
	 **/
	@GetMapping("/approve/{eventId}")
	public ResponseEntity getAllApproveList(@AuthenticationPrincipal(expression = "id") Long memberId,
											@PathVariable Long eventId) {
		System.out.println("memberId = " + memberId);
		List<RegistrationResponseDto> res = eventService.getAllApproveList(memberId, eventId);

		return new ResponseEntity(ResponseData.res(StatusCode.OK, ResponseMsg.GET_DATA_SUCCESS, res), HttpStatus.OK);
	}

	/**
	 * 이벤트에 참여한 승인 대기중인 명단
	 **/
	@GetMapping("/pending/{eventId}")
	public ResponseEntity getAllPendingList(@AuthenticationPrincipal(expression = "id") Long memberId,
											@PathVariable Long eventId) {
		System.out.println("memberId = " + memberId);
		List<RegistrationResponseDto> res = eventService.getAllPendingList(memberId, eventId);

		return new ResponseEntity(ResponseData.res(StatusCode.OK, ResponseMsg.GET_DATA_SUCCESS, res), HttpStatus.OK);
	}

	/**
	 * 이벤트 내용 수정
	 **/
	@PatchMapping("/edit/{eventId}")
	public ResponseEntity eventEdit(@AuthenticationPrincipal(expression = "id") Long memberId,
									@RequestBody EventEditDto eventEditDto) {
		eventService.editEvent(memberId, eventEditDto);

		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.CHANGE_EVENT_STATUS), HttpStatus.OK);
	}

	/**
	 * 이벤트 종료
	 **/
	@PatchMapping("/close")
	public ResponseEntity eventClose(@AuthenticationPrincipal(expression = "id") Long memberId,
									@PathVariable Long eventId) {
		eventService.closeEvent(memberId, eventId);

		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.CHANGE_EVENT_STATUS), HttpStatus.OK);
	}

	/**
	 * 이벤트 수동 시작
	 **/
	@PatchMapping("/running")
	public ResponseEntity eventRunning(@AuthenticationPrincipal(expression = "id") Long memberId,
									@PathVariable Long eventId) {
		eventService.runningEvent(memberId, eventId);

		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.CHANGE_EVENT_STATUS), HttpStatus.OK);
	}

	/**
	 * 이벤트 삭제
	 **/
	@DeleteMapping("/{eventId}")
	public ResponseEntity eventDelete(@AuthenticationPrincipal(expression = "id") Long memberId,
									  @PathVariable Long eventId) {
		eventService.deleteEvent(memberId, eventId);

		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_EVENT), HttpStatus.OK);
	}

}
