package com.bbrz.sebastian.JobSwiperBackend.repository;

import com.bbrz.sebastian.JobSwiperBackend.model.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    Page<JobOffer> findByActiveTrue(Pageable pageable);
    Page<JobOffer> findByActiveTrueAndCategoryIgnoreCase(String category, Pageable pageable);
    Page<JobOffer> findByEmployerProfileUserId(Long userId, Pageable pageable);
    long countByActiveTrue();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select job from JobOffer job where job.id = :id")
    Optional<JobOffer> findByIdForUpdate(Long id);

    @Query("""
            select job from JobOffer job
            where job.active = true
              and (:category is null or lower(job.category) = lower(:category))
              and (:location is null or lower(job.location) like lower(concat('%', :location, '%')))
            """)
    Page<JobOffer> searchActive(String category, String location, Pageable pageable);
}
