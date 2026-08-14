package com.bbrz.sebastian.JobSwiperBackend.api;

import com.bbrz.sebastian.JobSwiperBackend.dto.ProfileDtos;
import com.bbrz.sebastian.JobSwiperBackend.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer/profile")
@PreAuthorize("hasRole('ARBEITGEBER')")
public class EmployerProfileController {
    private final ProfileService profiles;

    public EmployerProfileController(ProfileService profiles) { this.profiles = profiles; }

    @GetMapping("/me")
    public ProfileDtos.EmployerProfileResponse me(Authentication auth) { return profiles.getOwnEmployer(auth); }

    @PutMapping("/me")
    public ProfileDtos.EmployerProfileResponse update(Authentication auth,
                                                       @Valid @RequestBody ProfileDtos.EmployerProfileRequest request) {
        return profiles.updateEmployer(auth, request);
    }
}
