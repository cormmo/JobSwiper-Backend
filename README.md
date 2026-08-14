### Sebastian Steiner
---
# JobSwiper – Webbasiertes Jobportal für Arbeitgeber und Arbeitnehmer
## LAP Projektvorschlag | Applikationsentwicklung - Coding | Betriebliches Projekt

---

## Projektidee

**Webbasiertes Matching-System für Arbeitnehmer und Arbeitgeber** mit Swipe-Mechanik zur vereinfachten Jobsuche und Kandidatenauswahl.

**Besonderheit:** Zwei getrennte Spring Boot Projekte (Backend-API + Frontend) mit CORS-Konfiguration.

**Technologiestack:**
- **Backend (Port 8080):** Java 21 · Spring Boot 3 · Spring Security · JWT · Spring Data JPA · H2
- **Frontend (Port 8081):** Java 21 · Spring Boot 3 · Thymeleaf · Bootstrap 5 · RestTemplate

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
