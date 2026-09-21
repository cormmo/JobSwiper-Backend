package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.enums.Decision;
import com.bbrz.sebastian.JobSwiperBackend.enums.SwipeDirection;
import com.bbrz.sebastian.JobSwiperBackend.model.JobOffer;
import com.bbrz.sebastian.JobSwiperBackend.model.SwipeDecision;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobMatchRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.SwipeDecisionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MatchServiceTest {

    @Test
    @DisplayName("Testfall 12 - Match wird nur bei beidseitigem Like erzeugt")
    void matchIsCreatedOnlyWhenBothSidesLike() {
        CurrentUserService currentUsers = mock(CurrentUserService.class);
        JobMatchRepository matches = mock(JobMatchRepository.class);
        SwipeDecisionRepository swipes = mock(SwipeDecisionRepository.class);
        MatchService service = new MatchService(currentUsers, matches, swipes);

        UserAccount employee = mock(UserAccount.class);
        UserAccount employer = mock(UserAccount.class);
        JobOffer job = mock(JobOffer.class);
        when(employee.getId()).thenReturn(1L);
        when(employer.getId()).thenReturn(2L);
        when(job.getId()).thenReturn(3L);

        assertThat(service.createIfMutual(
                employee, employer, job, Decision.LIKE,
                SwipeDirection.ARBEITGEBER_TO_ARBEITNEHMER)).isFalse();
        verify(matches, never()).save(any());

        SwipeDecision counterpartLike = mock(SwipeDecision.class);
        when(counterpartLike.getDecision()).thenReturn(Decision.LIKE);
        when(swipes.findByEmployeeIdAndEmployerIdAndJobOfferIdAndDirection(
                1L, 2L, 3L, SwipeDirection.ARBEITGEBER_TO_ARBEITNEHMER))
                .thenReturn(Optional.of(counterpartLike));
        when(matches.existsByEmployeeIdAndEmployerIdAndJobOfferId(1L, 2L, 3L)).thenReturn(false);

        assertThat(service.createIfMutual(
                employee, employer, job, Decision.LIKE,
                SwipeDirection.ARBEITGEBER_TO_ARBEITNEHMER)).isTrue();
        verify(matches).save(any());
    }
}
