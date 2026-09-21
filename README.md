### Sebastian Steiner
---
# JobSwiper – Webbasiertes Jobportal für Arbeitgeber und Arbeitnehmer
## LAP Projektvorschlag | Applikationsentwicklung - Coding | Betriebliches Projekt

---

## Projektidee

**Webbasiertes Matching-System für Arbeitnehmer und Arbeitgeber** mit Swipe-Mechanik zur vereinfachten Jobsuche und Kandidatenauswahl.

**Besonderheit:** Zwei getrennte Spring Boot Projekte (Backend-API + Frontend) mit CORS-Konfiguration.

**Technologiestack:**
- **Backend (Port 8080):** Java 21 · Spring Boot 4 · Spring Security · JWT · Spring Data JPA · H2
- **Frontend (Port 8081):** Java 21 · Spring Boot · Thymeleaf · Bootstrap 5 · RestTemplate

---

## Enthaltene Dokumente

| Datei | Inhalt | Zweck |
|-------|--------|-------|
| `01_Anmeldung_betriebliches_Projekt.pdf` | Ausgefülltes Anmeldeformular | Bei Lehrlingsstelle einzureichen |
| `02_Pflichtenheft.pdf` | Vollständiges Pflichtenheft (alle 11 Abschnitte) | Bei Lehrlingsstelle einzureichen |
| `03_Projektplan_Zeitschaetzung.pdf` | Meilensteinplan + PSP mit Stundenaufstellung | Bei Lehrlingsstelle einzureichen |
| `04_Executive_Summary.pdf` | 2-seitige Projektbeschreibung nach den Leitfragen | Teil des Anmeldeformulars |

---

## Erfüllte Pflichtanforderungen

Eigenständig lauffähige Applikation (zwei Spring Boot Apps).  
Datenbankanbindung (H2 im Backend via JPA).  
Webbasiert und responsive (Thymeleaf + Bootstrap 5).  
Sicherheitskonzept (JWT, BCrypt, CORS-Whitelist, Rollen).  
Entwicklungssprache Java.  
Programmieraufwand ≥ 65 Stunden (geplant: ~51h rein Coding).  
Gesamtaufwand ca. 90 Stunden.

---

## Technische Highlights für die Prüfung

- **CORS live demonstrierbar** im Browser-Netzwerk-Tab (Preflight-Request sichtbar)
- **JWT-Flow** im Browser-DevTools (Authorization-Header, Token-Inhalt)
- **Swipe-Matching-System** als zentrales Feature (Like/Dislike + Match-Erstellung)
- **Zwei getrennte Anwendungen** (Frontend + Backend) wie in realen Projekten

## Backend starten

Voraussetzung ist ein installiertes JDK 21. Maven muss nicht separat installiert sein, da der Maven Wrapper im Repository enthalten ist.

Normaler Start unter macOS oder Linux:

```bash
./mvnw spring-boot:run
```

Unter Windows:

```powershell
mvnw.cmd spring-boot:run
```

Das Backend ist anschließend unter `http://localhost:8080` erreichbar. Beim normalen Start wird die persistente H2-Datenbank `./data/jobswiper` verwendet. Das Frontend ist ein separates Projekt und muss separat auf Port 8081 gestartet werden.

### Entwicklungsprofil mit Beispieldaten

Für lokale Benutzertests kann das Entwicklungsprofil aktiviert werden:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Unter Windows lautet der entsprechende Befehl:

```powershell
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

Auch das Entwicklungsprofil verwendet `./data/jobswiper`. Zusätzlich aktiviert es die H2-Konsole unter `http://localhost:8080/h2-console` und lädt idempotente Beispieldaten aus `data-dev.sql`. Dabei werden 30 aktive Stellenangebote angelegt.

Folgende lokale Testkonten stehen zur Verfügung:

| Rolle | Benutzername | Passwort |
|-------|--------------|----------|
| Arbeitnehmer | `test` | `testtest` |
| Administrator | `admin` | `adminadmin` |
| Arbeitgeber | `ag` | `ag123456` |

Die Beispieldaten sind ausschließlich für lokale Entwicklung und Demonstrationen gedacht. Bei jedem Start mit dem Profil `dev` werden Passwort, Rolle und Aktivstatus dieser drei Konten auf die dokumentierten Werte gesetzt. Bereits vorhandene Profile und Stellenangebote werden nicht dupliziert.

### Tests ausführen

```bash
./mvnw test
```

Unter Windows:

```powershell
mvnw.cmd test
```
