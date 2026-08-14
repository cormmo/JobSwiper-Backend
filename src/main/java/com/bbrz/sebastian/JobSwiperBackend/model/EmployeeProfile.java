package com.bbrz.sebastian.JobSwiperBackend.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee_profiles")
public class EmployeeProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount user;

    @Column(nullable = false, length = 80) private String firstName;
    @Column(nullable = false, length = 80) private String lastName;
    @Column(length = 40) private String phone;
    @Column(length = 120) private String location;
    @Column(length = 2000) private String summary;
    @Column(length = 160) private String desiredPosition;

    @ElementCollection
    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "skill", nullable = false, length = 80)
    private List<String> skills = new ArrayList<>();

    @OneToMany(mappedBy = "employeeProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<WorkExperience> workExperience = new ArrayList<>();

    @Column(nullable = false) private Instant lastUpdated;

    protected EmployeeProfile() {}

    public EmployeeProfile(UserAccount user) {
        this.user = user;
        this.lastUpdated = Instant.now();
    }

    public void update(String firstName, String lastName, String phone, String location, String summary,
                       String desiredPosition, List<String> skills, List<WorkExperience> experience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.location = location;
        this.summary = summary;
        this.desiredPosition = desiredPosition;
        this.skills.clear();
        this.skills.addAll(skills);
        this.workExperience.clear();
        experience.forEach(item -> item.attachTo(this));
        this.workExperience.addAll(experience);
        this.lastUpdated = Instant.now();
    }

    public Long getId() { return id; }
    public UserAccount getUser() { return user; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }
    public String getSummary() { return summary; }
    public String getDesiredPosition() { return desiredPosition; }
    public List<String> getSkills() { return List.copyOf(skills); }
    public List<WorkExperience> getWorkExperience() { return List.copyOf(workExperience); }
    public Instant getLastUpdated() { return lastUpdated; }
}
