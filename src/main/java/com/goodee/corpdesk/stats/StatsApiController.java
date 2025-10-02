package com.goodee.corpdesk.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsApiController {

	private final StatsService statsService;

	@PostMapping
	public Map<String, Object> list(@RequestBody Map<String, Object> payload) {
		LocalDate end = LocalDate.now();
		LocalDate start = end.minusYears(1);
		if (payload.get("start") != null && !payload.get("start").equals("")) {
			start = LocalDate.parse((String) payload.get("start"));
		}
		if (payload.get("end") != null && !payload.get("end").equals("")) {
			end = LocalDate.parse((String) payload.get("end"));
		}

		Integer departmentId = (Integer) payload.get("departmentId");
		Integer positionId = (Integer) payload.get("positionId");

		return statsService.list(start, end, departmentId, positionId);
	}
}
