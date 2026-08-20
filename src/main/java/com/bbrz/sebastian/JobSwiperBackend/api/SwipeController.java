package com.bbrz.sebastian.JobSwiperBackend.api;

import com.bbrz.sebastian.JobSwiperBackend.dto.SwipeDtos;
import com.bbrz.sebastian.JobSwiperBackend.service.SwipeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/swipes")
public class SwipeController {
    private final SwipeService swipes;

    public SwipeController(SwipeService swipes) { this.swipes = swipes; }

    @PostMapping("/job/{jobId}")
    @PreAuthorize("hasRole('ARBEITNEHMER')")
    public SwipeDtos.SwipeResponse job(Authentication auth, @PathVariable Long jobId,
                                        @Valid @RequestBody SwipeDtos.JobSwipeRequest request) {
        return swipes.swipeJob(auth, jobId, request.decision());
    }

    @PostMapping("/profile/{employeeId}")
    @PreAuthorize("hasRole('ARBEITGEBER')")
    public SwipeDtos.SwipeResponse profile(Authentication auth, @PathVariable Long employeeId,
                                            @Valid @RequestBody SwipeDtos.ProfileSwipeRequest request) {
        return swipes.swipeEmployee(auth, employeeId, request.jobOfferId(), request.decision());
    }
}
