package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.dto.SwipeDtos;
import com.bbrz.sebastian.JobSwiperBackend.enums.Decision;
import com.bbrz.sebastian.JobSwiperBackend.enums.Role;
import com.bbrz.sebastian.JobSwiperBackend.enums.SwipeDirection;
import com.bbrz.sebastian.JobSwiperBackend.exception.ForbiddenOperationException;
import com.bbrz.sebastian.JobSwiperBackend.exception.ResourceNotFoundException;
import com.bbrz.sebastian.JobSwiperBackend.model.JobMatch;
import com.bbrz.sebastian.JobSwiperBackend.model.JobOffer;
import com.bbrz.sebastian.JobSwiperBackend.model.SwipeDecision;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.EmployeeProfileRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobMatchRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.SwipeDecisionRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles swipe decisions between employees and employers.
 *
 * <p>Creates a match when both sides have liked each other.</p>
 */
@Service
public class SwipeService {

    private final CurrentUserService currentUsers;
    private final JobService jobService;
    private final UserAccountRepository users;
    private final EmployeeProfileRepository employeeProfiles;
    private final SwipeDecisionRepository swipes;
    private final JobMatchRepository matches;

    /**
     * Creates the swipe service.
     *
     * @param currentUsers service for accessing the current user
     * @param jobService service for job offers
     * @param users user repository
     * @param employeeProfiles employee profile repository
     * @param swipes swipe decision repository
     * @param matches match repository
     */
    public SwipeService(
            CurrentUserService currentUsers,
            JobService jobService,
            UserAccountRepository users,
            EmployeeProfileRepository employeeProfiles,
            SwipeDecisionRepository swipes,
            JobMatchRepository matches) {

        this.currentUsers = currentUsers;
        this.jobService = jobService;
        this.users = users;
        this.employeeProfiles = employeeProfiles;
        this.swipes = swipes;
        this.matches = matches;
    }

    /**
     * Lets an employee swipe on a job offer.
     *
     * @param auth current authentication
     * @param jobId job offer ID
     * @param decision swipe decision
     * @return swipe result
     */
    @Transactional
    public SwipeDtos.SwipeResponse swipeJob(
            Authentication auth,
            Long jobId,
            Decision decision) {

        UserAccount employee =
                currentUsers.requireRole(auth, Role.ARBEITNEHMER);

        employeeProfiles.findByUserId(employee.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Create an employee profile before swiping"
                        ));

        JobOffer job = jobService.requireJobForUpdate(jobId);

        if (!job.isActive()) {
            throw new ResourceNotFoundException("Job offer not found");
        }

        UserAccount employer = job.getEmployerProfile().getUser();

        SwipeDecision swipe = upsert(
                employee,
                employee,
                employer,
                job,
                SwipeDirection.ARBEITNEHMER_TO_JOB,
                decision
        );

        return SwipeDtos.SwipeResponse.from(
                swipe,
                createMatchIfMutual(
                        employee,
                        employer,
                        job,
                        decision,
                        SwipeDirection.ARBEITGEBER_TO_ARBEITNEHMER
                )
        );
    }

    /**
     * Lets an employer swipe on an employee for a job offer.
     *
     * @param auth current authentication
     * @param employeeId employee user ID
     * @param jobId job offer ID
     * @param decision swipe decision
     * @return swipe result
     */
    @Transactional
    public SwipeDtos.SwipeResponse swipeEmployee(
            Authentication auth,
            Long employeeId,
            Long jobId,
            Decision decision) {

        UserAccount employer =
                currentUsers.requireRole(auth, Role.ARBEITGEBER);

        UserAccount employee = users.findById(employeeId)
                .filter(UserAccount::isActive)
                .filter(user -> user.getRole() == Role.ARBEITNEHMER)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        employeeProfiles.findByUserId(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee profile not found"
                        ));

        JobOffer job = jobService.requireJobForUpdate(jobId);

        if (!job.isActive()) {
            throw new ResourceNotFoundException("Job offer not found");
        }

        if (!job.getEmployerProfile()
                .getUser()
                .getId()
                .equals(employer.getId())) {

            throw new ForbiddenOperationException(
                    "The selected job offer does not belong to you"
            );
        }

        SwipeDecision swipe = upsert(
                employer,
                employee,
                employer,
                job,
                SwipeDirection.ARBEITGEBER_TO_ARBEITNEHMER,
                decision
        );

        return SwipeDtos.SwipeResponse.from(
                swipe,
                createMatchIfMutual(
                        employee,
                        employer,
                        job,
                        decision,
                        SwipeDirection.ARBEITNEHMER_TO_JOB
                )
        );
    }

    /**
     * Creates or updates a swipe decision.
     */
    private SwipeDecision upsert(
            UserAccount actor,
            UserAccount employee,
            UserAccount employer,
            JobOffer job,
            SwipeDirection direction,
            Decision decision) {

        SwipeDecision swipe =
                swipes.findByActorIdAndEmployeeIdAndEmployerIdAndJobOfferIdAndDirection(
                                actor.getId(),
                                employee.getId(),
                                employer.getId(),
                                job.getId(),
                                direction
                        )
                        .orElseGet(() ->
                                new SwipeDecision(
                                        actor,
                                        employee,
                                        employer,
                                        job,
                                        direction,
                                        decision
                                ));

        swipe.changeDecision(decision);

        return swipes.save(swipe);
    }

    /**
     * Creates a match when both sides have liked each other.
     */
    private boolean createMatchIfMutual(
            UserAccount employee,
            UserAccount employer,
            JobOffer job,
            Decision currentDecision,
            SwipeDirection counterpartDirection) {

        if (currentDecision != Decision.LIKE) {
            return false;
        }

        boolean counterpartLiked =
                swipes.findByEmployeeIdAndEmployerIdAndJobOfferIdAndDirection(
                                employee.getId(),
                                employer.getId(),
                                job.getId(),
                                counterpartDirection
                        )
                        .map(swipe ->
                                swipe.getDecision() == Decision.LIKE)
                        .orElse(false);

        if (!counterpartLiked
                || matches.existsByEmployeeIdAndEmployerIdAndJobOfferId(
                employee.getId(),
                employer.getId(),
                job.getId())) {
            return false;
        }

        matches.save(new JobMatch(employee, employer, job));

        return true;
    }
}
