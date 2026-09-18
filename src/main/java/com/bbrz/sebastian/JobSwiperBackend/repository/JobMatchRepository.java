package com.bbrz.sebastian.JobSwiperBackend.repository;

import com.bbrz.sebastian.JobSwiperBackend.model.JobMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobMatchRepository extends JpaRepository<JobMatch, Long> {
    boolean existsByEmployeeIdAndEmployerIdAndJobOfferId(Long employeeId, Long employerId, Long jobOfferId);
    Page<JobMatch> findByEmployeeIdOrEmployerId(Long employeeId, Long employerId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "employee", "employer", "jobOffer", "jobOffer.employerProfile"
    })

    @Query("""
            select match from JobMatch match
            join match.employee employee
            where locate(:query, lower(employee.username)) > 0
                or locate(:query, lower(employee.email)) > 0
            order by employee.username, match.createdAt desc, match.id desc
            """)
    List<JobMatch> findMatchesForEmployees(@Param("query") String query);

    @EntityGraph(attributePaths = {
            "employee", "employer", "jobOffer", "jobOffer.employerProfile"
    })
@Query("""
          select match from JobMatch match
          join match.employer employer
          where locate(:query, lower(employer.username)) > 0
            or locate(:query, lower(employer.email)) > 0
          order by employer.username, match.createdAt desc, match.id desc
""")
    List<JobMatch> findMatchesForEmployers(@Param("query") String query);
}
