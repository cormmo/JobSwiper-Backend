package com.bbrz.sebastian.JobSwiperBackend.api;

import com.bbrz.sebastian.JobSwiperBackend.dto.PageResponse;
import com.bbrz.sebastian.JobSwiperBackend.dto.ProfileDtos;
import com.bbrz.sebastian.JobSwiperBackend.service.ProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@Validated
public class EmployeeProfileController {
    private final ProfileService profiles;

    public EmployeeProfileController(ProfileService profiles) { this.profiles = profiles; }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ARBEITNEHMER')")
    public ProfileDtos.EmployeeProfileResponse me(Authentication auth) { return profiles.getOwnEmployee(auth); }

    @PutMapping("/me")
    @PreAuthorize("hasRole('ARBEITNEHMER')")
    public ProfileDtos.EmployeeProfileResponse update(Authentication auth,
                                                       @Valid @RequestBody ProfileDtos.EmployeeProfileRequest request) {
        return profiles.updateEmployee(auth, request);
    }

    @GetMapping("/employees/{userId}")
    @PreAuthorize("hasAnyRole('ARBEITGEBER','ADMIN')")
    public ProfileDtos.EmployeeProfileResponse employee(@PathVariable Long userId) {
        return profiles.getEmployee(userId);
    }

    @GetMapping("/employees")
    @PreAuthorize("hasAnyRole('ARBEITGEBER','ADMIN')")
    public PageResponse<ProfileDtos.EmployeeProfileResponse> employees(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skill,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return profiles.listEmployees(location, skill, page, size);
    }
}
