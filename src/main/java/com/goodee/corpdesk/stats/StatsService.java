package com.goodee.corpdesk.stats;

import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

	private final StatsRepository statsRepository;
	private final DepartmentRepository departmentRepository;
	private final PositionRepository positionRepository;

	public List<Department> getDepartmentList() {
		return departmentRepository.findAll();
	}

	public List<Position> getPositionList() {
		return positionRepository.findAll();
	}

	// FIXME: 조건 조회를 하려면 분기가 4개
	public Map<String, Object> list(LocalDate start, LocalDate end,
			Integer departmentId, Integer positionId) {
		Map<String, Object> map = new HashMap<>();

		List<String> months = statsRepository.findAllDateMonth(start, end);

		List<Long> joiner = statsRepository.countAllJoiner(start, end);
		List<Long> resigner = statsRepository.countAllResigner(start, end);
		List<Long> resider = statsRepository.countAllResider(start, end);
		
		map.put("months", months);
		map.put("joiner", joiner);
		map.put("resigner", resigner);
		map.put("resider", resider);

		return map;
	}
}
