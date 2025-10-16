package com.goodee.corpdesk.department.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goodee.corpdesk.department.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	Optional<Department> findByDepartmentName(String departmentName);
	 Optional<Department> findByDepartmentId(Integer departmentId);
	List<Department> findByParentDepartmentId(Integer id);
	
	List<Department> findByUseYnTrue(); 
	List<Department> findByParentDepartmentIdAndUseYnTrue(Integer parentDepartmentId);
	// 부서 ID로 조회하되 활성 부서만
    Optional<Department> findByDepartmentIdAndUseYnTrue(Integer departmentId);
	
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Department d set d.parentDepartmentId = :newParentId where d.parentDepartmentId = :oldParentId")
    int reparentChildren(@Param("oldParentId") Integer oldParentId,
                         @Param("newParentId") Integer newParentId);
	Optional<Department> findByDepartmentNameAndUseYnTrue(String deptName);

}
