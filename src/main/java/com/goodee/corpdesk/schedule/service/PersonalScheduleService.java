package com.goodee.corpdesk.schedule.service;

import com.goodee.corpdesk.schedule.dto.ReqPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import com.goodee.corpdesk.schedule.repository.PersonalScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PersonalScheduleService {

    @Autowired
    private PersonalScheduleRepository personalScheduleRepository;

    public ResPersonalScheduleDTO createSchedule(ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        PersonalSchedule newSchedule = reqPersonalScheduleDTO.toEntity();

        return personalScheduleRepository.save(newSchedule).toResPersonalScheduleDTO();

    }

    // username, useYn, (year, month)로 일정 데이터들 조회
    public List<ResPersonalScheduleDTO> getSchedules(ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        return personalScheduleRepository.findPersonalScheduleByUsernameAndYearMonth(true
                                                                                    , reqPersonalScheduleDTO.getUsername()
                                                                                    , reqPersonalScheduleDTO.getYear()
                                                                                    , reqPersonalScheduleDTO.getMonth());
        
    }

    public List<Integer> getYearRangeByUsername(String username) {

        // 유저의 가장 오래된 일정 year 반환
        Integer oldestYear = personalScheduleRepository.findOldestScheduleYearByUsername(true, username);

        // year ~ 오늘로 List 생성 (유저의 가장 오래된 일정 year가 없다면 오늘 날짜만 있는 List 리턴
        int currentYear = LocalDate.now().getYear();

        if(oldestYear == null) return List.of(currentYear);

        List<Integer> years = new ArrayList<>();
        for(int year = oldestYear; year <= currentYear; year++){
            years.add(year);
        }

        return years;
        
    }

    public ResPersonalScheduleDTO getScheduleById(Long personalScheduleId) {

        return personalScheduleRepository.findPersonalScheduleByUseYnAndPersonalScheduleId(true, personalScheduleId).toResPersonalScheduleDTO();

    }

    public ResPersonalScheduleDTO updateSchedule(Long personalScheduleId, ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        // id로 조회
        PersonalSchedule oldSchedule = personalScheduleRepository.findPersonalScheduleByUseYnAndPersonalScheduleId(true, personalScheduleId);

        // save
        oldSchedule.setScheduleName(reqPersonalScheduleDTO.getScheduleName());
        oldSchedule.setScheduleDateTime(reqPersonalScheduleDTO.getScheduleDateTime());
        oldSchedule.setContent(reqPersonalScheduleDTO.getContent());
        oldSchedule.setAddress(reqPersonalScheduleDTO.getAddress());

        return oldSchedule.toResPersonalScheduleDTO();

    }

    public ResPersonalScheduleDTO deleteSchedule(Long personalScheduleId) {

        // id로 조회
        PersonalSchedule oldSchedule = personalScheduleRepository.findPersonalScheduleByUseYnAndPersonalScheduleId(true, personalScheduleId);

        // delete
        oldSchedule.setUseYn(false);

        return oldSchedule.toResPersonalScheduleDTO();

    }

    public List<ResPersonalScheduleDTO> getSchedulesByDate(String username, LocalDate date) {
        return null;
    }

}
