package com.bbrz.sebastian.JobSwiperBackend.dto;

import com.bbrz.sebastian.JobSwiperBackend.model.EmployeeProfile;
import com.bbrz.sebastian.JobSwiperBackend.model.EmployerProfile;
import com.bbrz.sebastian.JobSwiperBackend.model.WorkExperience;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class ProfileDtos {
    private ProfileDtos() {}

    public record WorkExperienceRequest(
            @NotBlank @Size(max = 120) String company,
            @NotBlank @Size(max = 120) String position,
            @NotNull @PastOrPresent LocalDate startDate,
            @PastOrPresent LocalDate endDate,
            @Size(max = 2000) String description,
            @Min(0) int sortOrder) {
        @AssertTrue(message = "endDate must not be before startDate")
        public boolean isDateRangeValid() {
            return startDate == null || endDate == null || !endDate.isBefore(startDate);
        }

        public WorkExperience toEntity() {
            return new WorkExperience(company, position, startDate, endDate, description, sortOrder);
        }
    }

    public record WorkExperienceResponse(Long id, String company, String position, LocalDate startDate,
                                         LocalDate endDate, String description, int sortOrder) {
        static WorkExperienceResponse from(WorkExperience item) {
            return new WorkExperienceResponse(item.getId(), item.getCompany(), item.getPosition(),
                    item.getStartDate(), item.getEndDate(), item.getDescription(), item.getSortOrder());
        }
    }

    public record EmployeeProfileRequest(
            @NotBlank @Size(max = 80) String firstName,
            @NotBlank @Size(max = 80) String lastName,
            @Size(max = 40) String phone,
            @Size(max = 120) String location,
            @Size(max = 2000) String summary,
            @Size(max = 160) String desiredPosition,
            @NotNull @Size(max = 30) List<@NotBlank @Size(max = 80) String> skills,
            @NotNull @Size(max = 30) List<@Valid WorkExperienceRequest> workExperience) {}

    public record EmployeeProfileResponse(Long id, AuthDtos.UserResponse user, String firstName, String lastName,
                                          String phone, String location, String summary, String desiredPosition,
                                          List<String> skills, List<WorkExperienceResponse> workExperience,
                                          Instant lastUpdated) {
        public static EmployeeProfileResponse from(EmployeeProfile profile) {
            return new EmployeeProfileResponse(profile.getId(), AuthDtos.UserResponse.from(profile.getUser()),
                    profile.getFirstName(), profile.getLastName(), profile.getPhone(), profile.getLocation(),
                    profile.getSummary(), profile.getDesiredPosition(), profile.getSkills(),
                    profile.getWorkExperience().stream().map(WorkExperienceResponse::from).toList(),
                    profile.getLastUpdated());
        }
    }

    public record EmployerProfileRequest(
            @NotBlank @Size(max = 160) String companyName,
            @Size(max = 3000) String description,
            @Size(max = 120) String location,
            @NotBlank @Email @Size(max = 254) String contactEmail) {}

    public record EmployerProfileResponse(Long id, AuthDtos.UserResponse user, String companyName,
                                          String description, String location, String contactEmail,
                                          Instant lastUpdated) {
        public static EmployerProfileResponse from(EmployerProfile profile) {
            return new EmployerProfileResponse(profile.getId(), AuthDtos.UserResponse.from(profile.getUser()),
                    profile.getCompanyName(), profile.getDescription(), profile.getLocation(),
                    profile.getContactEmail(), profile.getLastUpdated());
        }
    }
}
