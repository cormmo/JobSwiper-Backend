package com.bbrz.sebastian.JobSwiperBackend.repository;

import com.bbrz.sebastian.JobSwiperBackend.enums.SwipeDirection;
import com.bbrz.sebastian.JobSwiperBackend.model.SwipeDecision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SwipeDecisionRepository extends JpaRepository<SwipeDecision, Long> {
    Optional<SwipeDecision> findByActorIdAndEmployeeIdAndEmployerIdAndJobOfferIdAndDirection(
            Long actorId, Long employeeId, Long employerId, Long jobOfferId, SwipeDirection direction);

    Optional<SwipeDecision> findByEmployeeIdAndEmployerIdAndJobOfferIdAndDirection(
            Long employeeId, Long employerId, Long jobOfferId, SwipeDirection direction);
}
