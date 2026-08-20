package com.bbrz.sebastian.JobSwiperBackend.api;

import com.bbrz.sebastian.JobSwiperBackend.dto.JobDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.PageResponse;
import com.bbrz.sebastian.JobSwiperBackend.service.JobService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@Validated
public class JobController {
    private final JobService jobs;

    public JobController(JobService jobs) { this.jobs = jobs; }

    @GetMapping
    public PageResponse<JobDtos.JobOfferResponse> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return jobs.listActive(category, location, page, size);
    }

    @GetMapping("/{id}")
    public JobDtos.JobOfferResponse get(@PathVariable Long id) { return jobs.get(id); }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('ARBEITGEBER')")
    public PageResponse<JobDtos.JobOfferResponse> mine(Authentication auth,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return jobs.listOwn(auth, page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ARBEITGEBER')")
    public JobDtos.JobOfferResponse create(Authentication auth,
                                            @Valid @RequestBody JobDtos.JobOfferRequest request) {
        return jobs.create(auth, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ARBEITGEBER')")
    public JobDtos.JobOfferResponse update(Authentication auth, @PathVariable Long id,
                                            @Valid @RequestBody JobDtos.JobOfferRequest request) {
        return jobs.update(auth, id, request);
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ARBEITGEBER')")
    public JobDtos.JobOfferResponse active(Authentication auth, @PathVariable Long id,
                                            @RequestBody JobDtos.ActiveRequest request) {
        return jobs.setActive(auth, id, request.active());
    }
}
