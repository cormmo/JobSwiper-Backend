package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.dto.AdminDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.AuthDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.JobDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.MatchDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.PageResponse;
import com.bbrz.sebastian.JobSwiperBackend.exception.ConflictException;
import com.bbrz.sebastian.JobSwiperBackend.exception.ResourceNotFoundException;
import com.bbrz.sebastian.JobSwiperBackend.model.JobMatch;
import com.bbrz.sebastian.JobSwiperBackend.model.JobOffer;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobMatchRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobOfferRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.UserAccountRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

/**
 * Provides administrative operations for users, jobs and matches.
 *
 * <p>Supports overview statistics, pagination, status changes and match searches.</p>
 */
@Service
public class AdminService {

    private final CurrentUserService currentUsers;
    private final UserAccountRepository users;
    private final JobOfferRepository jobs;
    private final JobMatchRepository matches;

    /**
     * Creates the admin service.
     *
     * @param currentUsers service for accessing the current user
     * @param users user repository
     * @param jobs job repository
     * @param matches match repository
     */
    public AdminService(CurrentUserService currentUsers, UserAccountRepository users,
                        JobOfferRepository jobs, JobMatchRepository matches) {
        this.currentUsers = currentUsers;
        this.users = users;
        this.jobs = jobs;
        this.matches = matches;
    }

    /**
     * Returns general application statistics.
     *
     * @return admin overview
     */
    @Transactional(readOnly = true)
    public AdminDtos.OverviewResponse overview() {
        return new AdminDtos.OverviewResponse(
                users.count(),
                jobs.countByActiveTrue(),
                matches.count()
        );
    }

    /**
     * Returns a paginated list of users.
     *
     * @param page page number
     * @param size page size
     * @return paginated users
     */
    @Transactional(readOnly = true)
    public PageResponse<AuthDtos.UserResponse> users(int page, int size) {
        return PageResponse.from(
                users.findAll(PageRequest.of(page, size)),
                AuthDtos.UserResponse::from
        );
    }

    /**
     * Returns a paginated list of job offers.
     *
     * @param page page number
     * @param size page size
     * @return paginated job offers
     */
    @Transactional(readOnly = true)
    public PageResponse<JobDtos.JobOfferResponse> jobs(int page, int size) {
        return PageResponse.from(
                jobs.findAll(PageRequest.of(page, size)),
                JobDtos.JobOfferResponse::from
        );
    }

    /**
     * Returns a paginated list of matches.
     *
     * @param page page number
     * @param size page size
     * @return paginated matches
     */
    @Transactional(readOnly = true)
    public PageResponse<MatchDtos.MatchResponse> matches(int page, int size) {
        return PageResponse.from(
                matches.findAll(PageRequest.of(page, size)),
                MatchDtos.MatchResponse::from
        );
    }

    /**
     * Changes the active status of a user.
     *
     * @param auth current authentication
     * @param userId user ID
     * @param active new active status
     * @return updated user
     */
    @Transactional
    public AuthDtos.UserResponse setUserActive(
            Authentication auth,
            Long userId,
            boolean active) {

        UserAccount acting = currentUsers.require(auth);

        if (acting.getId().equals(userId) && !active) {
            throw new ConflictException(
                    "You cannot deactivate your own account"
            );
        }

        UserAccount user = users.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setActive(active);

        return AuthDtos.UserResponse.from(user);
    }

    /**
     * Changes the active status of a job offer.
     *
     * @param jobId job offer ID
     * @param active new active status
     * @return updated job offer
     */
    @Transactional
    public JobDtos.JobOfferResponse setJobActive(Long jobId, boolean active) {
        JobOffer job = jobs.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job offer not found"));

        job.setActive(active);

        return JobDtos.JobOfferResponse.from(job);
    }

    /**
     * Searches matches and groups them by participant.
     *
     * @param query search term
     * @return grouped employee and employer matches
     */
    @Transactional(readOnly = true)
    public AdminDtos.GroupedMatchSearchResponse searchMatches(String query) {
        String term = query.strip().toLowerCase(Locale.ROOT);

        return new AdminDtos.GroupedMatchSearchResponse(
                groupMatches(
                        matches.findMatchesForEmployees(term),
                        JobMatch::getEmployee
                ),
                groupMatches(
                        matches.findMatchesForEmployers(term),
                        JobMatch::getEmployer
                )
        );
    }

    /**
     * Groups matches by user.
     *
     * @param results matches to group
     * @param participant function used to select the participant
     * @return grouped matches
     */
    private List<AdminDtos.ParticipantMatches> groupMatches(
            List<JobMatch> results,
            Function<JobMatch, UserAccount> participant) {

        Map<Long, AuthDtos.UserResponse> usersById = new LinkedHashMap<>();
        Map<Long, List<MatchDtos.MatchResponse>> matchesByUserId =
                new LinkedHashMap<>();

        for (JobMatch match : results) {
            UserAccount user = participant.apply(match);

            usersById.putIfAbsent(
                    user.getId(),
                    AuthDtos.UserResponse.from(user)
            );

            matchesByUserId
                    .computeIfAbsent(
                            user.getId(),
                            ignored -> new ArrayList<>()
                    )
                    .add(MatchDtos.MatchResponse.from(match));
        }

        return matchesByUserId.entrySet().stream()
                .map(entry -> new AdminDtos.ParticipantMatches(
                        usersById.get(entry.getKey()),
                        entry.getValue()
                ))
                .toList();
    }
}
