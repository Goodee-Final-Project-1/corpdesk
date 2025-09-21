package com.goodee.corpdesk.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @NativeQuery("""
        WITH e AS (
        	SELECT *
        	FROM employee
        	WHERE username = :username
        )
        SELECT
            e.username AS username, e.position_id AS position_id, e.department_id AS department_id, e.name AS name
            , d.department_name AS department_name, d.parent_department_id AS parent_department_id
            , p.position_name AS position_name, p.parent_position_id AS parent_position_id
        FROM e
        JOIN department d USING (department_id)
        JOIN `position` p USING (position_id);
    """)
    public ResEmployeeDTO findEmployeeWithDeptAndPosition(@Param("username") String username);

	boolean existsByUsername(String username);

	List<Employee> findAllByUseYnTrue();

	boolean existsByMobilePhone(String mobilePhone);

	Optional<Employee> findByUsername(String username);
}
