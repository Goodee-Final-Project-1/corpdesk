package com.goodee.corpdesk.attendance.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.goodee.corpdesk.approval.dto.ReqApprovalDTO;
import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import com.goodee.corpdesk.vacation.repository.VacationDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final VacationDetailRepository vacationDetailRepository;
    private final EmployeeRepository employeeRepository;

    @Value("${attendance.work-hour.start}")
    private String workStartHour;
    @Value("${attendance.work-hour.end}")
    private String workEndHour;

    /**
     * 새 출퇴근 내역을 생성하거나 기존 출퇴근 내역을 수정합니다.
     *
     * 현재 인증된 사용자로 'modifiedBy'를 설정합니다.
     * insert하는 경우 'createdAt'을 설정합니다.
     * update하는 경우 'updatedAt'을 설정합니다.
     *
     * @param attendance 생성 혹은 수정할 Attendance entity
     * @return 생성 혹은 수정된 Attendance entity
     */
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

    /**
     * 출퇴근pk로 출퇴근 내역을 조회합니다.
     *
     * @param id 출퇴근pk
     * @return id로 조회한 Attendance entity
     * @throws RuntimeException 만약 id로 조회할 출퇴근 내역이 없다면 발생합니다.
     */
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
    }

    /**
     * 제공된 Attendance 엔티티에 대한 변경 사항을 영속화합니다.
     *
     * 주어진 Attendance 엔티티를 레포지토리에 저장하며, 이미 존재하는 엔티티일 경우 업데이트를 적용합니다.
     *
     * @param attendance 저장할 Attendance 엔티티
     */
    public void updateAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    /**
     * 지정된 출퇴근 기록을 비활성(inactive)으로 표시하여 소프트 삭제합니다.
     *
     * 식별된 각 기록은 `useYn`이 `false`로 설정되고, `modifiedBy`는 현재 인증된 사용자로,
     * `updatedAt`은 현재 시간으로 설정됩니다.
     *
     * @param username      삭제를 요청하는 사용자 이름 (기록 필터링에는 사용되지 않습니다.)
     * @param attendanceIds 비활성으로 표시할 출퇴근 기록 ID 목록
     */
    public void deleteAttendances(String username, java.util.List<Long> attendanceIds) {
        attendanceIds.forEach(id -> {
            Attendance att = getAttendanceById(id);
            att.setUseYn(false);
            att.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            att.setUpdatedAt(LocalDateTime.now());
            attendanceRepository.save(att);
        });
    }

    /**
     * 지정된 직원의 활성(Active) 출퇴근 기록을 조회합니다.
     *
     * @param username 직원의 사용자 이름
     * @return 주어진 사용자 이름에 대해 `useYn`이 `true`인 Attendance 엔티티 목록
     */
    public List<Attendance> getAttendanceByUsername(String username) {
        return attendanceRepository.findByUsernameAndUseYn(username, true);
    }


    // =================== passing7by

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
        LocalDate checkOutDate = latestAttendance.getCheckOutDateTime() == null ?
                                    null : latestAttendance.getCheckOutDateTime().toLocalDate();

        if(checkInDate != null) {
            // 2. 출근전
            // 가장 최근의 출퇴근 내역 조회(a)
            // a.출근일시 != 오늘날짜 & a.퇴근일시 != null
            if(!checkInDate.isEqual(today)) resAttendanceDTO.setWorkStatus("출근전");
            // 3. 퇴근, 출근 -> 가장 최근의 출퇴근 내역에 있는 workStatus값 사용
            else resAttendanceDTO.setWorkStatus(latestAttendance.getWorkStatus());
        }

        resAttendanceDTO.setAttendanceId(latestAttendance.getAttendanceId());

        resAttendanceDTO.setCheckInDateTime(latestAttendance.getCheckInDateTime());
        resAttendanceDTO.setCheckOutDateTime(latestAttendance.getCheckOutDateTime());

        resAttendanceDTO.setCheckInDate(checkInDate);
        resAttendanceDTO.setCheckOutDate(checkOutDate);
        resAttendanceDTO.setCheckInTime(latestAttendance.getCheckInDateTime().toLocalTime());
        resAttendanceDTO.setCheckOutTime(latestAttendance.getCheckOutDateTime() == null ?
                                        null : latestAttendance.getCheckOutDateTime().toLocalTime());

        resAttendanceDTO.setToday(checkOutDate == null ? null : checkOutDate.isEqual(today));

        resAttendanceDTO.setAttendanceId(latestAttendance.getAttendanceId());

        return resAttendanceDTO;
    }

    public List<Integer> getYearRangeByEmployee(String username) throws Exception {
        Integer oldestYear = attendanceRepository.findOldestCheckInYearByUsername(username);
        int currentYear = LocalDate.now().getYear();

        if (oldestYear == null) {
            return List.of(currentYear);
        }

        List<Integer> years = new ArrayList<>();
        for (int year = oldestYear; year <= currentYear; year++) {
            years.add(year);
        }
        return years;
    }

    // 특정 직원의 지각, 조퇴, 결근 횟수를 한 번에 묶어서 반환하는 DTO를 생성합니다.
    // year와 month가 null이면 전체 횟수를 조회합니다.
    public ResAttendanceDTO getAttendanceCounts(String username, String year, String month) throws Exception {

        ResAttendanceDTO  resAttendanceDTO = new ResAttendanceDTO();
        LocalTime workStartTime = LocalTime.parse(workStartHour);
        LocalTime workEndTime = LocalTime.parse(workEndHour);

        resAttendanceDTO.setLateArrivalsCnt(
                attendanceRepository.countLateArrivalsByUsernameAndYearMonth(workStartTime, username,  year, month)
        );
        resAttendanceDTO.setAbsentDaysCnt(
                attendanceRepository.countAbsentDaysByUsernameAndYearMonth(username, year, month)
        );
        resAttendanceDTO.setEarlyLeavingsCnt(
                attendanceRepository.countEarlyLeavingsByUsernameAndYearMonth(workEndTime, username, year, month)
        );

        return resAttendanceDTO;

    }

    // 특정 직원의 총 근무 시간 및 총 근무 일수를 조회하여 반환합니다.
    // year와 month가 null이면 전체 횟수를 조회합니다.
    public ResAttendanceDTO getWorkSummary(String username, String year, String month) throws Exception {

       return attendanceRepository.findWorkSummaryByUsernameAndYearMonth(username, year, month, LocalDateTime.now());

    }

    // 특정 직원의 상세 출퇴근 기록 목록 (출근일, 출퇴근 시간, 근무 상태 등)을 조회합니다.
    // year와 month가 null이면 전체 횟수를 조회합니다.
    public List<ResAttendanceDTO> getAttendanceDetailList(String username, String year, String month) throws Exception {
        return attendanceRepository.findByUseYnAndUsernameAndYearMonth(username, year, month).stream().map(Attendance::toDTO).toList();
    }

    public ResAttendanceDTO checkIn(String username) throws Exception {
        // username, 출근일시, 상태, modifiedBy insert
        Attendance newAttd = new Attendance();
        newAttd.setUsername(username);
        newAttd.setCheckInDateTime(LocalDateTime.now());
        newAttd.setWorkStatus("출근");
        newAttd.setModifiedBy(username);

        return attendanceRepository.save(newAttd).toDTO();
    }

    public ResAttendanceDTO checkOut(Long attendanceId, String username) throws Exception {
        // 퇴근일시, 상태, modifiedBy update
        Attendance attd = attendanceRepository.findById(attendanceId).get();

        attd.setCheckOutDateTime(LocalDateTime.now());
        attd.setWorkStatus("퇴근");
        attd.setModifiedBy(username);

        return attendanceRepository.save(attd).toDTO();
    }

    public void recordAbsence() throws Exception {

        // 모든 직원의 데이터를 가져옴
        List<Employee> employees = employeeRepository.findAllByUseYnTrue();
        log.warn("employees: {}", employees);

        for (Employee e : employees) {
            // 해당 직원의 가장 최근 출퇴근 데이터 조회
            Attendance latestAttd = attendanceRepository.findLatestAttendanceByUsername(e.getUsername());
            log.warn("latestAttd: {}", latestAttd);

            if(latestAttd != null) {
                // 어제 < 입사일 -> 직원 등록은 되어있지만 아직 출근을 시작하지 않음
                if(LocalDate.now().minusDays(1).isBefore(e.getHireDate())) continue;

                // workStatus = '출근' -> 일하는 중이므로 그냥 넘어감
                // checkOutDateTime == 어제 날짜 and workStatus = '퇴근' -> 어제 퇴근했다면 어제 일했으므로 그냥 넘어감
                if ("출근".equals(latestAttd.getWorkStatus())) continue;
                else if(latestAttd.getCheckOutDateTime().isBefore(LocalDateTime.now())) continue;

                // 해당 직원의 모든 휴가 데이터 조회
                // 하나의 휴가데이터에라도 시작일과 종료일 사이(경계 포함)에 오늘이 있다면 그냥 넘어감
                VacationDetail vacationDetail = vacationDetailRepository.findVacationDetailOnDate(e.getUsername(), LocalDateTime.now().toLocalDate());

                if (vacationDetail != null) continue;
            }

            // 위에서 continue 되지 않았다면 work_status = "결근" and username = 유저ID 데이터 insert
            Attendance absenceData = new Attendance();
            absenceData.setUsername(e.getUsername());
            absenceData.setWorkStatus("결근");

            attendanceRepository.save(absenceData);

        }

    }

}

