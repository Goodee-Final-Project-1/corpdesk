package com.goodee.corpdesk.attendance;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

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
    public void deleteAttendances(String username, List<Long> attendanceIds) {
        List<Attendance> records = attendanceRepository.findAllById(attendanceIds);

        for (Attendance att : records) {
            if (!att.getUsername().equals(username)) {
                throw new IllegalArgumentException("다른 직원의 출퇴근 기록은 삭제할 수 없습니다. ID=" + att.getAttendanceId());
            }
            att.setUseYn(false); // soft delete
        }

        attendanceRepository.saveAll(records);
    }

    // 특정 직원 출퇴근 내역 조회
    public List<Attendance> getAttendanceByUsername(String username) {
        return attendanceRepository.findByUsernameAndUseYn(username, true);
    }
}
