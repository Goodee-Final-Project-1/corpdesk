package com.goodee.corpdesk.attendance.service;

import com.goodee.corpdesk.attendance.DTO.ResAttendanceDTO;
import com.goodee.corpdesk.attendance.entity.Attendance;
import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import com.goodee.corpdesk.vacation.entity.VacationDetail;
import com.goodee.corpdesk.vacation.repository.VacationDetailRepository;
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
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final VacationDetailRepository vacationDetailRepository;

    /**
     * Save a new attendance record or update an existing one, while populating audit fields.
     *
     * The method sets `modifiedBy` to the currently authenticated user, sets `createdAt`
     * when inserting (attendanceId is null), and always updates `updatedAt` before persisting.
     *
     * @param attendance the Attendance entity to persist; its audit fields (`createdAt`, `updatedAt`, `modifiedBy`) will be updated
     * @return the persisted Attendance entity
     */
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
    
    
    /**
     * Retrieve an Attendance record by its identifier.
     *
     * @param id the identifier of the attendance record
     * @return the Attendance with the given id
     * @throws RuntimeException if no attendance record exists for the given id
     */
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
    }

    /**
     * Persist changes to the provided Attendance entity.
     *
     * Saves the given Attendance to the repository, applying updates when the entity already exists.
     */
    @Transactional
    public void updateAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    /**
     * Soft-delete the specified attendance records by marking them inactive.
     *
     * Each identified record will have its `useYn` set to `false`, its `modifiedBy`
     * set to the currently authenticated user, and its `updatedAt` set to the current time.
     *
     * @param username      the username requesting the deletion (not used to filter records)
     * @param attendanceIds list of attendance record IDs to mark inactive
     */
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

    /**
     * Retrieve active attendance records for the specified employee.
     *
     * @param username the employee's username
     * @return a list of Attendance entities for the given username where `useYn` is `true`
     */
    public List<Attendance> getAttendanceByUsername(String username) {
        return attendanceRepository.findByUsernameAndUseYn(username, true);
    }

    // 휴가/퇴근/출근/출근전 상태 조회
    /**
     * Determine the user's attendance status for today and populate a ResAttendanceDTO with relevant timestamps.
     *
     * <p>The returned DTO may have status set to "휴가", "퇴근", "출근", or "출근전" based on today's holiday/vacation checks
     * and the most recent attendance record; if no applicable record or status exists, an empty DTO is returned.</p>
     *
     * @param username the user identifier whose attendance is being evaluated
     * @param year     the year context (currently unused; intended for future/monthly statistics)
     * @param month    the month context (currently unused; intended for future/monthly statistics)
     * @return a ResAttendanceDTO populated with attendanceId, checkInDateTime, checkOutDateTime, oldestCheckInDateTime when available, and a status string describing today's attendance state
	 */
    public ResAttendanceDTO getAttendanceStatus(String username, String year, String month) {

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
            resAttendanceDTO.setStatus("휴가");

            return resAttendanceDTO;
        }

        // 0. 가장 최근의 출퇴근 내역
        Attendance attendance = attendanceRepository.findLatestAttendanceByUsername(username);

        if(attendance == null) return resAttendanceDTO;

        LocalDate checkInDate = attendance.getCheckInDateTime().toLocalDate();
        LocalDate checkOutDate = attendance.getCheckOutDateTime().toLocalDate();
        if(checkInDate != null) {
            // 2. 퇴근
            // 가장 최근의 출퇴근 내역 조회(a)
            // a.출근일시 != null & a.퇴근일시 != null & a.출근일시 == 오늘날짜
            if(checkOutDate != null && checkInDate.isEqual(today)) resAttendanceDTO.setStatus("퇴근");
            // 3. 출근
            // 가장 최근의 출퇴근 내역 조회(a)
            // a.출근일시 != null & a.퇴근일시 == null
            else if(checkOutDate == null) resAttendanceDTO.setStatus("출근");
            // 4. 출근전
            // 가장 최근의 출퇴근 내역 조회(a)
            // a.출근일시 != 오늘날짜 & a.퇴근일시 != null
            else if(!checkInDate.isEqual(today)) resAttendanceDTO.setStatus("출근전");
        }

        resAttendanceDTO.setAttendanceId(attendance.getAttendanceId());
        resAttendanceDTO.setCheckInDateTime(attendance.getCheckInDateTime());
        resAttendanceDTO.setCheckOutDateTime(attendance.getCheckOutDateTime());

        Timestamp oldestCheckInTime = attendanceRepository.findOldestAttendanceByUsername(username);

        // 기존 출근 기록이 아예 없는 경우가 있을 수 있음 (첫출근 + 출근 버튼 누르기 전)
        if(oldestCheckInTime != null) {
            resAttendanceDTO.setOldestCheckInDateTime(
               LocalDateTime.ofInstant(attendanceRepository.findOldestAttendanceByUsername(username)
                                                            .toInstant(), ZoneId.of("Asia/Seoul"))
            );
        }

        // TODO 더 뿌려야 할 데이터
        // TODO (해당 월의/전체)지각, 조퇴, 결근 횟수
        // TODO (해당 월의/전체)근무 시간
        // TODO 출근일, 출근시간, 퇴근일, 퇴근시간, 근무상태(출근/퇴근/출근전/휴가)

        return resAttendanceDTO;
    }
    */
}

