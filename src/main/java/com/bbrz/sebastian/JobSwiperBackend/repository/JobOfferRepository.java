package com.bbrz.sebastian.JobSwiperBackend.repository;

import com.bbrz.sebastian.JobSwiperBackend.model.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import java.util.Optional;

/**
 * Repository for accessing job offer data.
 *
 * <p>Provides methods for filtering, searching and locking job offers.</p>
 */
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    /**
     * Returns all active job offers.
     *
     * @param pageable pagination information
     * @return paginated active job offers
     */
    Page<JobOffer> findByActiveTrue(Pageable pageable);

    /**
     * Returns active job offers of a specific category.
     *
     * @param category job category
     * @param pageable pagination information
     * @return paginated matching job offers
     */
    Page<JobOffer> findByActiveTrueAndCategoryIgnoreCase(
            String category,
            Pageable pageable
    );

    /**
     * Returns job offers created by a specific employer.
     *
     * @param userId employer user ID
     * @param pageable pagination information
     * @return paginated job offers
     */
    Page<JobOffer> findByEmployerProfileUserId(
            Long userId,
            Pageable pageable
    );

    /**
     * Counts all active job offers.
     *
     * @return number of active job offers
     */
    long countByActiveTrue();

    /**
     * Finds and locks a job offer for updating.
     *
     * @param id job offer ID
     * @return matching job offer if found
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select job from JobOffer job where job.id = :id")
    Optional<JobOffer> findByIdForUpdate(Long id);

    /**
     * Searches active job offers by category and location.
     *
     * @param category optional category filter
     * @param location optional location filter
     * @param pageable pagination information
     * @return paginated matching job offers
     */
    @Query("""
            select job from JobOffer job
            where job.active = true
              and (:category is null or lower(job.category) = lower(:category))
              and (:location is null or lower(job.location) like lower(concat('%', :location, '%')))
            """)
    Page<JobOffer> searchActive(
            String category,
            String location,
            Pageable pageable
    );
}
