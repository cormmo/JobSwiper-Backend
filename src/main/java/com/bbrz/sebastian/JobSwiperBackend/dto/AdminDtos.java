package com.bbrz.sebastian.JobSwiperBackend.dto;

import java.util.List;

public final class AdminDtos {
    private AdminDtos() {}
    public record OverviewResponse(long users, long activeJobOffers, long matches) {}

    public record ParticipantMatches(
            AuthDtos.UserResponse user,
            List<MatchDtos.MatchResponse> matches
    ) {}

    public record GroupedMatchSearchResponse(
            List<ParticipantMatches> employees,
            List<ParticipantMatches> employers
    ) {}
}
