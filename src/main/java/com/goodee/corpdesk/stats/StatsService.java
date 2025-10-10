package com.goodee.corpdesk.stats;

import com.goodee.corpdesk.department.entity.Department;
import com.goodee.corpdesk.department.repository.DepartmentRepository;
import com.goodee.corpdesk.position.entity.Position;
import com.goodee.corpdesk.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

	public Map<String, List> list2() {
		List<Map<String, Long>> list = statsRepository.countAllServicePeriod();

		List<String> diff = new ArrayList<>() {{
			add("1년 미만");
			add("1 ~ 3년");
			add("3 ~ 5년");
			add("5년 이상");
		}};

		List<Long> count = new ArrayList<>() {{
			add(0L);
			add(0L);
			add(0L);
			add(0L);
		}};

		list.forEach(map -> {
			if(map.get("diff") < 1) count.set(0, count.get(0) + map.get("count"));
			else if(map.get("diff") < 3) count.set(1, count.get(1) + map.get("count"));
			else if(map.get("diff") < 5) count.set(2, count.get(2) + map.get("count"));
			else count.set(3, count.get(3) + map.get("count"));
		});

		return new HashMap<String, List>() {{
			put("diff", diff);
			put("count", count);
		}};
	}

	public Map<String, List> list3() {
		List<Map<String, Long>> list = statsRepository.countAllAge();

		List<String> diff = new ArrayList<>() {{
			add("20 ~ 29세");
			add("30 ~ 39세");
			add("40 ~ 49세");
			add("50세 이상");
		}};

		List<Long> count = new ArrayList<>() {{
			add(0L);
			add(0L);
			add(0L);
			add(0L);
		}};

		list.forEach(map -> {
			if(map.get("diff") >= 20 && map.get("diff") < 30) count.set(0, count.get(0) + map.get("count"));
			else if(map.get("diff") >= 30 && map.get("diff") < 40) count.set(1, count.get(1) + map.get("count"));
			else if(map.get("diff") >= 40 && map.get("diff") < 50) count.set(2, count.get(2) + map.get("count"));
			else if(map.get("diff") >= 50) count.set(3, count.get(3) + map.get("count"));
		});

		return new HashMap<String, List>() {{
			put("diff", diff);
			put("count", count);
		}};
	}
}
