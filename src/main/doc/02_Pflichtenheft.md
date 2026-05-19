#### Sebastian Steiner ####

---
# Pflichtenheft
## JobSwiper – Webbasiertes Jobportal für Arbeitgeber und Arbeitnehmer

**Projektbezeichnung:** JobSwiper
**Version:** 1.0
**Erstellt am:** 23.04.2026

### Systemarchitektur (Überblick)

Das Projekt besteht aus **zwei eigenständigen Spring Boot Applikationen**:

| Komponente | Bezeichnung | Port | Aufgabe |
|-----------|-------------|------|---------|
| **jobswiper-backend** | REST-API Backend | 8080 | Datenhaltung, Geschäftslogik, Authentifizierung |
| **jobswiper-frontend** | Web-Frontend | 8081 | Benutzeroberfläche, ruft Backend-API per HTTP auf |

Die Trennung der beiden Projekte erfordert eine explizite **CORS-Konfiguration** im Backend, da der Browser Anfragen von `localhost:8081` an `localhost:8080` als Cross-Origin-Anfragen behandelt.

```text
Browser (Port 8081)
      │  HTTP-Request (Thymeleaf-Seite)
      ▼
jobswiper-frontend (Spring Boot, Port 8081)
      │  REST-Call via JavaScript fetch() / RestTemplate
      │  → Origin: http://localhost:8081
      ▼
jobswiper-backend (Spring Boot, Port 8080)
      │  CORS-Header: Access-Control-Allow-Origin: http://localhost:8081
      ▼
H2 Database (embedded im Backend)
```

---

## 1. Zielbestimmung

### 1.1 Musskriterien

- **M01** – Das Backend stellt eine REST-API bereit, die alle Datenzugriffe kapselt. Das Frontend darf nicht direkt auf die Datenbank zugreifen.
- **M02** – Das Backend konfiguriert CORS explizit, sodass Anfragen vom Frontend (andere Origin) zugelassen werden.
- **M03** – Benutzer können sich am System registrieren und anmelden. Die Authentifizierung erfolgt über JWT (JSON Web Token).
- **M04** – Arbeitnehmer können ein persönliches Profil in strukturierter Form erfassen und bearbeiten (Stammdaten, Kurzbeschreibung, Fähigkeiten, Berufserfahrung, gewünschte Tätigkeit).
- **M05** – Arbeitgeber können Unternehmensprofile und Stellenangebote anlegen, bearbeiten und verwalten.
- **M06** – Das System ermöglicht ein Swipe-basiertes Matching zwischen Arbeitnehmerprofilen und Stellenangeboten. Bei beidseitigem Interesse wird ein Match erzeugt.
- **M07** – Administratoren können Benutzerkonten und Stellenangebote einsehen, verwalten und bei Bedarf deaktivieren.
- **M08** – Das Frontend stellt alle Funktionen über ein responsives Web-Interface bereit (Bootstrap 5).
- **M09** – Alle Dateneingaben werden serverseitig validiert (Bean Validation).
- **M10** – Passwörter werden BCrypt-gehasht gespeichert; JWT-Tokens sind zeitlich begrenzt (Ablauf nach 8 Stunden).

### 1.2 Wunschkriterien

- **W01** – Match-Benachrichtigung im Frontend, sobald ein beidseitiges Interesse entstanden ist.
- **W02** – Detailansicht eines Matches mit zusätzlichen Informationen zu Profil und Stellenangebot.
- **W03** – Profilbild-Upload für Arbeitnehmer und Unternehmenslogo-Upload für Arbeitgeber.
- **W04** – Admin-Dashboard mit Übersicht: Anzahl Benutzer, Anzahl Stellenangebote, Anzahl Matches.
- **W05** – Filterfunktion für Stellenangebote und Arbeitnehmerprofile nach Kategorie oder Fähigkeiten.

### 1.3 Abgrenzungskriterien

- **A01** – Keine direkte Integration mit externen Jobbörsen oder AMS-Systemen.
- **A02** – Kein Echtzeit-Chat zwischen Arbeitgebern und Arbeitnehmern im Pflichtumfang.
- **A03** – Kein automatischer Import von Bewerbungsunterlagen oder externen Jobprofilen.
- **A04** – Die Anwendung ist für Demonstrations- und Ausbildungszwecke konzipiert, nicht für den produktiven öffentlichen Interneteinsatz (kein HTTPS im Pflichtumfang).
- **A05** – Keine native Mobile App – nur responsives Web.

---

## 2. Produkteinsatz

### 2.1 Anwendungsbereiche

- Vermittlung von Stellenangeboten zwischen Arbeitgebern und Arbeitnehmern
- Vereinfachte digitale Kontaktanbahnung durch Swipe-basiertes Matching
- Übersicht für Administratoren über Benutzer, Stellenangebote und Matches

### 2.2 Zielgruppen

| Rolle | Beschreibung |
|-------|-------------|
| **Admin** | Kann Benutzerkonten, Stellenangebote und Matches einsehen, verwalten und moderieren |
| **Arbeitnehmer** | Erstellt ein eigenes Profil, bewertet Stellenangebote per Swipe und erhält Matches |
| **Arbeitgeber** | Erstellt ein Unternehmensprofil, veröffentlicht Stellenangebote und bewertet Arbeitnehmerprofile per Swipe |

### 2.3 Betriebsbedingungen

- Betrieb auf einem lokalen Entwicklungsrechner oder in einer Testumgebung
- Beide Spring Boot Applikationen laufen auf demselben Server oder Entwicklerrechner
- Zugriff über Standard-Webbrowser; kein Plugin erforderlich

---

## 3. Produktumgebung

### 3.1 Software (Laufzeit)

| Komponente | Version |
|-----------|---------|
| Java Runtime Environment | 17 (LTS) oder höher |
| Webbrowser | Chrome 110+, Firefox 110+, Edge 110+ |
| Betriebssystem | Windows 10/11, Linux, macOS |

### 3.2 Hardware (Mindestanforderungen)

| Ressource | Mindest | Empfohlen |
|-----------|---------|-----------|
| CPU | 2 Cores, 1.5 GHz | 4 Cores, 2 GHz |
| RAM | 1 GB frei | 2 GB frei (beide Apps laufen gleichzeitig) |
| Festplatte | 300 MB | 1 GB |

### 3.3 Produktschnittstellen

| Schnittstelle | Beschreibung |
|--------------|-------------|
| REST-API (intern) | Frontend → Backend über HTTP/JSON, Port 8080 |
| H2-Datenbankschnittstelle | Backend → H2 via JDBC / Spring Data JPA |
| Browser-HTTP | Benutzer → Frontend über HTTP, Port 8081 |

---


## 4. Produktfunktionen

### F01 – Registrierung und Login
Neue Benutzer können sich mit Benutzername, E-Mail und Passwort registrieren. Nach dem Login erhält das Frontend ein JWT, das bei jedem weiteren API-Aufruf im `Authorization: Bearer`-Header mitgesendet wird.

### F02 – Arbeitnehmerprofil erfassen und bearbeiten
Arbeitnehmer pflegen ihr Profil in mehreren Abschnitten:
- **Stammdaten:** Name, E-Mail, Telefon, Wohnort
- **Kurzprofil:** Kurze Beschreibung der eigenen Person und Ziele
- **Fähigkeiten:** Fachliche Kenntnisse und Kompetenzen
- **Berufserfahrung:** Einträge mit Unternehmen, Position, Zeitraum und Beschreibung
- **Gewünschte Tätigkeit:** Angabe der bevorzugten Branche oder Position

### F03 – Arbeitgeberprofil und Stellenangebote verwalten
Arbeitgeber können ein Unternehmensprofil mit Namen, Beschreibung und Kontaktdaten pflegen. Zusätzlich können sie Stellenangebote mit Titel, Beschreibung, Anforderungen, Standort und Kategorie anlegen, bearbeiten und deaktivieren.

### F04 – Swipe-basiertes Matching
Arbeitnehmer sehen passende Stellenangebote und können diese per Like oder Dislike bewerten. Arbeitgeber sehen Arbeitnehmerprofile und können diese ebenfalls bewerten. Wenn beide Seiten ein Like abgegeben haben, erzeugt das System automatisch ein Match.

### F05 – Match-Übersicht
Benutzer können ihre entstandenen Matches in einer Übersicht einsehen. Dort werden die wichtigsten Informationen zum jeweiligen Stellenangebot bzw. Arbeitnehmerprofil angezeigt.

### F06 – Administrationsbereich
Der Admin sieht alle registrierten Benutzer, Stellenangebote und Matches in Tabellenform. Benutzerkonten und Stellenangebote können deaktiviert oder überprüft werden.

### F07 – CORS-Demonstration (technisches Kernmerkmal)
Das Backend konfiguriert CORS über `@CrossOrigin` auf Klassen-Ebene oder zentral via `WebMvcConfigurer`. Das Frontend sendet Requests von einer anderen Origin (Port 8081) und empfängt die korrekten CORS-Response-Header. Im Entwicklungsmodus wird dies durch Browser-DevTools sichtbar gemacht.

### F08 – Match-Benachrichtigung anzeigen (Wunschkriterium W01)
Sobald ein Match entsteht, zeigt das Frontend dem Benutzer eine Benachrichtigung oder Hervorhebung in der Match-Übersicht an.

### F09 – Filterfunktion für Profile und Stellenangebote (Wunschkriterium W05)
Benutzer können Stellenangebote oder Arbeitnehmerprofile nach Kategorien, Fähigkeiten oder Standort filtern, um passendere Vorschläge zu erhalten.

---


## 5. Produktdaten

### Backend-Entitäten

**User**
- id (Long, PK)
- username (String, unique, not null)
- email (String, unique, not null)
- password (String, BCrypt, not null)
- role (Enum: ADMIN, ARBEITNEHMER, ARBEITGEBER)
- active (Boolean)
- createdAt (LocalDateTime)

**EmployeeProfile** (1:1 zu User)
- id (Long, PK)
- user (User, FK, unique)
- fullName (String)
- phone (String)
- location (String)
- summary (String)
- skills (String oder separate Entity)
- desiredPosition (String)
- profileImageBase64 (String, nullable)
- lastUpdated (LocalDateTime)

**WorkExperience** (n:1 zu EmployeeProfile)
- id (Long, PK)
- employeeProfile (EmployeeProfile, FK)
- company (String)
- position (String)
- startDate (LocalDate)
- endDate (LocalDate, nullable – „bis heute“)
- description (String)
- sortOrder (Integer)

**EmployerProfile** (1:1 zu User)
- id (Long, PK)
- user (User, FK, unique)
- companyName (String)
- description (String)
- location (String)
- contactEmail (String)
- companyLogoBase64 (String, nullable)
- lastUpdated (LocalDateTime)

**JobOffer** (n:1 zu EmployerProfile)
- id (Long, PK)
- employerProfile (EmployerProfile, FK)
- title (String)
- description (String)
- requirements (String)
- location (String)
- category (String)
- active (Boolean)
- createdAt (LocalDateTime)

**SwipeDecision**
- id (Long, PK)
- actor (User, FK)
- targetUser (User, FK, nullable)
- targetJobOffer (JobOffer, FK, nullable)
- decision (Enum: LIKE, DISLIKE)
- createdAt (LocalDateTime)

**Match**
- id (Long, PK)
- employee (User, FK)
- employer (User, FK)
- jobOffer (JobOffer, FK)
- createdAt (LocalDateTime)
- status (Enum: OFFEN, BESTAETIGT, ARCHIVIERT)

### Datenmenge (Schätzung)
- User: 200 Einträge
- EmployeeProfile: 120 Einträge
- EmployerProfile: 80 Einträge
- WorkExperience: ø 2 pro Arbeitnehmerprofil → 240 Einträge
- JobOffer: ø 3 pro Arbeitgeberprofil → 240 Einträge
- SwipeDecision: ca. 2.000 Einträge
- Match: ca. 150 Einträge

---


## 6. Produktleistungen

- **API-Antwortzeit:** < 500ms bei bis zu 50 gleichzeitigen Benutzern im LAN
- **Seitenaufbau Frontend:** < 2 Sekunden (inkl. API-Calls)
- **Swipe-Verarbeitung:** < 1 Sekunde pro Bewertung
- **Match-Erzeugung:** < 1 Sekunde nach beidseitigem Like
- **JWT-Gültigkeit:** 8 Stunden (danach erneuter Login erforderlich)
- **Datenpersistenz:** H2 im Datei-Modus, kein Datenverlust bei Neustart

---


## 7. Benutzeroberfläche

### Frontend-Seiten

| Seite | URL (Frontend) | Zugänglich für |
|-------|---------------|---------------|
| Login / Registrierung | `/login`, `/register` | Alle (nicht eingeloggt) |
| Dashboard Arbeitnehmer | `/dashboard` | Eingeloggte Arbeitnehmer |
| Dashboard Arbeitgeber | `/employer/dashboard` | Eingeloggte Arbeitgeber |
| Arbeitnehmerprofil bearbeiten | `/profile/edit` | Eigener Arbeitnehmer |
| Stellenangebote verwalten | `/jobs/manage` | Eigener Arbeitgeber |
| Swipe-Ansicht Jobs | `/jobs/swipe` | Arbeitnehmer |
| Swipe-Ansicht Kandidaten | `/candidates/swipe` | Arbeitgeber |
| Match-Übersicht | `/matches` | Eingeloggte Benutzer |
| Admin-Übersicht | `/admin` | Admin |

### Gestaltungsprinzipien
- Bootstrap 5, responsiv (Mobile-First)
- Kartenbasierte Darstellung für Swipe-Ansichten
- Klar getrennte Bereiche für Arbeitnehmer, Arbeitgeber und Admin
- Formularvalidierung: clientseitig (HTML5 required) + serverseitige Fehlermeldungen via Thymeleaf

### CORS-Fluss (sichtbar im Browser)
Im Browser-Netzwerk-Tab ist bei jedem API-Aufruf der `OPTIONS`-Preflight-Request sowie der `Access-Control-Allow-Origin`-Antwortheader sichtbar – ein zentrales Lernziel des Projektes.

---


## 8. Qualitäts-Zielbestimmungen

| Merkmal | Ziel | Maßnahme |
|---------|------|----------|
| Sicherheit | Kein unbefugter Datenzugriff | JWT-Validierung bei jedem geschützten Endpunkt; CORS-Whitelist |
| Korrektheit | Vollständige Validierung aller Eingaben | Bean Validation + GlobalExceptionHandler mit strukturierten Fehlerantworten |
| Wartbarkeit | Klare Trennung Backend / Frontend | Keine Datenbankzugriffe im Frontend; REST-API als einzige Schnittstelle |
| Testbarkeit | Unit- und Integrationstests | JUnit 5, Mockito, Spring Boot Test / MockMvc |
| Lesbarkeit | Clean Code | Schichten-Architektur, Javadoc auf allen public Methoden |
| Benutzerfreundlichkeit | Klar verständliche UI | Formular-Feedback, übersichtliche Kartenansicht, eindeutige Match-Anzeige |
| Verfügbarkeit | Schneller Start | Beide Apps starten in < 30 Sekunden |

---


## 9. Globale Testszenarien und Testfälle

### Testfall 1 – Registrierung und JWT-Login
- **Vorbedingung:** Kein Benutzer mit diesem Namen vorhanden
- **Aktion:** POST `/api/auth/register` mit Benutzerdaten; dann POST `/api/auth/login`
- **Erwartetes Ergebnis:** HTTP 201 bei Registrierung; HTTP 200 mit JWT-Token beim Login

### Testfall 2 – Geschützter Endpunkt ohne Token
- **Vorbedingung:** –
- **Aktion:** GET `/api/profile/me` ohne Authorization-Header
- **Erwartetes Ergebnis:** HTTP 401 Unauthorized

### Testfall 3 – CORS-Preflight-Request
- **Vorbedingung:** Backend läuft auf Port 8080
- **Aktion:** Browser sendet OPTIONS-Request von `localhost:8081` an `localhost:8080/api/profile/me`
- **Erwartetes Ergebnis:** HTTP 200; Response-Header enthält `Access-Control-Allow-Origin: http://localhost:8081`

### Testfall 4 – Arbeitnehmerprofil anlegen und abrufen
- **Vorbedingung:** Arbeitnehmer ist eingeloggt (JWT vorhanden)
- **Aktion:** PUT `/api/profile/me` mit vollständigen Profildaten
- **Erwartetes Ergebnis:** HTTP 200; Daten korrekt in DB gespeichert; GET `/api/profile/me` liefert identische Daten zurück

### Testfall 5 – Stellenangebot anlegen
- **Vorbedingung:** Arbeitgeber ist eingeloggt
- **Aktion:** POST `/api/jobs` mit gültigen Stellendaten
- **Erwartetes Ergebnis:** HTTP 201; Stellenangebot wird gespeichert und dem Arbeitgeber zugeordnet

### Testfall 6 – Swipe durch Arbeitnehmer
- **Vorbedingung:** Arbeitnehmer ist eingeloggt; Stellenangebote vorhanden
- **Aktion:** POST `/api/swipes/job/{jobId}` mit Entscheidung `LIKE`
- **Erwartetes Ergebnis:** HTTP 200; Swipe-Entscheidung wird gespeichert

### Testfall 7 – Beidseitiges Match entsteht
- **Vorbedingung:** Arbeitnehmer hat ein Stellenangebot mit `LIKE` bewertet; Arbeitgeber bewertet dasselbe Arbeitnehmerprofil ebenfalls mit `LIKE`
- **Aktion:** POST `/api/swipes/profile/{userId}` mit Entscheidung `LIKE`
- **Erwartetes Ergebnis:** HTTP 200; ein neuer Match-Eintrag wird erzeugt

### Testfall 8 – Admin sieht alle Stellenangebote und Benutzer
- **Vorbedingung:** Admin ist eingeloggt
- **Aktion:** GET `/api/admin/overview`
- **Erwartetes Ergebnis:** HTTP 200; Übersicht mit Benutzern, Stellenangeboten und Matches

### Testfall 9 – Arbeitnehmer kann nicht auf Admin-Endpunkt zugreifen
- **Vorbedingung:** Arbeitnehmer ist eingeloggt
- **Aktion:** GET `/api/admin/overview` mit Arbeitnehmer-JWT
- **Erwartetes Ergebnis:** HTTP 403 Forbidden

### Testfall 10 – Filterfunktion anwenden (W05)
- **Vorbedingung:** Mehrere Stellenangebote oder Arbeitnehmerprofile vorhanden
- **Aktion:** GET `/api/jobs?category=IT&location=Wien`
- **Erwartetes Ergebnis:** HTTP 200; nur passende Einträge werden zurückgegeben

### Testfall 11 – Unit Test: JWT-Generierung und Validierung (automatisiert)
- **Typ:** JUnit 5 Unit Test
- **Beschreibung:** `JwtService.generateToken()` erzeugt valides Token; `JwtService.isTokenValid()` gibt `true` zurück; abgelaufenes Token gibt `false`

### Testfall 12 – Unit Test: Match-Erzeugung (automatisiert)
- **Typ:** JUnit 5 Unit Test im Service-Layer
- **Beschreibung:** `MatchService` erzeugt genau dann einen Match-Eintrag, wenn beide Seiten dieselbe Beziehung mit `LIKE` bewertet haben

---

## 10. Entwicklungsumgebung

### 10.1 Software

| Tool | Version / Zweck |
|------|----------------|
| JDK | 21 |
| Spring Boot | 3.2.x (beide Projekte) |
| Maven | 3.9.x |
| IntelliJ IDEA | 2024.x |
| Git | 2.x |
| H2 Database | via Spring Boot Starter (nur im Backend) |
| Spring Security | 6.x (JWT via `jjwt`-Bibliothek) |
| Thymeleaf | via Spring Boot Starter (nur im Frontend) |
| Bootstrap | 5.3.x |
| Browser DevTools | Chrome/Firefox – für CORS-Visualisierung |

### 10.2 Hardware

- Entwickler-PC, mind. 8 GB RAM (beide Apps laufen gleichzeitig)
- Internetzugang für Maven-Dependencies (einmalig)

### 10.3 Entwicklungsschnittstellen

| Schnittstelle | Zweck |
|--------------|-------|
| `mvn spring-boot:run` | Start beider Applikationen (je in eigenem Terminal) |
| H2-Konsole (`/h2-console`) | Datenbankinspektion während Entwicklung (nur Backend, nur Dev-Profil) |
| REST-Client (z.B. Bruno, Postman) | Direktes Testen der Backend-API unabhängig vom Frontend |
| Git | Zwei Repositories (oder ein Mono-Repo mit zwei Maven-Modulen) |

---


## 11. Ergänzungen

### CORS – Technische Hintergründe (Lernziel)

**Was ist CORS?**
CORS (Cross-Origin Resource Sharing) ist ein Sicherheitsmechanismus des Browsers. Sendet eine Webseite unter `http://localhost:8081` eine JavaScript-Anfrage an `http://localhost:8080`, blockiert der Browser diese standardmäßig. Das Backend muss explizit durch HTTP-Header mitteilen, welche Origins erlaubt sind.

**Umsetzung im Projekt:**

```java
// jobswiper-backend: CorsConfig.java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:8081")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false)
            .maxAge(3600);
    }
}
```

### JWT-Authentifizierungsfluss

```text
1. POST /api/auth/login → Backend prüft Credentials → liefert JWT
2. Frontend speichert JWT im SessionStorage
3. Jeder folgende API-Call: Header "Authorization: Bearer <token>"
4. Backend-Filter validiert Token bei jedem Request
5. Nach 8h: Token abgelaufen → Frontend leitet auf Login-Seite um
```

### Projektstruktur

```text
jobswiper/
├── jobswiper-backend/          ← Maven-Projekt 1 (Port 8080)
│   ├── src/main/java/at/bbrz/jobswiper/backend/
│   │   ├── config/             (SecurityConfig, CorsConfig, JwtConfig)
│   │   ├── controller/         (AuthController, ProfileController, JobController, SwipeController, AdminController)
│   │   ├── dto/                (LoginRequest, JwtResponse, EmployeeProfileDto, JobOfferDto, SwipeDto, ...)
│   │   ├── entity/             (User, EmployeeProfile, EmployerProfile, WorkExperience, JobOffer, SwipeDecision, Match)
│   │   ├── exception/          (GlobalExceptionHandler, ResourceNotFoundException)
│   │   ├── repository/         (UserRepository, EmployeeProfileRepository, JobOfferRepository, MatchRepository, ...)
│   │   └── service/            (AuthService, JwtService, ProfileService, JobService, SwipeService, MatchService)
│   └── src/main/resources/
│       ├── application.properties
│       └── data.sql
│
└── jobswiper-frontend/         ← Maven-Projekt 2 (Port 8081)
    ├── src/main/java/at/bbrz/jobswiper/frontend/
    │   ├── config/             (WebClientConfig – konfiguriert RestTemplate mit Backend-URL)
    │   ├── controller/         (PageController, ProfilePageController, JobPageController, MatchPageController)
    │   └── service/            (ApiClientService – alle Calls an das Backend)
    └── src/main/resources/
        ├── application.properties  (backend.url=http://localhost:8080)
        └── templates/          (Thymeleaf HTML-Templates)
```

### Sicherheitskonzept

| Maßnahme | Umsetzung |
|----------|----------|
| Authentifizierung | JWT (HS256, 8h Gültigkeit) |
| Passwörter | BCrypt (Stärke 12) |
| CORS | Whitelist: nur Frontend-Origin erlaubt |
| Autorisierung | Spring Security Method-Security (`@PreAuthorize`) |
| Eingabevalidierung | Bean Validation auf allen DTOs |
| SQL-Injection | Ausschließlich JPA/JPQL-Abfragen |
| XSS | Thymeleaf escaped standardmäßig; kein `th:utext` auf Benutzerdaten |
| Öffentliche Endpunkte | Nur dort freigegeben, wo sie fachlich erforderlich sind |

**Konzept Weiterentwicklung:** HTTPS (TLS), Chat-Funktion nach Match, Rate-Limiting auf Login-Endpunkt, Refresh-Token-Mechanismus, Audit-Log.
