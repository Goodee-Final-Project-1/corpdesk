package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	// 출근기록 저장(신규/수정 공통 처리)
    @Transactional
    public Attendance save(Attendance attendance) {
        // 내부: JPA가 영속화/병합 처리 → insert/update 결정
        return attendanceRepository.save(attendance);
    }

    // 기간으로 출근기록 조회 (포함 범위: start ≤ x ≤ end)
    @Transactional(readOnly = true)
    public List<Attendance> getListByCheckInBetween(LocalDateTime start, LocalDateTime end) {
        // 내부: 메서드명 파싱 기반 쿼리 실행 → where check_in_datetime between ? and ?
        return attendanceRepository.findAllByCheckInDatetimeBetween(start, end);
    }

    // 특정 직원의 가장 최근 출근기록 1건
    @Transactional(readOnly = true)
    public Optional<Attendance> getLatestByEmployee(Long employeeId) {
        // 내부: order by check_in_datetime desc limit 1 자동 생성
        return attendanceRepository.findTop1ByEmployeeIdOrderByCheckInDatetimeDesc(employeeId);
    }

    // 특정 기간에 출근기록이 존재하는지 빠르게 확인
    @Transactional(readOnly = true)
    public boolean existsInPeriod(LocalDateTime start, LocalDateTime end) {
        // 내부: exists 쿼리로 최적화된 조회 수행
        return attendanceRepository.existsByCheckInDatetimeBetween(start, end);
    }

    // NULL 제외한 가장 이른 출근 시각
    @Transactional(readOnly = true)
    public Optional<LocalDateTime> getMinCheckInNotNull() {
        // 내부: select min(a.checkInDatetime) where not null → 없으면 null
        return Optional.ofNullable(attendanceRepository.findMinCheckInDatetimeNotNull());
    }

    // NULL 제외한 가장 늦은 출근 시각
    @Transactional(readOnly = true)
    public Optional<LocalDateTime> getMaxCheckInNotNull() {
        // 내부: select max(a.checkInDatetime) where not null → 없으면 null
        return Optional.ofNullable(attendanceRepository.findMaxCheckInDatetimeNotNull());
    }

    // 직원+기간 조건 조회(정렬: 오래된순)
    @Transactional(readOnly = true)
    public List<Attendance> getByEmployeeAndPeriod(Long employeeId, LocalDateTime start, LocalDateTime end) {
        // 내부: @Query JPQL 실행, 파라미터 바인딩으로 안전한 이름 매칭
        return attendanceRepository.findByEmployeeAndPeriod(employeeId, start, end);
    }

    // 화면/응답 편의를 위한 min/max 한 번에 가져오기
    @Transactional(readOnly = true)
    public MinMaxCheckIn getMinMaxCheckIn() {
        // 내부: 각각 Optional로 받아서 null-safe 구성
        LocalDateTime min = attendanceRepository.findMinCheckInDatetimeNotNull();
        LocalDateTime max = attendanceRepository.findMaxCheckInDatetimeNotNull();
        return new MinMaxCheckIn(min, max);
    }

    // 화면단/컨트롤러에서 쓰기 쉬운 최소/최대 값 전송용 단순 DTO
    public static record MinMaxCheckIn(LocalDateTime min, LocalDateTime max) {}

}
