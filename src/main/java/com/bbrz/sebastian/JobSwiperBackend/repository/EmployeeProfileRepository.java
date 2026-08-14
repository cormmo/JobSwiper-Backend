package com.bbrz.sebastian.JobSwiperBackend.repository;

import com.bbrz.sebastian.JobSwiperBackend.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    Optional<EmployeeProfile> findByUserId(Long userId);
    Page<EmployeeProfile> findByUserActiveTrue(Pageable pageable);

    @Query(value = """
            select distinct profile from EmployeeProfile profile left join profile.skills skill
            where profile.user.active = true
              and (:location is null or lower(profile.location) like lower(concat('%', :location, '%')))
              and (:skill is null or lower(skill) = lower(:skill))
            """, countQuery = """
            select count(distinct profile) from EmployeeProfile profile left join profile.skills skill
            where profile.user.active = true
              and (:location is null or lower(profile.location) like lower(concat('%', :location, '%')))
              and (:skill is null or lower(skill) = lower(:skill))
            """)
    Page<EmployeeProfile> searchActive(String location, String skill, Pageable pageable);
}
