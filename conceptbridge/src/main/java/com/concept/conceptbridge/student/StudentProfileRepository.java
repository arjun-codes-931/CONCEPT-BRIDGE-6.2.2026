package com.concept.conceptbridge.student;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUserUsername(String username);

    boolean existsByUserId(Long userId);

    StudentProfile findByUser_Id(Long userId);

    // ✅ CORRECT: These methods use "branch" (matches StudentProfile entity field)
    List<StudentProfile> findByBranch(String branch);

    List<StudentProfile> findByBranchAndSemester(String branch, Integer semester);

    // ❌ REMOVE any methods with "department" in the name
    // ❌ Remove: findByDepartmentAndSemester
    // ❌ Remove: countByDepartmentAndSemester
}