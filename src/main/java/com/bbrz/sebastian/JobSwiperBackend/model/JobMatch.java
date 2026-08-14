package com.bbrz.sebastian.JobSwiperBackend.model;

import com.bbrz.sebastian.JobSwiperBackend.enums.MatchStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "job_matches", uniqueConstraints = @UniqueConstraint(
        name = "uk_match_relationship", columnNames = {"employee_id", "employer_id", "job_offer_id"}
))
public class JobMatch {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private UserAccount employee;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employer_id", nullable = false)
    private UserAccount employer;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_offer_id", nullable = false)
    private JobOffer jobOffer;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private MatchStatus status;

    protected JobMatch() {}
    public JobMatch(UserAccount employee, UserAccount employer, JobOffer jobOffer) {
        this.employee = employee;
        this.employer = employer;
        this.jobOffer = jobOffer;
        this.createdAt = Instant.now();
        this.status = MatchStatus.OFFEN;
    }

    public void setStatus(MatchStatus status) { this.status = status; }
    public Long getId() { return id; }
    public UserAccount getEmployee() { return employee; }
    public UserAccount getEmployer() { return employer; }
    public JobOffer getJobOffer() { return jobOffer; }
    public Instant getCreatedAt() { return createdAt; }
    public MatchStatus getStatus() { return status; }
}
