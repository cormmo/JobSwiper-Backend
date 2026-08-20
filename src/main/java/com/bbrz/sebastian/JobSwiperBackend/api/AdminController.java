package com.bbrz.sebastian.JobSwiperBackend.api;

import com.bbrz.sebastian.JobSwiperBackend.dto.*;
import com.bbrz.sebastian.JobSwiperBackend.service.AdminService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {
    private final AdminService admin;

    public AdminController(AdminService admin) { this.admin = admin; }

    @GetMapping("/overview")
    public AdminDtos.OverviewResponse overview() { return admin.overview(); }

    @GetMapping("/users")
    public PageResponse<AuthDtos.UserResponse> users(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return admin.users(page, size);
    }

    @PatchMapping("/users/{id}/active")
    public AuthDtos.UserResponse userActive(Authentication auth, @PathVariable Long id,
                                             @RequestBody JobDtos.ActiveRequest request) {
        return admin.setUserActive(auth, id, request.active());
    }

    @GetMapping("/jobs")
    public PageResponse<JobDtos.JobOfferResponse> jobs(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return admin.jobs(page, size);
    }

    @PatchMapping("/jobs/{id}/active")
    public JobDtos.JobOfferResponse jobActive(@PathVariable Long id, @RequestBody JobDtos.ActiveRequest request) {
        return admin.setJobActive(id, request.active());
    }

    @GetMapping("/matches")
    public PageResponse<MatchDtos.MatchResponse> matches(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return admin.matches(page, size);
    }
}
