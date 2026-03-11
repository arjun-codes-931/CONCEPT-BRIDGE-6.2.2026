package com.concept.conceptbridge.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {

    // ✅ Keep only one method for each query

    // Find by username
    Optional<TeacherProfile> findByUserUsername(String username);

    // Check if user ID exists
    boolean existsByUserId(Long userId);

    // Find by employee ID
    Optional<TeacherProfile> findByEmployeeId(String employeeId);

    // Find by department
    List<TeacherProfile> findByDepartment(String department);

    // ✅ Choose ONE of these - remove duplicates:
    // Option 1: Keep this one (uses property path)
    Optional<TeacherProfile> findByUser_Id(Long userId);
    // Find teacher by username from associated user

    // Alternative if the above doesn't work
    Optional<TeacherProfile> findByUser_Username(String username);
    // ❌ Remove this duplicate:
    // Optional<TeacherProfile> findByUserId(Long userId);

    // Optional custom queries (remove if not needed)
    @Query("SELECT tp.id FROM TeacherProfile tp WHERE tp.user.username = :username")
    Optional<Long> findTeacherIdByUsername(@Param("username") String username);

    @Query("SELECT tp.user.id FROM TeacherProfile tp WHERE tp.user.username = :username")
    Optional<Long> findUserIdByUsername(@Param("username") String username);
}