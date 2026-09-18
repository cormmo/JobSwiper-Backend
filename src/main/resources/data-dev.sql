-- Development-only seed data. Loaded through application-dev.yaml after Hibernate
-- has created or updated the schema. Statements are idempotent across restarts.

-- Keep the documented credentials and roles stable in an existing dev database.
UPDATE user_accounts SET password_hash = '$2a$12$9LqAVXHythzKnHrB2hzo4u2TAbi5AJ55txat5GT4v28xX.q.HOgiC', role = 'ARBEITNEHMER', active = TRUE WHERE LOWER(username) = 'test';
UPDATE user_accounts SET password_hash = '$2a$12$hc1BY6IAlp6VfEOVLnC7HekdCz8cKLTBqR5XU4Eu/d8H8tLRhpRMm', role = 'ADMIN', active = TRUE WHERE LOWER(username) = 'admin';
UPDATE user_accounts SET password_hash = '$2a$12$87K2E6PrJd0Ugb5kTT9F0uvBxF3JdsmJjYHbfuKDhTK3ZZ1QXXu/i', role = 'ARBEITGEBER', active = TRUE WHERE LOWER(username) = 'ag';

-- Login: test / testtest
INSERT INTO user_accounts (username, email, password_hash, role, active, created_at)
SELECT 'test', 'test@jobswiper.local', '$2a$12$9LqAVXHythzKnHrB2hzo4u2TAbi5AJ55txat5GT4v28xX.q.HOgiC', 'ARBEITNEHMER', TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM user_accounts WHERE LOWER(username) = 'test' OR LOWER(email) = 'test@jobswiper.local');

-- Login: admin / adminadmin
INSERT INTO user_accounts (username, email, password_hash, role, active, created_at)
SELECT 'admin', 'admin@jobswiper.local', '$2a$12$hc1BY6IAlp6VfEOVLnC7HekdCz8cKLTBqR5XU4Eu/d8H8tLRhpRMm', 'ADMIN', TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM user_accounts WHERE LOWER(username) = 'admin' OR LOWER(email) = 'admin@jobswiper.local');

-- Login: ag / ag123456
INSERT INTO user_accounts (username, email, password_hash, role, active, created_at)
SELECT 'ag', 'ag@jobswiper.local', '$2a$12$87K2E6PrJd0Ugb5kTT9F0uvBxF3JdsmJjYHbfuKDhTK3ZZ1QXXu/i', 'ARBEITGEBER', TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM user_accounts WHERE LOWER(username) = 'ag' OR LOWER(email) = 'ag@jobswiper.local');

INSERT INTO employee_profiles (user_id, first_name, last_name, phone, location, summary, desired_position, last_updated)
SELECT id, 'Tessa', 'Testperson', '+43 660 1234567', 'Wien',
       'Motivierte Allrounderin mit Erfahrung in digitalen Projekten, Kundenkontakt und Organisation. Lernt schnell und arbeitet gerne im Team.',
       'Softwareentwicklerin oder technische Projektmitarbeiterin', CURRENT_TIMESTAMP
FROM user_accounts u
WHERE LOWER(u.username) = 'test'
  AND NOT EXISTS (SELECT 1 FROM employee_profiles p WHERE p.user_id = u.id);

INSERT INTO employee_skills (profile_id, skill)
SELECT p.id, seed.skill
FROM employee_profiles p
JOIN user_accounts u ON u.id = p.user_id
CROSS JOIN (VALUES ('Java'), ('Spring Boot'), ('SQL'), ('Git'), ('REST APIs'), ('Agile Zusammenarbeit')) AS seed(skill)
WHERE LOWER(u.username) = 'test'
  AND NOT EXISTS (SELECT 1 FROM employee_skills existing WHERE existing.profile_id = p.id AND existing.skill = seed.skill);

INSERT INTO work_experiences (employee_profile_id, company, position, start_date, end_date, description, sort_order)
SELECT p.id, 'Digitalwerkstatt Wien', 'Junior Softwareentwicklerin', DATE '2023-02-01', DATE '2025-03-31',
       'Entwicklung und Test von Webanwendungen sowie Mitarbeit an REST-Schnittstellen und Datenbankmodellen.', 0
FROM employee_profiles p JOIN user_accounts u ON u.id = p.user_id
WHERE LOWER(u.username) = 'test'
  AND NOT EXISTS (SELECT 1 FROM work_experiences w WHERE w.employee_profile_id = p.id AND w.company = 'Digitalwerkstatt Wien' AND w.position = 'Junior Softwareentwicklerin');

INSERT INTO work_experiences (employee_profile_id, company, position, start_date, end_date, description, sort_order)
SELECT p.id, 'Servicepunkt Österreich', 'Kundenservice und Administration', DATE '2020-09-01', DATE '2023-01-31',
       'Betreuung von Kundinnen und Kunden, Terminorganisation und Pflege interner Daten.', 1
FROM employee_profiles p JOIN user_accounts u ON u.id = p.user_id
WHERE LOWER(u.username) = 'test'
  AND NOT EXISTS (SELECT 1 FROM work_experiences w WHERE w.employee_profile_id = p.id AND w.company = 'Servicepunkt Österreich' AND w.position = 'Kundenservice und Administration');

INSERT INTO employer_profiles (user_id, company_name, description, location, contact_email, last_updated)
SELECT id, 'JobSwiper Demo GmbH',
       'Demo-Arbeitgeber mit einem vielfältigen Stellenangebot für die lokale Entwicklung und Präsentation von JobSwiper.',
       'Wien', 'recruiting@jobswiper.local', CURRENT_TIMESTAMP
FROM user_accounts u
WHERE LOWER(u.username) = 'ag'
  AND NOT EXISTS (SELECT 1 FROM employer_profiles p WHERE p.user_id = u.id);

-- No swipe or match records: test should initially see every active offer.
INSERT INTO job_offers (employer_profile_id, title, description, requirements, location, category, active, created_at, last_updated)
SELECT employer.id, seed.title, seed.description, seed.requirements, seed.location, seed.category, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM user_accounts owner
JOIN employer_profiles employer ON employer.user_id = owner.id
CROSS JOIN (VALUES
    ('Java Backend Developer', 'Entwickle robuste REST APIs und arbeite gemeinsam mit Product und Frontend an neuen Funktionen.', 'Java, Spring Boot, SQL, Git und Freude an sauberem Code.', 'Wien / Hybrid', 'IT & Software'),
    ('Senior Spring Boot Engineer', 'Übernimm technische Verantwortung für skalierbare Backend-Services und begleite jüngere Entwickler.', 'Mehrjährige Erfahrung mit Java, Spring Boot, Security, Datenbanken und Tests.', 'Wien / Remote', 'IT & Software'),
    ('Frontend Developer Angular', 'Baue barrierearme Benutzeroberflächen, wiederverwendbare Komponenten und integriere REST-Schnittstellen.', 'TypeScript, Angular, HTML, CSS, responsives Design und Frontend-Tests.', 'Graz / Hybrid', 'IT & Software'),
    ('Full Stack Developer', 'Arbeite an einer Weblösung von der Datenbank bis zur modernen Benutzeroberfläche.', 'Java oder Kotlin, TypeScript, REST, SQL und Docker.', 'Linz / Hybrid', 'IT & Software'),
    ('Mobile App Developer', 'Entwickle nutzerfreundliche mobile Anwendungen vom Prototyp bis in den Store.', 'Flutter, React Native, Swift oder Kotlin und Erfahrung mit mobilen APIs.', 'Salzburg / Hybrid', 'IT & Software'),
    ('DevOps Engineer', 'Automatisiere Build- und Deployment-Prozesse und verbessere unsere Entwicklungsplattform.', 'Linux, CI/CD, Docker, Kubernetes und grundlegendes Cloud-Verständnis.', 'Österreich / Remote', 'IT & Software'),
    ('QA Automation Engineer', 'Konzipiere automatisierte Tests für Webanwendungen und unterstütze die Qualitätsstrategie.', 'Testautomatisierung, API- und Browser-Tests sowie eine Programmiersprache.', 'Wien / Hybrid', 'IT & Software'),
    ('IT Support Specialist', 'Unterstütze bei technischen Fragen und betreue Clients, Konten und Standardsoftware.', 'Windows- und Linux-Grundkenntnisse, Serviceorientierung und verständliche Kommunikation.', 'St. Pölten', 'IT & Software'),
    ('Data Analyst', 'Analysiere Geschäftskennzahlen, entwickle Dashboards und übersetze Daten in Empfehlungen.', 'SQL, Power BI oder Tableau und ein gutes Zahlenverständnis.', 'Wien / Hybrid', 'Data & AI'),
    ('Junior Data Scientist', 'Unterstütze ML-Projekte von der Datenaufbereitung bis zur Evaluation von Modellen.', 'Python, Statistik, SQL und Machine-Learning-Grundlagen.', 'Graz / Hybrid', 'Data & AI'),
    ('Machine Learning Engineer', 'Bringe Machine-Learning-Modelle zuverlässig in Produktion und entwickle Datenpipelines.', 'Python, ML-Frameworks, API-Entwicklung, Docker und Monitoring.', 'Österreich / Remote', 'Data & AI'),
    ('UX/UI Designer', 'Gestalte verständliche digitale Produkte auf Basis von Interviews, Prototypen und Feedback.', 'UX/UI-Portfolio, Figma und Kenntnisse barrierearmer Gestaltung.', 'Wien / Hybrid', 'Design'),
    ('Grafikdesigner', 'Entwickle visuelle Konzepte für Kampagnen, Social Media und Print.', 'Adobe Creative Cloud, Gespür für Typografie und ein Portfolio.', 'Innsbruck', 'Design'),
    ('Online Marketing Manager', 'Plane digitale Kampagnen, optimiere Landingpages und werte Ergebnisse aus.', 'SEA, SEO, Analytics, Content-Planung und Kampagnenmanagement.', 'Wien / Hybrid', 'Marketing'),
    ('Social Media Manager', 'Betreue Kanäle, entwickle redaktionelle Formate und baue eine Community auf.', 'Text- und Videosicherheit, Plattformverständnis und Redaktionsplanung.', 'Salzburg / Hybrid', 'Marketing'),
    ('Key Account Manager', 'Betreue strategische Geschäftskunden und entwickle langfristige Partnerschaften.', 'B2B-Vertrieb, Verhandlungsgeschick, Reisebereitschaft und sehr gutes Deutsch.', 'Wien', 'Vertrieb'),
    ('Sales Development Representative', 'Identifiziere neue Geschäftskunden, führe Erstgespräche und qualifiziere Verkaufschancen.', 'Kommunikationsstärke, Eigeninitiative und Interesse an digitalen Produkten.', 'Linz / Hybrid', 'Vertrieb'),
    ('Recruiter', 'Begleite Bewerber durch den Recruiting-Prozess und berate unsere Fachbereiche.', 'Recruiting-Erfahrung, Organisationstalent und wertschätzende Kommunikation.', 'Wien / Hybrid', 'Personal'),
    ('HR Generalist', 'Betreue Mitarbeitende vom Eintritt bis zum Austritt und unterstütze die Personalentwicklung.', 'Arbeitsrechtliche Grundkenntnisse, HR-Erfahrung und Diskretion.', 'Graz', 'Personal'),
    ('Buchhalter', 'Bearbeite laufende Buchhaltung, Zahlungsverkehr und Kontenabstimmungen.', 'Buchhalterprüfung, ERP-Erfahrung und sorgfältige Arbeitsweise.', 'Linz', 'Finanzen'),
    ('Controller', 'Erstelle Budgets und Forecasts, analysiere Abweichungen und entwickle Reports.', 'Wirtschaftliches Studium, Excel und Erfahrung mit BI- oder ERP-Systemen.', 'Wien / Hybrid', 'Finanzen'),
    ('Pflegefachassistenz', 'Begleite Patienten professionell im Alltag und arbeite interdisziplinär.', 'Abgeschlossene Ausbildung, Registereintragung und Empathie.', 'Wien', 'Gesundheit'),
    ('Ordinationsassistenz', 'Organisiere Termine, betreue Patienten und unterstütze den medizinischen Ablauf.', 'Abgeschlossene Ausbildung, freundliches Auftreten und Bürosoftware-Kenntnisse.', 'Baden bei Wien', 'Gesundheit'),
    ('Elektrotechniker', 'Installiere und warte elektrische Anlagen in Gewerbeobjekten.', 'Abgeschlossene Lehre, Führerschein B und Sicherheitskenntnisse.', 'Wien und Umgebung', 'Technik & Handwerk'),
    ('Mechatroniker', 'Warte automatisierte Produktionsanlagen, analysiere Störungen und verbessere Abläufe.', 'Technische Ausbildung, Mechanik, Elektronik und Schichtbereitschaft.', 'Steyr', 'Technik & Handwerk'),
    ('CNC Facharbeiter', 'Rüste und bediene CNC-Anlagen und sichere die Qualität der Bauteile.', 'Metalltechnische Ausbildung, CNC-Erfahrung und technische Zeichnungen.', 'Wels', 'Technik & Handwerk'),
    ('Lagermitarbeiter', 'Übernimm Wareneingang, Kommissionierung, Versand und Lagerbestände.', 'Belastbarkeit, Genauigkeit und idealerweise Staplerschein.', 'Wien', 'Logistik'),
    ('Disponent', 'Plane Transporte, koordiniere Fahrer und löse kurzfristige Änderungen.', 'Organisationstalent, Stressresistenz und Logistikerfahrung.', 'Wels', 'Logistik'),
    ('Koch', 'Bereite saisonale österreichische und internationale Gerichte zu.', 'Kochlehre, Hygienebewusstsein sowie Abend- und Wochenendbereitschaft.', 'Salzburg', 'Gastronomie'),
    ('Projektassistenz', 'Unterstütze Projektleitungen bei Terminen, Dokumentation und Kommunikation.', 'Office-Kenntnisse, Organisationstalent, Genauigkeit und sehr gutes Deutsch.', 'Wien / Hybrid', 'Administration')
) AS seed(title, description, requirements, location, category)
WHERE LOWER(owner.username) = 'ag'
  AND NOT EXISTS (SELECT 1 FROM job_offers existing WHERE existing.employer_profile_id = employer.id AND existing.title = seed.title);
