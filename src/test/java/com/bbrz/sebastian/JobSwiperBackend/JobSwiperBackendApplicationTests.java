package com.bbrz.sebastian.JobSwiperBackend;

import com.bbrz.sebastian.JobSwiperBackend.enums.Decision;
import com.bbrz.sebastian.JobSwiperBackend.enums.Role;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.EmployeeProfileRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobMatchRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.JobOfferRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.SwipeDecisionRepository;
import com.bbrz.sebastian.JobSwiperBackend.repository.UserAccountRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class JobSwiperBackendApplicationTests {

    private static final String PASSWORD = "password123";

    @Autowired MockMvc mvc;
    @Autowired UserAccountRepository users;
    @Autowired EmployeeProfileRepository employeeProfiles;
    @Autowired JobOfferRepository jobs;
    @Autowired SwipeDecisionRepository swipes;
    @Autowired JobMatchRepository matches;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Testfall 1 - Registrierung und JWT-Login")
    void registrationAndJwtLogin() throws Exception {
        AuthSession registration = register("case1", "case1@example.test", "ARBEITNEHMER");
        assertThat(registration.token()).isNotBlank();

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"case1","password":"password123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    @DisplayName("Testfall 2 - Geschützter Endpunkt ohne Token")
    void protectedEndpointWithoutTokenReturnsUnauthorized() throws Exception {
        mvc.perform(get("/api/profile/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Testfall 3 - CORS-Preflight-Request")
    void corsPreflightAllowsConfiguredFrontend() throws Exception {
        mvc.perform(options("/api/profile/me")
                        .header(HttpHeaders.ORIGIN, "http://localhost:8081")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        "http://localhost:8081"));
    }

    @Test
    @DisplayName("Testfall 4 - Arbeitnehmerprofil anlegen und abrufen")
    void employeeProfileCanBeCreatedAndRetrieved() throws Exception {
        AuthSession employee = register("case4", "case4@example.test", "ARBEITNEHMER");

        mvc.perform(put("/api/profile/me")
                        .headers(bearer(employee.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validEmployeeProfile()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.skills[0]").value("Java"));

        mvc.perform(get("/api/profile/me").headers(bearer(employee.token())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Worker"))
                .andExpect(jsonPath("$.location").value("Vienna"))
                .andExpect(jsonPath("$.desiredPosition").value("Backend Developer"))
                .andExpect(jsonPath("$.skills[0]").value("Java"))
                .andExpect(jsonPath("$.workExperience[0].company").value("Example GmbH"));

        var stored = employeeProfiles.findByUserId(employee.userId()).orElseThrow();
        assertThat(stored.getFirstName()).isEqualTo("Alice");
        assertThat(stored.getSkills()).containsExactly("Java", "Spring");
    }

    @Test
    @DisplayName("Testfall 5 - Stellenangebot anlegen")
    void employerCanCreateJobOffer() throws Exception {
        EmployerJob fixture = createEmployerWithJob("case5");

        var stored = jobs.findById(fixture.jobId()).orElseThrow();
        assertThat(stored.getTitle()).isEqualTo("Java Developer");
        assertThat(stored.getEmployerProfile().getUser().getId()).isEqualTo(fixture.employerId());
    }

    @Test
    @DisplayName("Testfall 6 - Swipe durch Arbeitnehmer")
    void employeeSwipeIsStored() throws Exception {
        AuthSession employee = createEmployeeWithProfile("case6employee");
        EmployerJob employer = createEmployerWithJob("case6employer");

        mvc.perform(post("/api/swipes/job/{jobId}", employer.jobId())
                        .headers(bearer(employee.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"decision\":\"LIKE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decision").value(Decision.LIKE.name()));

        assertThat(swipes.count()).isOne();
        assertThat(swipes.findAll().getFirst().getJobOffer().getId()).isEqualTo(employer.jobId());
    }

    @Test
    @DisplayName("Testfall 7 - Beidseitiges Match entsteht")
    void mutualLikesCreateMatch() throws Exception {
        AuthSession employee = createEmployeeWithProfile("case7employee");
        EmployerJob employer = createEmployerWithJob("case7employer");

        performMutualLikes(employee, employer);

        assertThat(matches.count()).isOne();
        var match = matches.findAll().getFirst();
        assertThat(match.getEmployee().getId()).isEqualTo(employee.userId());
        assertThat(match.getEmployer().getId()).isEqualTo(employer.employerId());
        assertThat(match.getJobOffer().getId()).isEqualTo(employer.jobId());
    }

    @Test
    @DisplayName("Testfall 8 - Admin sieht Stellenangebote und Benutzer")
    void adminCanSeeOverviewOfUsersJobsAndMatches() throws Exception {
        AuthSession admin = createAdmin("case8admin");
        AuthSession employee = createEmployeeWithProfile("case8employee");
        EmployerJob employer = createEmployerWithJob("case8employer");
        performMutualLikes(employee, employer);

        mvc.perform(get("/api/admin/overview").headers(bearer(admin.token())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").value(3))
                .andExpect(jsonPath("$.activeJobOffers").value(1))
                .andExpect(jsonPath("$.matches").value(1));
    }

    @Test
    @DisplayName("Testfall 9 - Arbeitnehmer hat keinen Admin-Zugriff")
    void employeeCannotAccessAdminEndpoint() throws Exception {
        AuthSession employee = register("case9", "case9@example.test", "ARBEITNEHMER");

        mvc.perform(get("/api/admin/overview").headers(bearer(employee.token())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Testfall 10 - Serverseitige Validierung ungültiger Profildaten")
    void invalidEmployeeProfileReturnsErrorsAndIsNotStored() throws Exception {
        AuthSession employee = register("case10", "case10@example.test", "ARBEITNEHMER");

        mvc.perform(put("/api/profile/me")
                        .headers(bearer(employee.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"","lastName":"","phone":"123","location":"Vienna",
                                 "summary":"Invalid profile","desiredPosition":"Developer",
                                 "skills":null,"workExperience":null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.firstName").exists())
                .andExpect(jsonPath("$.fieldErrors.lastName").exists())
                .andExpect(jsonPath("$.fieldErrors.skills").exists())
                .andExpect(jsonPath("$.fieldErrors.workExperience").exists());

        assertThat(employeeProfiles.findByUserId(employee.userId())).isEmpty();
    }

    private AuthSession register(String username, String email, String role) throws Exception {
        String response = mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"email\":\"" + email
                                + "\",\"password\":\"" + PASSWORD + "\",\"role\":\"" + role + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return new AuthSession(
                JsonPath.read(response, "$.token"),
                ((Number) JsonPath.read(response, "$.user.id")).longValue());
    }

    private AuthSession createAdmin(String username) throws Exception {
        UserAccount admin = users.saveAndFlush(new UserAccount(
                username,
                username + "@example.test",
                passwordEncoder.encode(PASSWORD),
                Role.ADMIN));

        String response = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + PASSWORD + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return new AuthSession(JsonPath.read(response, "$.token"), admin.getId());
    }

    private AuthSession createEmployeeWithProfile(String username) throws Exception {
        AuthSession employee = register(username, username + "@example.test", "ARBEITNEHMER");
        mvc.perform(put("/api/profile/me")
                        .headers(bearer(employee.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validEmployeeProfile()))
                .andExpect(status().isOk());
        return employee;
    }

    private EmployerJob createEmployerWithJob(String username) throws Exception {
        AuthSession employer = register(username, username + "@example.test", "ARBEITGEBER");

        mvc.perform(put("/api/employer/profile/me")
                        .headers(bearer(employer.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"companyName":"Example GmbH","description":"Software",
                                 "location":"Vienna","contactEmail":"jobs@example.test"}
                                """))
                .andExpect(status().isOk());

        String response = mvc.perform(post("/api/jobs")
                        .headers(bearer(employer.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Java Developer","description":"Build APIs",
                                 "requirements":"Java 21","location":"Vienna","category":"IT"}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return new EmployerJob(
                employer.token(),
                employer.userId(),
                ((Number) JsonPath.read(response, "$.id")).longValue());
    }

    private void performMutualLikes(AuthSession employee, EmployerJob employer) throws Exception {
        mvc.perform(post("/api/swipes/job/{jobId}", employer.jobId())
                        .headers(bearer(employee.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"decision\":\"LIKE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchCreated").value(false));

        mvc.perform(post("/api/swipes/profile/{employeeId}", employee.userId())
                        .headers(bearer(employer.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jobOfferId\":" + employer.jobId() + ",\"decision\":\"LIKE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchCreated").value(true));
    }

    private String validEmployeeProfile() {
        return """
                {"firstName":"Alice","lastName":"Worker","phone":"123","location":"Vienna",
                 "summary":"Java developer","desiredPosition":"Backend Developer",
                 "skills":["Java","Spring"],
                 "workExperience":[{"company":"Example GmbH","position":"Developer",
                 "startDate":"2024-01-01","endDate":null,"description":"Built APIs","sortOrder":0}]}
                """;
    }

    private HttpHeaders bearer(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    private record AuthSession(String token, long userId) {}

    private record EmployerJob(String token, long employerId, long jobId) {}
}
