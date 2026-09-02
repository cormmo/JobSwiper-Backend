package com.bbrz.sebastian.JobSwiperBackend.api;

import com.bbrz.sebastian.JobSwiperBackend.dto.*;
import com.bbrz.sebastian.JobSwiperBackend.service.AdminService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Provides administrative API endpoints.
 *
 * <p>Allows administrators to view system data and manage users and job offers.</p>
 */
@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    private final AdminService admin;

    /**
     * Creates the admin controller.
     *
     * @param admin service containing administrative operations
     */
    public AdminController(AdminService admin) {
        this.admin = admin;
    }

    /**
     * Returns an overview of administrative statistics.
     *
     * @return admin overview data
     */
    @GetMapping("/overview")
    public AdminDtos.OverviewResponse overview() {
        return admin.overview();
    }

    /**
     * Returns a paginated list of users.
     *
     * @param page page number
     * @param size number of users per page
     * @return paginated user list
     */
    @GetMapping("/users")
    public PageResponse<AuthDtos.UserResponse> users(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return admin.users(page, size);
    }

    /**
     * Changes the active status of a user.
     *
     * @param auth current authentication
     * @param id user ID
     * @param request requested active status
     * @return updated user
     */
    @PatchMapping("/users/{id}/active")
    public AuthDtos.UserResponse userActive(
            Authentication auth,
            @PathVariable Long id,
            @RequestBody JobDtos.ActiveRequest request) {
        return admin.setUserActive(auth, id, request.active());
    }

    /**
     * Returns a paginated list of job offers.
     *
     * @param page page number
     * @param size number of jobs per page
     * @return paginated job list
     */
    @GetMapping("/jobs")
    public PageResponse<JobDtos.JobOfferResponse> jobs(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return admin.jobs(page, size);
    }

    /**
     * Changes the active status of a job offer.
     *
     * @param id job offer ID
     * @param request requested active status
     * @return updated job offer
     */
    @PatchMapping("/jobs/{id}/active")
    public JobDtos.JobOfferResponse jobActive(
            @PathVariable Long id,
            @RequestBody JobDtos.ActiveRequest request) {
        return admin.setJobActive(id, request.active());
    }

    /**
     * Returns a paginated list of matches.
     *
     * @param page page number
     * @param size number of matches per page
     * @return paginated match list
     */
    @GetMapping("/matches")
    public PageResponse<MatchDtos.MatchResponse> matches(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return admin.matches(page, size);
    }
}
