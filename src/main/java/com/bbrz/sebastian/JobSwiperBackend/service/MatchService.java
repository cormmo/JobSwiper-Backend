package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.dto.MatchDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.PageResponse;
import com.bbrz.sebastian.JobSwiperBackend.enums.Decision;
import com.bbrz.sebastian.JobSwiperBackend.enums.SwipeDirection;
import com.bbrz.sebastian.JobSwiperBackend.model.JobMatch;
import com.bbrz.sebastian.JobSwiperBackend.model.JobOffer;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobMatchRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.SwipeDecisionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {
    private final CurrentUserService currentUsers;
    private final JobMatchRepository matches;
    private final SwipeDecisionRepository swipes;

    public MatchService(CurrentUserService currentUsers, JobMatchRepository matches,
                        SwipeDecisionRepository swipes) {
        this.currentUsers = currentUsers;
        this.matches = matches;
        this.swipes = swipes;
    }

    @Transactional(readOnly = true)
    public PageResponse<MatchDtos.MatchResponse> listOwn(Authentication auth, int page, int size) {
        UserAccount user = currentUsers.require(auth);
        return PageResponse.from(matches.findByEmployeeIdOrEmployerId(user.getId(), user.getId(),
                PageRequest.of(page, size)), MatchDtos.MatchResponse::from);
    }

    /**
     * Creates a match if the current decision and the decision from the other side are both likes.
     * Existing matches are left unchanged.
     *
     * @return {@code true} when a new match was created
     */
    @Transactional
    public boolean createIfMutual(UserAccount employee, UserAccount employer, JobOffer job,
                                  Decision currentDecision, SwipeDirection counterpartDirection) {
        if (currentDecision != Decision.LIKE) {
            return false;
        }

        boolean counterpartLiked = swipes
                .findByEmployeeIdAndEmployerIdAndJobOfferIdAndDirection(
                        employee.getId(), employer.getId(), job.getId(), counterpartDirection)
                .map(swipe -> swipe.getDecision() == Decision.LIKE)
                .orElse(false);

        if (!counterpartLiked || matches.existsByEmployeeIdAndEmployerIdAndJobOfferId(
                employee.getId(), employer.getId(), job.getId())) {
            return false;
        }

        matches.save(new JobMatch(employee, employer, job));
        return true;
    }
}
