package com.bbrz.sebastian.JobSwiperBackend.dto;

import com.bbrz.sebastian.JobSwiperBackend.model.JobOffer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public final class JobDtos {
    private JobDtos() {}

    public record JobOfferRequest(
            @NotBlank @Size(max = 160) String title,
            @NotBlank @Size(max = 5000) String description,
            @NotBlank @Size(max = 3000) String requirements,
            @NotBlank @Size(max = 120) String location,
            @NotBlank @Size(max = 100) String category) {}

    public record ActiveRequest(boolean active) {}

    public record JobOfferResponse(Long id, Long employerId, String companyName, String title, String description,
                                   String requirements, String location, String category, boolean active,
                                   Instant createdAt, Instant lastUpdated) {
        public static JobOfferResponse from(JobOffer job) {
            return new JobOfferResponse(job.getId(), job.getEmployerProfile().getUser().getId(),
                    job.getEmployerProfile().getCompanyName(), job.getTitle(), job.getDescription(),
                    job.getRequirements(), job.getLocation(), job.getCategory(), job.isActive(),
                    job.getCreatedAt(), job.getLastUpdated());
        }
    }
}
