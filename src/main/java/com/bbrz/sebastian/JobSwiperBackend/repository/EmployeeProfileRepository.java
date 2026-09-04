package com.bbrz.sebastian.JobSwiperBackend.repository;

import com.bbrz.sebastian.JobSwiperBackend.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository for accessing employee profile data.
 *
 * <p>Provides methods for finding profiles by user, active status,
 * location and skills.</p>
 */
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    /**
     * Finds an employee profile by user ID.
     *
     * @param userId ID of the user
     * @return matching employee profile if found
     */
    Optional<EmployeeProfile> findByUserId(Long userId);

    /**
     * Returns all profiles belonging to active users.
     *
     * @param pageable pagination information
     * @return paginated active employee profiles
     */
    Page<EmployeeProfile> findByUserActiveTrue(Pageable pageable);

    /**
     * Searches active employee profiles by location and skill.
     *
     * @param location optional location filter
     * @param skill optional skill filter
     * @param pageable pagination information
     * @return paginated matching employee profiles
     */
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
    Page<EmployeeProfile> searchActive(
            String location,
            String skill,
            Pageable pageable
    );
}
