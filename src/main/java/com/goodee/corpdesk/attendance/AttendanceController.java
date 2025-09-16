package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;

	// NULL 제외 최솟값/최댓값을 한 번에 조회
    @GetMapping("/minmax")
    public ResponseEntity<MinMaxResponse> getMinMax() {
        // 서비스에서 최소/최대 체크인 시각을 조회
        AttendanceService.MinMaxCheckIn mm = attendanceService.getMinMaxCheckIn();
        // 응답 DTO로 변환(값이 null일 수 있음)
        return ResponseEntity.ok(new MinMaxResponse(mm.min(), mm.max()));
    }

    // 기간으로 출근 기록 조회(start ≤ x ≤ end)
    @GetMapping("/list")
    public ResponseEntity<List<Attendance>> getListByPeriod(
            // ISO-8601 문자열을 LocalDateTime으로 변환(예: 2025-09-01T09:00:00)
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        // 서비스 호출로 기간 내 출근 기록을 조회
        List<Attendance> list = attendanceService.getListByCheckInBetween(start, end);
        // 결과 리스트를 그대로 반환
        return ResponseEntity.ok(list);
    }

    // 특정 직원의 가장 최근 출근 기록 1건
    @GetMapping("/employee/{employeeId}/latest")
    public ResponseEntity<Attendance> getLatestByEmployee(@PathVariable("employeeId") Long employeeId) {
        // 서비스 호출로 최신 출근 기록 Optional 조회
        Optional<Attendance> opt = attendanceService.getLatestByEmployee(employeeId);
        // 존재하면 200, 없으면 204 No Content 반환
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    // 특정 기간에 출근 기록 존재 여부만 빠르게 확인
    @GetMapping("/exists")
    public ResponseEntity<ExistsResponse> existsInPeriod(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        // 서비스 호출로 존재 여부 확인
        boolean exists = attendanceService.existsInPeriod(start, end);
        // 불리언 래핑해서 응답
        return ResponseEntity.ok(new ExistsResponse(exists));
    }

    // 출근 기록 저장(신규/수정 공용) - 학습용 단순 예시
    @PostMapping
    public ResponseEntity<Attendance> save(@RequestBody Attendance attendance) {
        // 서비스 호출로 저장(insert/update 결정)
        Attendance saved = attendanceService.save(attendance);
        // 저장 결과 반환
        return ResponseEntity.ok(saved);
    }

    // 화면/응답용 최소/최대 DTO
    public static record MinMaxResponse(LocalDateTime min, LocalDateTime max) {}

    // 존재 여부 응답 DTO
    public static record ExistsResponse(boolean exists) {}

}
