package com.goodee.corpdesk.attendance.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import com.goodee.corpdesk.vacation.repository.VacationDetailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final VacationDetailRepository vacationDetailRepository;

    @Value("${attendance.work-hour.start}")
    private String workHourStart;
    @Value("${attendance.work-hour.end}")
    private String workHourEnd;

    // insert / update 통합 처리
    @Transactional
    public Attendance saveOrUpdateAttendance(Attendance attendance) {
        // 로그인 사용자 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();

        // modifiedBy 세팅
        attendance.setModifiedBy(currentUser);

        // createdAt 세팅 (insert인 경우만)
        if (attendance.getAttendanceId() == null) {
            attendance.setCreatedAt(LocalDateTime.now());
        }

        // updatedAt 항상 세팅
        attendance.setUpdatedAt(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }
    
    
    // ID로 출퇴근 기록 조회
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
    }

    // Attendance 엔티티 저장/업데이트
    @Transactional
    public void updateAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    // 소프트 삭제
    @Transactional
    public void deleteAttendances(String username, java.util.List<Long> attendanceIds) {
        attendanceIds.forEach(id -> {
            Attendance att = getAttendanceById(id);
            att.setUseYn(false);
            att.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            att.setUpdatedAt(LocalDateTime.now());
            attendanceRepository.save(att);
        });
    }

    // 특정 직원 출퇴근 내역 조회
    public List<Attendance> getAttendanceByUsername(String username) {
        return attendanceRepository.findByUsernameAndUseYn(username, true);
    }

    // 특정 직원의 현재 상태(휴가/퇴근/출근/출근전) & 출퇴근id & 출퇴근일시 조회
    public ResAttendanceDTO getCurrentAttendance(String username) throws Exception {

        ResAttendanceDTO  resAttendanceDTO = new ResAttendanceDTO();

        // 0. 오늘은 공휴일이거나 휴일인가?
        Boolean isHoliday = false;
        // 1) 오늘이 공휴일인지 구함
        // TODO 특일 api 사용 가능해지면 구현

        // 2) 오늘이 휴일인지 구함
        LocalDate today = LocalDate.now();
        if(today.getDayOfWeek().getValue() >= 6) isHoliday = true;

        // 1. 휴가
        // !공휴일 & !휴일 & 연차사용일
        VacationDetail vacationDetail = vacationDetailRepository.findVacationDetailOnDate(username, today);
        if(!isHoliday && vacationDetail != null) {
            resAttendanceDTO.setWorkStatus("휴가");

            return resAttendanceDTO;
        }

        // 0. 가장 최근의 출퇴근 내역
        Attendance latestAttendance = attendanceRepository.findLatestAttendanceByUsername(username);
        
        // 가장 최근의 출퇴근 내역이 없으면 첫출근이므로 "출근전" 처리
        if(latestAttendance == null) {
            resAttendanceDTO.setWorkStatus("출근전");
            
            return resAttendanceDTO;
        }

        LocalDate checkInDate = latestAttendance.getCheckInDateTime().toLocalDate();
        LocalDate checkOutDate = latestAttendance.getCheckOutDateTime().toLocalDate();

        if(checkInDate != null) {
            // 2. 출근전
            // 가장 최근의 출퇴근 내역 조회(a)
            // a.출근일시 != 오늘날짜 & a.퇴근일시 != null
            if(!checkInDate.isEqual(today)) resAttendanceDTO.setWorkStatus("출근전");
            // 3. 퇴근, 출근 -> 가장 최근의 출퇴근 내역에 있는 workStatus값 사용
            resAttendanceDTO.setWorkStatus(latestAttendance.getWorkStatus());

            /*
            // 2. 퇴근
            // 가장 최근의 출퇴근 내역 조회(a)
            // a.출근일시 != null & a.퇴근일시 != null & a.출근일시 == 오늘날짜
            else if(checkOutDate != null && checkInDate.isEqual(today)) resAttendanceDTO.setWorkStatus("퇴근");
            // 3. 출근
            // 가장 최근의 출퇴근 내역 조회(a)
            // a.출근일시 != null & a.퇴근일시 == null
            else if(checkOutDate == null) resAttendanceDTO.setWorkStatus("출근");
            */
        }

        resAttendanceDTO.setAttendanceId(latestAttendance.getAttendanceId());
        resAttendanceDTO.setCheckInDateTime(latestAttendance.getCheckInDateTime());
        resAttendanceDTO.setCheckOutDateTime(latestAttendance.getCheckOutDateTime());

        return resAttendanceDTO;
    }

    // 특정 직원의 출퇴근 기록 중 출근 날짜가 가장 오래된 년도 조회
    public LocalDateTime getOldestCheckInDateTime(String username) throws Exception {
        return attendanceRepository.findOldestAttendanceByUsername(username);
    }

    // 특정 직원의 지각 횟수 조회
//    public ResAttendanceDTO getLateAttendance(LocalTime time, String username, String year, String month) {
//        if()
//
//        return null;
//    }

}
