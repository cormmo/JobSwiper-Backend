package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.dto.AdminDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.AuthDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.JobDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.MatchDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.PageResponse;
import com.bbrz.sebastian.JobSwiperBackend.exception.ConflictException;
import com.bbrz.sebastian.JobSwiperBackend.exception.ResourceNotFoundException;
import com.bbrz.sebastian.JobSwiperBackend.model.JobOffer;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobMatchRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobOfferRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.UserAccountRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    private final CurrentUserService currentUsers;
    private final UserAccountRepository users;
    private final JobOfferRepository jobs;
    private final JobMatchRepository matches;

    public AdminService(CurrentUserService currentUsers, UserAccountRepository users, JobOfferRepository jobs,
                        JobMatchRepository matches) {
        this.currentUsers = currentUsers;
        this.users = users;
        this.jobs = jobs;
        this.matches = matches;
    }

    @Transactional(readOnly = true)
    public AdminDtos.OverviewResponse overview() {
        return new AdminDtos.OverviewResponse(users.count(), jobs.countByActiveTrue(), matches.count());
    }

    @Transactional(readOnly = true)
    public PageResponse<AuthDtos.UserResponse> users(int page, int size) {
        return PageResponse.from(users.findAll(PageRequest.of(page, size)), AuthDtos.UserResponse::from);
    }

    @Transactional(readOnly = true)
    public PageResponse<JobDtos.JobOfferResponse> jobs(int page, int size) {
        return PageResponse.from(jobs.findAll(PageRequest.of(page, size)), JobDtos.JobOfferResponse::from);
    }

    @Transactional(readOnly = true)
    public PageResponse<MatchDtos.MatchResponse> matches(int page, int size) {
        return PageResponse.from(matches.findAll(PageRequest.of(page, size)), MatchDtos.MatchResponse::from);
    }

    @Transactional
    public AuthDtos.UserResponse setUserActive(Authentication auth, Long userId, boolean active) {
        UserAccount acting = currentUsers.require(auth);
        if (acting.getId().equals(userId) && !active) throw new ConflictException("You cannot deactivate your own account");
        UserAccount user = users.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(active);
        return AuthDtos.UserResponse.from(user);
    }

    @Transactional
    public JobDtos.JobOfferResponse setJobActive(Long jobId, boolean active) {
        JobOffer job = jobs.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("Job offer not found"));
        job.setActive(active);
        return JobDtos.JobOfferResponse.from(job);
    }
}
