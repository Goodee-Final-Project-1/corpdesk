package com.goodee.corpdesk.holiday.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.goodee.corpdesk.holiday.dto.HolidayDTO;
import com.goodee.corpdesk.holiday.dto.HolidayItemDTO;
import com.goodee.corpdesk.holiday.dto.HolidayResponseDTO;
import com.goodee.corpdesk.holiday.entity.Holiday;
import com.goodee.corpdesk.holiday.repository.HolidayRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    // WebClient Bean 주입
    @Autowired
    private WebClient webClient;

    @Value("${api.holiday.key}")
    private String key;

//    private ObjectMapper objectMapper = new ObjectMapper();

    private XmlMapper xmlMapper = new XmlMapper();
    /**
     * Fetches holiday data for May 2025 from the external government API, parses the XML response into Holiday entities, and logs the resulting list.
     *
     * <p>If XML parsing fails, the parsing error is logged and no Holiday entities are produced. Persistence of the parsed holidays is intentionally disabled in the current implementation.</p>
     */
    public void getHoliday(){

        Mono<List<Holiday>> holidayListMono = webClient.get()
            .uri("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo" +
                "?solYear=2025&solMonth=05&ServiceKey=" + key)
            .retrieve()
            // bodyToMono를 사용하여 전체 응답 객체(HolidayResponseDTO)를 받습니다.
            .bodyToMono(String.class)
            // map 연산자를 사용해 HolidayItemDTO 리스트를 추출합니다.
            .map(xmlString -> {
                log.warn("{}", xmlString);

                try {
                    // String을 xmlMapper를 사용하여 HolidayResponseDTO로 변환
                    HolidayResponseDTO responseDTO = xmlMapper.readValue(xmlString , HolidayResponseDTO.class);
                    return responseDTO.getBody().getItems().getItem();
                } catch (Exception e) {
                    log.error("xml 파싱 오류: {}", e.getMessage(), e);
                    return null; // 파싱 실패 시 null 반환
                }
            })
            // 추출한 HolidayItemDTO 리스트를 Holiday 엔티티 리스트로 변환합니다.
            .map(itemDTOs -> itemDTOs.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList()));

        // .block()을 사용하여 Mono가 완료될 때까지 기다리고 List<Holiday>를 반환받습니다.
        List<Holiday> holidays = holidayListMono.block();

        log.warn("{}", holidays);

        // 반환받은 List를 DB에 저장합니다.
//        if (holidays != null && !holidays.isEmpty()) {
//            holidayRepository.saveAll(holidays);
//            log.info("Holiday data saved successfully.");
//        }

    }

    /**
     * Convert a HolidayItemDTO into a Holiday entity.
     *
     * @param itemDTO the DTO containing holiday data; expects `locdate` as an integer in YYYYMMDD form, `dateName`, and `isHoliday`
     * @return a Holiday populated with `dateName`, `locdate` parsed to a LocalDate, and `isHoliday` taken as the first character
     */
    private Holiday convertToEntity(HolidayItemDTO itemDTO) {
        // 1. Integer(20250505)를 String으로 변환
        String dateString = String.valueOf(itemDTO.getLocdate());

        // 2. String(YYYYMMDD)을 LocalDate로 변환
        LocalDate locdate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));

        return Holiday.builder()
            .dateName(itemDTO.getDateName())
            .locdate(locdate)
            .isHoliday(itemDTO.getIsHoliday().charAt(0))
            .build();
    }

}
