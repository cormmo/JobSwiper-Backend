package com.bbrz.sebastian.JobSwiperBackend.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "job_offers")
public class JobOffer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employer_profile_id", nullable = false)
    private EmployerProfile employerProfile;
    @Column(nullable = false, length = 160) private String title;
    @Column(nullable = false, length = 5000) private String description;
    @Column(nullable = false, length = 3000) private String requirements;
    @Column(nullable = false, length = 120) private String location;
    @Column(nullable = false, length = 100) private String category;
    @Column(nullable = false) private boolean active = true;
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @Column(nullable = false) private Instant lastUpdated;

    protected JobOffer() {}
    public JobOffer(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
        this.createdAt = Instant.now();
        this.lastUpdated = this.createdAt;
    }

    public void update(String title, String description, String requirements, String location, String category) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.location = location;
        this.category = category;
        this.lastUpdated = Instant.now();
    }
    public void setActive(boolean active) { this.active = active; this.lastUpdated = Instant.now(); }

    public Long getId() { return id; }
    public EmployerProfile getEmployerProfile() { return employerProfile; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRequirements() { return requirements; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastUpdated() { return lastUpdated; }
}
