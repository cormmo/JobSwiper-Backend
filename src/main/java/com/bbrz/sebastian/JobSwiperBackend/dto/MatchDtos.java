package com.bbrz.sebastian.JobSwiperBackend.dto;

import com.bbrz.sebastian.JobSwiperBackend.enums.MatchStatus;
import com.bbrz.sebastian.JobSwiperBackend.model.JobMatch;

import java.time.Instant;

public final class MatchDtos {
    private MatchDtos() {}

    public record MatchResponse(Long id, AuthDtos.UserResponse employee, AuthDtos.UserResponse employer,
                                JobDtos.JobOfferResponse jobOffer, Instant createdAt, MatchStatus status) {
        public static MatchResponse from(JobMatch match) {
            return new MatchResponse(match.getId(), AuthDtos.UserResponse.from(match.getEmployee()),
                    AuthDtos.UserResponse.from(match.getEmployer()), JobDtos.JobOfferResponse.from(match.getJobOffer()),
                    match.getCreatedAt(), match.getStatus());
        }
    }
}
