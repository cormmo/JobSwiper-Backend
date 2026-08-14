package com.bbrz.sebastian.JobSwiperBackend.repository;

import com.bbrz.sebastian.JobSwiperBackend.model.JobMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobMatchRepository extends JpaRepository<JobMatch, Long> {
    boolean existsByEmployeeIdAndEmployerIdAndJobOfferId(Long employeeId, Long employerId, Long jobOfferId);
    Page<JobMatch> findByEmployeeIdOrEmployerId(Long employeeId, Long employerId, Pageable pageable);
}
