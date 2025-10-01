package com.goodee.corpdesk.stats;

import com.goodee.corpdesk.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.time.LocalDate;
import java.util.List;

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
""")
	List<Long> countAllResider(LocalDate start, LocalDate end);


	@NativeQuery("""
	WITH sub AS (
		SELECT username, TIMESTAMPDIFF(YEAR, IFNULL(hire_date, NOW()), IFNULL(last_working_day, NOW())) AS diff
		FROM employee
	)
	SELECT COUNT(e.username) AS count
	FROM employee e
	JOIN sub s ON e.username = s.username
	GROUP BY diff
	ORDER BY diff ASC
""")
	List<Long> countAllServicePeriod();
}















