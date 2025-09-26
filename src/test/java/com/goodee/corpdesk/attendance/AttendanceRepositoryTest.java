package com.goodee.corpdesk.attendance;

import com.goodee.corpdesk.attendance.repository.AttendanceRepository;
import com.goodee.corpdesk.attendance.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

@SpringBootTest
@Slf4j
class AttendanceRepositoryTest {
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Test
    void countByCheckInDateTimeTimeAfterAndUsername_Success() {
        // when
        long lateCount = attendanceRepository.countLateAttendanceByUsername(LocalTime.of(9, 0), "jung_frontend");

        // then
        log.warn("{}",lateCount);
    }
}