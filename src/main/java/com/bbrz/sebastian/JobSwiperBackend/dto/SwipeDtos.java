package com.bbrz.sebastian.JobSwiperBackend.dto;

import com.bbrz.sebastian.JobSwiperBackend.enums.Decision;
import com.bbrz.sebastian.JobSwiperBackend.enums.SwipeDirection;
import com.bbrz.sebastian.JobSwiperBackend.model.SwipeDecision;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public final class SwipeDtos {
    private SwipeDtos() {}

    public record JobSwipeRequest(@NotNull Decision decision) {}
    public record ProfileSwipeRequest(@NotNull Long jobOfferId, @NotNull Decision decision) {}

    public record SwipeResponse(Long id, Long employeeId, Long employerId, Long jobOfferId,
                                SwipeDirection direction, Decision decision, Instant updatedAt,
                                boolean matchCreated) {
        public static SwipeResponse from(SwipeDecision swipe, boolean matchCreated) {
            return new SwipeResponse(swipe.getId(), swipe.getEmployee().getId(), swipe.getEmployer().getId(),
                    swipe.getJobOffer().getId(), swipe.getDirection(), swipe.getDecision(), swipe.getUpdatedAt(),
                    matchCreated);
        }
    }
}
