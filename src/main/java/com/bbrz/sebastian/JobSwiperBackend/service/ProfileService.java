package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.dto.PageResponse;
import com.bbrz.sebastian.JobSwiperBackend.dto.ProfileDtos;
import com.bbrz.sebastian.JobSwiperBackend.enums.Role;
import com.bbrz.sebastian.JobSwiperBackend.exception.ResourceNotFoundException;
import com.bbrz.sebastian.JobSwiperBackend.model.EmployeeProfile;
import com.bbrz.sebastian.JobSwiperBackend.model.EmployerProfile;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.EmployeeProfileRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.EmployerProfileRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides operations for employee and employer profiles.
 *
 * <p>Supports profile updates, profile images and employee searches.</p>
 */
@Service
public class ProfileService {

    private final CurrentUserService currentUsers;
    private final EmployeeProfileRepository employees;
    private final EmployerProfileRepository employers;
    private final ProfileImageService images;

    /**
     * Creates the profile service.
     *
     * @param currentUsers service for accessing the current user
     * @param employees employee profile repository
     * @param employers employer profile repository
     * @param images service for validating profile images
     */
    public ProfileService(
            CurrentUserService currentUsers,
            EmployeeProfileRepository employees,
            EmployerProfileRepository employers,
            ProfileImageService images) {

        this.currentUsers = currentUsers;
        this.employees = employees;
        this.employers = employers;
        this.images = images;
    }

    /**
     * Creates or updates the current employee profile.
     *
     * @param auth current authentication
     * @param request employee profile data
     * @return updated employee profile
     */
    @Transactional
    public ProfileDtos.EmployeeProfileResponse updateEmployee(
            Authentication auth,
            ProfileDtos.EmployeeProfileRequest request) {

        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITNEHMER);

        EmployeeProfile profile = employees.findByUserId(user.getId())
                .orElseGet(() -> new EmployeeProfile(user));

        profile.update(
                request.firstName(),
                request.lastName(),
                request.phone(),
                request.location(),
                request.summary(),
                request.desiredPosition(),
                request.skills().stream()
                        .map(String::trim)
                        .distinct()
                        .toList(),
                request.workExperience().stream()
                        .map(ProfileDtos.WorkExperienceRequest::toEntity)
                        .toList()
        );

        return ProfileDtos.EmployeeProfileResponse.from(
                employees.save(profile)
        );
    }

    /**
     * Returns the current employee profile.
     *
     * @param auth current authentication
     * @return employee profile
     */
    @Transactional(readOnly = true)
    public ProfileDtos.EmployeeProfileResponse getOwnEmployee(Authentication auth) {
        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITNEHMER);

        return ProfileDtos.EmployeeProfileResponse.from(
                employees.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee profile has not been created"
                                ))
        );
    }

    /**
     * Uploads a profile picture for the current employee.
     *
     * @param auth current authentication
     * @param request image upload data
     * @return updated employee profile
     */
    @Transactional
    public ProfileDtos.EmployeeProfileResponse uploadEmployeeProfilePicture(
            Authentication auth,
            ProfileDtos.ImageUploadRequest request) {

        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITNEHMER);

        EmployeeProfile profile = employees.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee profile has not been created"
                        ));

        ProfileImageService.ValidatedImage image =
                images.validateAndDecode(request.imageBase64());

        profile.updateProfilePicture(
                image.data(),
                image.mediaType()
        );

        return ProfileDtos.EmployeeProfileResponse.from(
                employees.save(profile)
        );
    }

    /**
     * Returns an active employee profile by user ID.
     *
     * @param userId user ID
     * @return employee profile
     */
    @Transactional(readOnly = true)
    public ProfileDtos.EmployeeProfileResponse getEmployee(Long userId) {
        return ProfileDtos.EmployeeProfileResponse.from(
                employees.findByUserId(userId)
                        .filter(profile -> profile.getUser().isActive())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee profile not found"
                                ))
        );
    }

    /**
     * Searches active employee profiles.
     *
     * @param location optional location filter
     * @param skill optional skill filter
     * @param page page number
     * @param size page size
     * @return paginated employee profiles
     */
    @Transactional(readOnly = true)
    public PageResponse<ProfileDtos.EmployeeProfileResponse> listEmployees(
            String location,
            String skill,
            int page,
            int size) {

        return PageResponse.from(
                employees.searchActive(
                        normalize(location),
                        normalize(skill),
                        PageRequest.of(page, size)
                ),
                ProfileDtos.EmployeeProfileResponse::from
        );
    }

    /**
     * Creates or updates the current employer profile.
     *
     * @param auth current authentication
     * @param request employer profile data
     * @return updated employer profile
     */
    @Transactional
    public ProfileDtos.EmployerProfileResponse updateEmployer(
            Authentication auth,
            ProfileDtos.EmployerProfileRequest request) {

        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITGEBER);

        EmployerProfile profile = employers.findByUserId(user.getId())
                .orElseGet(() -> new EmployerProfile(user));

        profile.update(
                request.companyName(),
                request.description(),
                request.location(),
                request.contactEmail()
        );

        return ProfileDtos.EmployerProfileResponse.from(
                employers.save(profile)
        );
    }

    /**
     * Returns the current employer profile.
     *
     * @param auth current authentication
     * @return employer profile
     */
    @Transactional(readOnly = true)
    public ProfileDtos.EmployerProfileResponse getOwnEmployer(Authentication auth) {
        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITGEBER);

        return ProfileDtos.EmployerProfileResponse.from(
                employers.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employer profile has not been created"
                                ))
        );
    }

    /**
     * Uploads a company logo for the current employer.
     *
     * @param auth current authentication
     * @param request image upload data
     * @return updated employer profile
     */
    @Transactional
    public ProfileDtos.EmployerProfileResponse uploadEmployerCompanyLogo(
            Authentication auth,
            ProfileDtos.ImageUploadRequest request) {

        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITGEBER);

        EmployerProfile profile = employers.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employer profile has not been created"
                        ));

        ProfileImageService.ValidatedImage image =
                images.validateAndDecode(request.imageBase64());

        profile.updateCompanyLogo(
                image.data(),
                image.mediaType()
        );

        return ProfileDtos.EmployerProfileResponse.from(
                employers.save(profile)
        );
    }

    /**
     * Normalizes optional search values.
     */
    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}
