package com.bbrz.sebastian.JobSwiperBackend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "work_experiences")
public class WorkExperience {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_profile_id", nullable = false)
    private EmployeeProfile employeeProfile;
    @Column(nullable = false, length = 120) private String company;
    @Column(nullable = false, length = 120) private String position;
    @Column(nullable = false) private LocalDate startDate;
    private LocalDate endDate;
    @Column(length = 2000) private String description;
    @Column(nullable = false) private int sortOrder;

    protected WorkExperience() {}

    public WorkExperience(String company, String position, LocalDate startDate, LocalDate endDate,
                          String description, int sortOrder) {
        this.company = company;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    void attachTo(EmployeeProfile profile) { this.employeeProfile = profile; }
    public Long getId() { return id; }
    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getDescription() { return description; }
    public int getSortOrder() { return sortOrder; }
}
