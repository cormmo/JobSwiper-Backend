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
    @Lob
    @Column(name = "company_logo")
    private byte[] companyLogo;
    @Column(name = "company_logo_media_type", length = 32)
    private String companyLogoMediaType;
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

    public void updateCompanyLogo(byte[] companyLogo, String mediaType) {
        this.companyLogo = companyLogo.clone();
        this.companyLogoMediaType = mediaType;
        this.lastUpdated = Instant.now();
    }

    public Long getId() { return id; }
    public UserAccount getUser() { return user; }
    public String getCompanyName() { return companyName; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getContactEmail() { return contactEmail; }
    public byte[] getCompanyLogo() { return companyLogo == null ? null : companyLogo.clone(); }
    public String getCompanyLogoMediaType() { return companyLogoMediaType; }
    public Instant getLastUpdated() { return lastUpdated; }
}
