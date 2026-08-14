package com.bbrz.sebastian.JobSwiperBackend.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "employer_profiles")
public class EmployerProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount user;
    @Column(nullable = false, length = 160) private String companyName;
    @Column(length = 3000) private String description;
    @Column(length = 120) private String location;
    @Column(nullable = false, length = 254) private String contactEmail;
    @Column(nullable = false) private Instant lastUpdated;

    protected EmployerProfile() {}
    public EmployerProfile(UserAccount user) { this.user = user; this.lastUpdated = Instant.now(); }

    public void update(String companyName, String description, String location, String contactEmail) {
        this.companyName = companyName;
        this.description = description;
        this.location = location;
        this.contactEmail = contactEmail;
        this.lastUpdated = Instant.now();
    }

    public Long getId() { return id; }
    public UserAccount getUser() { return user; }
    public String getCompanyName() { return companyName; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getContactEmail() { return contactEmail; }
    public Instant getLastUpdated() { return lastUpdated; }
}
