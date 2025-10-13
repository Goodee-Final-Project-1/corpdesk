package com.goodee.corpdesk.stats;

import com.goodee.corpdesk.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface StatsRepository extends JpaRepository<Employee, String> {

	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start < :end
	)
	SELECT DATE_FORMAT(am.month_start, '%Y-%m') AS date
	FROM AllMonths am
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<String> findAllDateMonth(LocalDate start, LocalDate end);

	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start < :end
	)
	SELECT COUNT(e.hire_date) AS count
	FROM AllMonths am
	LEFT JOIN employee e ON DATE_FORMAT(am.month_start, '%Y-%m') = DATE_FORMAT(e.hire_date, '%Y-%m')
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Long> countAllJoiner(LocalDate start, LocalDate end);

	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start < :end
	)
	SELECT COUNT(e.last_working_day) AS count
	FROM AllMonths am
	LEFT JOIN employee e ON DATE_FORMAT(am.month_start, '%Y-%m') = DATE_FORMAT(e.last_working_day, '%Y-%m')
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Long> countAllResigner(LocalDate start, LocalDate end);

	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start < :end
	)
	SELECT COUNT(e.username) AS count
	FROM AllMonths am
	LEFT JOIN employee e ON DATE_FORMAT(am.month_start, '%Y-%m') >= DATE_FORMAT(e.hire_date, '%Y-%m') AND (e.last_working_day IS NULL OR DATE_FORMAT(am.month_start, '%Y-%m') <= DATE_FORMAT(e.last_working_day, '%Y-%m'))
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Long> countAllResider(LocalDate start, LocalDate end);


	@NativeQuery("""
	WITH sub AS (
		SELECT username, TIMESTAMPDIFF(YEAR, hire_date, IFNULL(last_working_day, :end)) AS diff
		FROM employee
	)
	SELECT diff, COUNT(diff) AS count
	FROM employee e
	JOIN sub s ON e.username = s.username
	WHERE e.hire_date < :end AND (e.last_working_day IS NULL OR e.last_working_day >= :start)
	GROUP BY diff
	HAVING diff IS NOT NULL
	ORDER BY diff ASC
""")
	List<Map<String, Long>> countAllServicePeriod(LocalDate start, LocalDate end);


	@NativeQuery("""
	WITH sub AS (
		SELECT username, TIMESTAMPDIFF(YEAR, birth_date, :end) AS diff
		FROM employee
	)
	SELECT diff, COUNT(diff) AS count
	FROM employee e
	JOIN sub s ON e.username = s.username
	WHERE e.hire_date < :end AND (e.last_working_day IS NULL OR e.last_working_day >= :start)
	GROUP BY diff
	HAVING diff IS NOT NULL
	ORDER BY diff ASC
""")
	List<Map<String, Long>> countAllAge(LocalDate start, LocalDate end);


	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start < :end
	)
	SELECT
		DATE_FORMAT(am.month_start, '%Y-%m') AS month,
		COUNT(CASE WHEN a.use_yn = true AND TIME(a.check_in_date_time) <= :workStartTime THEN 1 END) AS attended_count,
		COUNT(CASE WHEN a.use_yn = true AND TIME(a.check_in_date_time) > :workStartTime THEN 1 END) AS late_count,
		COUNT(CASE WHEN a.use_yn = true AND a.check_in_date_time IS NULL THEN 1 END) AS absent_count
	FROM AllMonths am
	LEFT JOIN attendance a
	ON DATE_FORMAT(am.month_start, '%Y-%m') = DATE_FORMAT(a.created_at, '%Y-%m')
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Map<String, Object>> countAllAttendance(LocalDate start, LocalDate end, LocalTime workStartTime);

	@NativeQuery("""
	WITH RECURSIVE AllMonths AS (
		SELECT DATE(:start) AS month_start
		UNION ALL
		SELECT month_start + INTERVAL 1 MONTH
		FROM AllMonths
		WHERE month_start < :end
	)
	SELECT
		DATE_FORMAT(am.month_start, '%Y-%m') AS month,
		COUNT(CASE WHEN a.use_yn = true AND TIMESTAMPDIFF(HOUR, a.check_in_date_time, a.check_out_date_time) = 8 THEN 1 END) AS fixed,
		COUNT(CASE WHEN a.use_yn = true AND TIMESTAMPDIFF(HOUR, a.check_in_date_time, a.check_out_date_time) > 8 THEN 1 END) AS overtime
	FROM AllMonths am
	LEFT JOIN attendance a
	ON DATE_FORMAT(am.month_start, '%Y-%m') = DATE_FORMAT(a.check_in_date_time, '%Y-%m')
	GROUP BY YEAR(am.month_start), MONTH(am.month_start)
	ORDER BY am.month_start
""")
	List<Map<String, Object>> countAllWorkHours(LocalDate start, LocalDate end);
}















