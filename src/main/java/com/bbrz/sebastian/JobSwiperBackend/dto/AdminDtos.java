package com.bbrz.sebastian.JobSwiperBackend.dto;

public final class AdminDtos {
    private AdminDtos() {}
    public record OverviewResponse(long users, long activeJobOffers, long matches) {}
}
