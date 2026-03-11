package com.concept.conceptbridge.material;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StudentPDFProgressRepository extends JpaRepository<StudentPDFProgress, Long> {
    Optional<StudentPDFProgress> findByStudentIdAndPdfId(Long studentId, Long pdfId);
}