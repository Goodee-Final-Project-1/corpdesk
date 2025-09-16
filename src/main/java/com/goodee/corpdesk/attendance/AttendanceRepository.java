package com.goodee.corpdesk.attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
	 // 기본 제공: save, findById, findAll, deleteById 등은 JpaRepository가 제공
    // 동작: 스프링 데이터 JPA가 메서드 시그니처를 분석해 구현체를 런타임에 생성

    // 기간으로 check-in 조회(포함 범위)
    List<Attendance> findAllByCheckInDatetimeBetween(LocalDateTime start, LocalDateTime end);
    // 동작: 메서드명 파싱 → where check_in_datetime between ? and ? 자동 생성

    // NULL 제외 최솟값
    @Query("select min(a.checkInDatetime) from Attendance a where a.checkInDatetime is not null")
    LocalDateTime findMinCheckInDatetimeNotNull();
    // 동작: JPQL 실행 → 결과가 없으면 null 반환

    // NULL 제외 최댓값
    @Query("select max(a.checkInDatetime) from Attendance a where a.checkInDatetime is not null")
    LocalDateTime findMaxCheckInDatetimeNotNull();
    // 동작: JPQL 실행 → 결과가 없으면 null 반환

    // 직원별 최근 한 건(내림차순 정렬 후 1건)
    Optional<Attendance> findTop1ByEmployeeIdOrderByCheckInDatetimeDesc(Long employeeId);
    // 동작: order by check_in_datetime desc limit 1 자동 생성

    // 특정 날짜 구간에 출근 기록이 존재하는지 여부
    boolean existsByCheckInDatetimeBetween(LocalDateTime start, LocalDateTime end);
    // 동작: select exists(...) 형태로 최적화 쿼리 생성

    // 기간 + 직원 조건 조회 예시(@Query로 명시)
    @Query("""
           select a
           from Attendance a
           where a.employeeId = :employeeId
             and a.checkInDatetime between :start and :end
           order by a.checkInDatetime asc
           """)
    List<Attendance> findByEmployeeAndPeriod(
            @Param("employeeId") Long employeeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    // 동작: JPQL에 바인딩 → 컴파일 옵션(-parameters) 없어도 안전
}

