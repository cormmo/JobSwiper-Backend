package com.bbrz.sebastian.JobSwiperBackend.api;

import com.bbrz.sebastian.JobSwiperBackend.dto.MatchDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.PageResponse;
import com.bbrz.sebastian.JobSwiperBackend.service.MatchService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@Validated
public class MatchController {
    private final MatchService matches;

    public MatchController(MatchService matches) { this.matches = matches; }

    @GetMapping
    public PageResponse<MatchDtos.MatchResponse> list(Authentication auth,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return matches.listOwn(auth, page, size);
    }
}
