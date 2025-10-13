package com.goodee.corpdesk.schedule.service;

import com.goodee.corpdesk.schedule.dto.DocumentDTO;
import com.goodee.corpdesk.schedule.dto.GeocodeBodyDTO;
import com.goodee.corpdesk.schedule.dto.ReqPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.dto.ResPersonalScheduleDTO;
import com.goodee.corpdesk.schedule.entity.PersonalSchedule;
import com.goodee.corpdesk.schedule.repository.PersonalScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PersonalScheduleService {

    @Autowired
    private PersonalScheduleRepository personalScheduleRepository;

    // WebClient Bean 주입
    @Autowired
    private WebClient webClient;

    @Value("${api.kakao.map.key}")
    private String kakaoMapKey;

    public ResPersonalScheduleDTO createSchedule(String username, ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        PersonalSchedule newSchedule = reqPersonalScheduleDTO.toEntity();
        newSchedule.setUsername(username);
        newSchedule.setModifiedBy(username);

        return personalScheduleRepository.save(newSchedule).toResPersonalScheduleDTO();

    }

    // username, useYn, (year, month)로 일정 데이터들 조회
    public List<ResPersonalScheduleDTO> getSchedules(String username, ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        return personalScheduleRepository.findPersonalScheduleByUsernameAndYearMonth(true
                                                                                    , username
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

    public ResPersonalScheduleDTO updateSchedule(String modifiedBy, Long personalScheduleId, ReqPersonalScheduleDTO reqPersonalScheduleDTO) {

        // id로 조회
        PersonalSchedule oldSchedule = personalScheduleRepository.findPersonalScheduleByUseYnAndPersonalScheduleId(true, personalScheduleId);

        // save
        oldSchedule.setModifiedBy(modifiedBy);
        oldSchedule.setScheduleName(reqPersonalScheduleDTO.getScheduleName());
        oldSchedule.setScheduleDateTime(reqPersonalScheduleDTO.getScheduleDateTime());
        oldSchedule.setContent(reqPersonalScheduleDTO.getContent());
        oldSchedule.setAddress(reqPersonalScheduleDTO.getAddress());

        return oldSchedule.toResPersonalScheduleDTO();

    }

    public ResPersonalScheduleDTO deleteSchedule(String modifiedBy, Long personalScheduleId) {

        // id로 조회
        PersonalSchedule oldSchedule = personalScheduleRepository.findPersonalScheduleByUseYnAndPersonalScheduleId(true, personalScheduleId);

        // delete
        oldSchedule.setModifiedBy(modifiedBy);
        oldSchedule.setUseYn(false);

        return oldSchedule.toResPersonalScheduleDTO();

    }

    public List<ResPersonalScheduleDTO> getSchedulesByDate(String username, LocalDateTime startOfDay, LocalDateTime endOfDay) {

        List<PersonalSchedule> schedules = personalScheduleRepository.findAllByUseYnAndUsernameAndScheduleDateTimeBetween(true,  username, startOfDay, endOfDay);

        if(schedules == null) return List.of();

        return schedules.stream().map(PersonalSchedule::toResPersonalScheduleDTO).toList();

    }

    // geocoding api 호출
    public List<ResPersonalScheduleDTO> bindGeocodesToSchedules(List<ResPersonalScheduleDTO> schedules) {

        for(ResPersonalScheduleDTO schedule : schedules) {

            if(schedule.getAddress() == null) continue;

            // api를 호출해서 주소의 위경도를 받아옴
            WebClient webClient = WebClient.create();
            Mono<GeocodeBodyDTO> geocodeMono = webClient.get()
                .uri("https://dapi.kakao.com/v2/local/search/address.json" +
                    "?query=" + schedule.getAddress())
                .header("Authorization", "KakaoAK " + kakaoMapKey)
                .retrieve()
                .bodyToMono(GeocodeBodyDTO.class);

            DocumentDTO geocodeInfo = geocodeMono.block().getDocuments().get(0);

            // schedule에 바인딩
            schedule.setLatitude(Double.parseDouble(geocodeInfo.getY()));
            schedule.setLongitude(Double.parseDouble(geocodeInfo.getX()));

        }

        return schedules;

    }

}
