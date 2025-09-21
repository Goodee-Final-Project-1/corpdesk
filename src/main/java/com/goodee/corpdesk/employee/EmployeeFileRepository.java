package com.goodee.corpdesk.employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeFileRepository extends JpaRepository<EmployeeFile, Long> {
    // ⭐ username으로 EmployeeFile을 찾습니다.
    Optional<EmployeeFile> findByUsername(String username);
}
