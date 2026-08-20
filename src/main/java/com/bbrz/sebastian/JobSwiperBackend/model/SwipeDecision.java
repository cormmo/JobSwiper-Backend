package com.bbrz.sebastian.JobSwiperBackend.model;

import com.bbrz.sebastian.JobSwiperBackend.enums.Decision;
import com.bbrz.sebastian.JobSwiperBackend.enums.SwipeDirection;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "swipe_decisions", uniqueConstraints = @UniqueConstraint(
        name = "uk_swipe_relationship",
        columnNames = {"actor_id", "employee_id", "employer_id", "job_offer_id", "direction"}
))
public class SwipeDecision {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actor_id", nullable = false)
    private UserAccount actor;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private UserAccount employee;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employer_id", nullable = false)
    private UserAccount employer;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_offer_id", nullable = false)
    private JobOffer jobOffer;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private SwipeDirection direction;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Decision decision;
    @Column(nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant updatedAt;

    protected SwipeDecision() {}

    public SwipeDecision(UserAccount actor, UserAccount employee, UserAccount employer, JobOffer jobOffer,
                         SwipeDirection direction, Decision decision) {
        this.actor = actor;
        this.employee = employee;
        this.employer = employer;
        this.jobOffer = jobOffer;
        this.direction = direction;
        this.decision = decision;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void changeDecision(Decision decision) { this.decision = decision; this.updatedAt = Instant.now(); }
    public Long getId() { return id; }
    public UserAccount getActor() { return actor; }
    public UserAccount getEmployee() { return employee; }
    public UserAccount getEmployer() { return employer; }
    public JobOffer getJobOffer() { return jobOffer; }
    public SwipeDirection getDirection() { return direction; }
    public Decision getDecision() { return decision; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
