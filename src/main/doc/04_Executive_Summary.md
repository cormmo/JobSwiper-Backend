# Executive Summary
## JobSwiper – Webbasiertes Matching-System für Arbeitnehmer und Arbeitgeber

---

## Das Problem

Der Prozess der Jobsuche hat sich trotz Digitalisierung nur teilweise weiterentwickelt. Bewerbungen erfolgen häufig weiterhin über standardisierte E-Mail-Verfahren oder komplexe Online-Portale, die für viele Nutzer unübersichtlich und zeitaufwendig sind.

- Bewerbungen sind oft langwierig und formalisiert
- Plattformen bieten wenig intuitive Möglichkeiten für schnellen Erstkontakt
- Arbeitgeber müssen viele unpassende Bewerbungen sichten
- Arbeitnehmer erhalten wenig direktes Feedback oder schnelle Rückmeldungen

Sowohl Arbeitnehmer als auch Arbeitgeber stehen somit vor der Herausforderung, effizient passende Matches zu finden.

---

## Die Lösung

**JobSwiper** ist eine webbasierte Plattform, die den Bewerbungsprozess durch ein modernes, swipe-basiertes Matching-System vereinfacht.

### Architektur: Zwei getrennte Spring Boot Anwendungen

Das System besteht aus zwei eigenständigen Anwendungen:

```
jobswiper-backend  (Port 8080)  →  REST-API, Datenbank, Authentifizierung
jobswiper-frontend (Port 8081)  →  Benutzeroberfläche, kommuniziert über REST
```

Diese Trennung erfordert eine explizite **CORS-Konfiguration**, wodurch ein zentrales Konzept moderner Webentwicklung praxisnah demonstriert wird.

### Swipe-basiertes Matching: Das Kernelement

JobSwiper verwendet ein intuitives Swipe-System:

- Arbeitnehmer bewerten Stellenangebote (Like/Dislike)
- Arbeitgeber bewerten Arbeitnehmerprofile
- Bei beidseitigem Interesse entsteht automatisch ein Match
- Matches können anschließend übersichtlich eingesehen werden

Dadurch wird der Bewerbungsprozess deutlich beschleunigt und vereinfacht.

---

## Kernfunktionen

| Funktion | Beschreibung |
|----------|-------------|
| Benutzerprofile | Arbeitnehmer- und Arbeitgeberprofile mit relevanten Informationen |
| Stellenangebote | Erstellung und Verwaltung von Jobangeboten |
| Swipe-Matching | Intuitive Like/Dislike-Interaktion |
| Match-System | Automatische Erkennung von beidseitigem Interesse |
| JWT-Authentifizierung | Sichere tokenbasierte Anmeldung |
| CORS-Architektur | Zwei getrennte Anwendungen mit API-Kommunikation |
| Admin-Übersicht | Verwaltung von Benutzern, Jobs und Matches |

---

## Technologiestack

| Bereich | Technologie |
|---------|------------|
| Sprache | Java 21 |
| Backend-Framework | Spring Boot 3, Spring Security 6, Spring Data JPA |
| Authentifizierung | JWT (jjwt-Bibliothek) |
| Datenbank | H2 (embedded, Datei-Modus) |
| Frontend-Framework | Spring Boot 3 + Thymeleaf + Bootstrap 5 |
| Build | Maven |
| Tests | JUnit 5, Mockito, Spring Boot Test / MockMvc |

---

## Warum JobSwiper für die LAP?

Das Projekt verbindet praxisrelevante Themen der modernen Softwareentwicklung mit einem realitätsnahen Anwendungsfall:

- **CORS** wird praktisch demonstriert durch getrennte Frontend-/Backend-Architektur
- **JWT-Authentifizierung** entspricht Industriestandard für REST-APIs
- **Zwei-Projekt-Architektur** bildet reale Systemarchitekturen ab
- **Swipe-Matching-System** zeigt innovative und benutzerfreundliche UI/UX-Konzepte
- Alle Pflichtanforderungen der LAP werden erfüllt (eigenständige App, Datenbank, responsives Web, Sicherheit, Java, ≥ 50h Programmieraufwand)

---

*Dieses Projekt erfüllt alle Pflichtanforderungen des betrieblichen Projekts nach § 11 der Ausbildungsordnung Applikationsentwicklung – Coding: eigenständig lauffähige Applikation, Datenbankanbindung (H2/JPA), webbasiert und responsive (Bootstrap 5), Sicherheitskonzept (JWT, BCrypt, CORS), Entwicklungssprache Java, Programmieraufwand ≥ 50 Stunden.*
