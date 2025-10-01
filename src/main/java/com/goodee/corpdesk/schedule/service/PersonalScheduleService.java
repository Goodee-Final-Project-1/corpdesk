package com.goodee.corpdesk.schedule.service;

import com.goodee.corpdesk.schedule.dto.ReqPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import com.goodee.corpdesk.schedule.repository.PersonalScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PersonalScheduleService {

    @Autowired
    private PersonalScheduleRepository personalScheduleRepository;

    public ResPersonalScheduleDTO createSchedule(ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        PersonalSchedule newSchedule = reqPersonalScheduleDTO.toEntity();

        return personalScheduleRepository.save(newSchedule).toResPersonalScheduleDTO();

    }

}
