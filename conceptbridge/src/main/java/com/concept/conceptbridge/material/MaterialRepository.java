package com.concept.conceptbridge.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByBranchAndSemesterAndVisibility(String branch, Integer semester, String visibility);
    List<Material> findByUploadedByOrderByCreatedAtDesc(Long uploadedBy);

    @Query("SELECT m FROM Material m WHERE m.uploadedBy = :uploadedBy " +
            "AND (:branch IS NULL OR m.branch = :branch) " +
            "AND (:subject IS NULL OR m.subject = :subject)")
    List<Material> findByTeacherWithFilters(
            @Param("uploadedBy") Long uploadedBy,
            @Param("branch") String branch,
            @Param("subject") String subject);
}