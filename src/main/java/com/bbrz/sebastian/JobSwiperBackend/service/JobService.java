package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.dto.JobDtos;
import com.bbrz.sebastian.JobSwiperBackend.dto.PageResponse;
import com.bbrz.sebastian.JobSwiperBackend.enums.Role;
import com.bbrz.sebastian.JobSwiperBackend.exception.ForbiddenOperationException;
import com.bbrz.sebastian.JobSwiperBackend.exception.ResourceNotFoundException;
import com.bbrz.sebastian.JobSwiperBackend.model.EmployerProfile;
import com.bbrz.sebastian.JobSwiperBackend.model.JobOffer;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.EmployerProfileRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobOfferRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides operations for creating, updating and retrieving job offers.
 *
 * <p>Also checks employer permissions and ownership of job offers.</p>
 */
@Service
public class JobService {

    private final CurrentUserService currentUsers;
    private final EmployerProfileRepository employers;
    private final JobOfferRepository jobs;

    /**
     * Creates the job service.
     *
     * @param currentUsers service for accessing the current user
     * @param employers employer profile repository
     * @param jobs job offer repository
     */
    public JobService(
            CurrentUserService currentUsers,
            EmployerProfileRepository employers,
            JobOfferRepository jobs) {

        this.currentUsers = currentUsers;
        this.employers = employers;
        this.jobs = jobs;
    }

    /**
     * Creates a new job offer for the current employer.
     *
     * @param auth current authentication
     * @param request job offer data
     * @return created job offer
     */
    @Transactional
    public JobDtos.JobOfferResponse create(
            Authentication auth,
            JobDtos.JobOfferRequest request) {

        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITGEBER);

        EmployerProfile employer = employers.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Create an employer profile before adding jobs"
                        ));

        JobOffer job = new JobOffer(employer);
        apply(job, request);

        return JobDtos.JobOfferResponse.from(jobs.save(job));
    }

    /**
     * Updates an existing job offer.
     *
     * @param auth current authentication
     * @param id job offer ID
     * @param request updated job data
     * @return updated job offer
     */
    @Transactional
    public JobDtos.JobOfferResponse update(
            Authentication auth,
            Long id,
            JobDtos.JobOfferRequest request) {

        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITGEBER);
        JobOffer job = requireJob(id);

        requireOwner(job, user);
        apply(job, request);

        return JobDtos.JobOfferResponse.from(job);
    }

    /**
     * Changes the active status of a job offer.
     *
     * @param auth current authentication
     * @param id job offer ID
     * @param active new active status
     * @return updated job offer
     */
    @Transactional
    public JobDtos.JobOfferResponse setActive(
            Authentication auth,
            Long id,
            boolean active) {

        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITGEBER);
        JobOffer job = requireJob(id);

        requireOwner(job, user);
        job.setActive(active);

        return JobDtos.JobOfferResponse.from(job);
    }

    /**
     * Returns an active job offer by ID.
     *
     * @param id job offer ID
     * @return job offer
     */
    @Transactional(readOnly = true)
    public JobDtos.JobOfferResponse get(Long id) {
        JobOffer job = requireJob(id);

        if (!job.isActive()) {
            throw new ResourceNotFoundException("Job offer not found");
        }

        return JobDtos.JobOfferResponse.from(job);
    }

    /**
     * Returns active job offers with optional filters.
     *
     * @param category optional category filter
     * @param location optional location filter
     * @param page page number
     * @param size page size
     * @return paginated job offers
     */
    @Transactional(readOnly = true)
    public PageResponse<JobDtos.JobOfferResponse> listActive(
            String category,
            String location,
            int page,
            int size) {

        var result = jobs.searchActive(
                normalize(category),
                normalize(location),
                PageRequest.of(page, size)
        );

        return PageResponse.from(result, JobDtos.JobOfferResponse::from);
    }

    /**
     * Returns job offers owned by the current employer.
     *
     * @param auth current authentication
     * @param page page number
     * @param size page size
     * @return paginated own job offers
     */
    @Transactional(readOnly = true)
    public PageResponse<JobDtos.JobOfferResponse> listOwn(
            Authentication auth,
            int page,
            int size) {

        UserAccount user = currentUsers.requireRole(auth, Role.ARBEITGEBER);

        return PageResponse.from(
                jobs.findByEmployerProfileUserId(
                        user.getId(),
                        PageRequest.of(page, size)
                ),
                JobDtos.JobOfferResponse::from
        );
    }

    /**
     * Returns a job offer by ID.
     *
     * @param id job offer ID
     * @return matching job offer
     */
    JobOffer requireJob(Long id) {
        return jobs.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job offer not found"));
    }

    /**
     * Returns and locks a job offer for updating.
     *
     * @param id job offer ID
     * @return locked job offer
     */
    JobOffer requireJobForUpdate(Long id) {
        return jobs.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job offer not found"));
    }

    /**
     * Verifies that a user owns the job offer.
     */
    private void requireOwner(JobOffer job, UserAccount user) {
        if (!job.getEmployerProfile().getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException(
                    "You do not own this job offer"
            );
        }
    }

    /**
     * Applies request data to a job offer.
     */
    private void apply(JobOffer job, JobDtos.JobOfferRequest request) {
        job.update(
                request.title(),
                request.description(),
                request.requirements(),
                request.location(),
                request.category()
        );
    }

    /**
     * Normalizes optional filter values.
     */
    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
