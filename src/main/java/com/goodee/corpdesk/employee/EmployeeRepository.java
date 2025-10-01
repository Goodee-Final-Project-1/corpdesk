package com.goodee.corpdesk.employee;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goodee.corpdesk.approval.dto.ResApprovalDTO;

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

    @NativeQuery("""
        WITH
        	d AS (
        		SELECT department_id, department_name
        		FROM department
        		WHERE department_id = :departmentId
        ),
        	e AS (
        		SELECT *
        		FROM employee
        		WHERE use_yn = :useYn
        )
        SELECT
        	d.department_name AS department_name
        	, e.username AS username, e.name AS name
        	, p.position_name AS position_name
        	, ef.file_id AS file_id, ef.ori_name AS ori_name, ef.save_name AS save_name
        	, ef.extension AS extension
        FROM d
        JOIN e USING (department_id)
        JOIN `position` p USING (position_id)
        LEFT JOIN employee_file ef USING (username)
    """)
    public List<ResApprovalDTO> findEmployeeWithDeptAndPositionAndFile(@Param("departmentId") Integer departmentId, @Param("useYn") Boolean useYn);

    @Query("""
        SELECT e
        FROM Employee e
        WHERE
            e.useYn = :useYn
        	AND MONTH(e.hireDate) = :month
        	AND DAY(e.hireDate) = :day
    """)
    public List<Employee> findAllByHireDateMonthDay(@Param("useYn") Boolean useYn, @Param("month") Integer month, @Param("day") Integer day);

	boolean existsByUsername(String username);

	List<Employee> findAllByUseYnTrue();

	boolean existsByMobilePhone(String mobilePhone);

	Optional<Employee> findByUsername(String username);
	
	List<Employee> findByDepartmentId(Integer departmentId);
	
	@Modifying
	@Query("UPDATE Employee e SET e.departmentId = NULL, e.departmentName = NULL WHERE e.departmentId = :deptId")
	void clearDepartmentByDeptId(@Param("deptId") Integer deptId);

	List<Employee> findByDepartmentIdAndUseYnTrue(Integer departmentId);














	@Query("""
	SELECT new com.goodee.corpdesk.employee.EmployeeInfoDTO (
		e.username,
		e.name,
		e.hireDate,
		d.departmentName,
		p.positionName
	)
	FROM Employee e
	JOIN Department d ON e.departmentId = d.departmentId
	JOIN Position p ON e.positionId = p.positionId
	WHERE e.username = :username
""")
	Optional<EmployeeInfoDTO> findByIdWithDept(String username);
}
