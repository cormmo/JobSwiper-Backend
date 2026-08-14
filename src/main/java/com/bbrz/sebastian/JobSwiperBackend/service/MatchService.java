package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.dto.MatchDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.PageResponse;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobMatchRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {
    private final CurrentUserService currentUsers;
    private final JobMatchRepository matches;

    public MatchService(CurrentUserService currentUsers, JobMatchRepository matches) {
        this.currentUsers = currentUsers;
        this.matches = matches;
    }

    @Transactional(readOnly = true)
    public PageResponse<MatchDtos.MatchResponse> listOwn(Authentication auth, int page, int size) {
        UserAccount user = currentUsers.require(auth);
        return PageResponse.from(matches.findByEmployeeIdOrEmployerId(user.getId(), user.getId(),
                PageRequest.of(page, size)), MatchDtos.MatchResponse::from);
    }
}
