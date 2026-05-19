#### Sebastian Steiner ####

---
# Projektplan mit Zeitschätzung
## JobSwiper – Webbasiertes Jobportal für Arbeitgeber und Arbeitnehmer

**Gesamtaufwand:** ca. 90 Stunden
**Reiner Programmieraufwand:** ca. 65 Stunden
**Testaufwand:** ca. 15 Stunden
**Dokumentation:** ca. 15 Stunden

---

## Meilensteinplan

| # | Meilenstein | Woche | Ergebnis |
|---|-------------|-------|----------|
| M1 | Anforderungen abgeschlossen, Architektur festgelegt | 1 | Pflichtenheft, Datenbankmodell, API-Design |
| M2 | Backend-Grundlage: Auth, DB, CORS lauffähig | 2 | JWT-Login funktioniert, CORS verifiziert |
| M3 | Backend vollständig: alle REST-Endpunkte | 3–4 | API vollständig, via REST-Client testbar |
| M4 | Frontend vollständig: alle Seiten, API-Calls | 5–6 | Vollständige Web-App nutzbar |
| M5 | Matching-System + Swipe-Funktion umgesetzt | 6 | Swipe-Funktion und Match-Erstellung funktionsfähig |
| M6 | Tests abgeschlossen, alle Bugs behoben | 7 | Alle 12 Testfälle bestanden |
| M7 | Dokumentation und Abgabe | 8 | Vollständige Abgabe beider Projekte |

---

## Projektstrukturplan (PSP)

### Phase 1 – Planung und Architekturdesign (14 h)

| Aufgabe | Stunden |
|---------|---------|
| Anforderungsanalyse, Pflichtenheft verfassen | 6 h |
| ER-Diagramm und Datenbankmodell entwerfen | 2 h |
| REST-API Design: Endpunkte, Request/Response-DTOs definieren | 2 h |
| Projektplan erstellen | 2 h |
| Beide Maven-Projekte aufsetzen, pom.xml + Dependencies | 1 h |
| **Summe Phase 1** | **14 h** |

### Phase 2 – Backend: Basis-Infrastruktur (12 h)

| Aufgabe | Stunden |
|---------|---------|
| Alle Entities anlegen (User, EmployeeProfile, EmployerProfile, WorkExperience, JobOffer, SwipeDecision, Match) | 3 h |
| JPA-Repositories für alle Entities | 1 h |
| H2-Datenbankverbindung + `data.sql` mit Testdaten | 1 h |
| Spring Security + JWT: `JwtService`, `JwtAuthFilter`, `SecurityConfig` | 4 h |
| `AuthController` (Register, Login) + `AuthService` | 2 h |
| **CORS-Konfiguration** (`CorsConfig`, verifiziert mit Browser DevTools) | 1 h |
| **Summe Phase 2** | **12 h** |

### Phase 3 – Backend: Fachliche REST-Endpunkte (12 h)

| Aufgabe | Stunden |
|---------|---------|
| `ProfileController` + Service: CRUD für Arbeitnehmerprofile | 2 h |
| `JobController` + Service: CRUD für Stellenangebote | 2 h |
| `SwipeController` + Service: Verarbeitung von Swipe-Entscheidungen | 2 h |
| `MatchService`: Logik zur Erzeugung von Matches | 2 h |
| Matching-Logik verfeinern (Filter + Vorschläge) | 2 h |
| `AdminController`: Benutzer- und Stellenübersicht | 1 h |
| `GlobalExceptionHandler` (strukturierte Fehler-Responses) | 1 h |
| **Summe Phase 3** | **12 h** |

### Phase 4 – Frontend: Infrastruktur und Auth (5 h)

| Aufgabe | Stunden |
|---------|---------|
| `ApiClientService`: RestTemplate konfigurieren, JWT-Header setzen | 2 h |
| Login- und Registrierungsseite + `PageController` | 2 h |
| JWT im SessionStorage speichern; automatische Weiterleitung bei abgelaufenem Token | 1 h |
| **Summe Phase 4** | **5 h** |

### Phase 5 – Frontend: Profil- und Jobverwaltung (12 h)

| Aufgabe | Stunden |
|---------|---------|
| Layout-Template (Navbar, Sidebar-Navigation, Footer) | 1 h |
| Dashboard Arbeitnehmer (Profilübersicht) | 2 h |
| Dashboard Arbeitgeber (Stellenübersicht) | 2 h |
| Profil-Formular (Arbeitnehmer) | 3 h |
| Stellenangebot-Verwaltung (Liste + Anlegen/Bearbeiten) | 3 h |
| Anzeige von Profil- und Jobdetails | 1 h |
| **Summe Phase 5** | **12 h** |

### Phase 6 – Frontend: Matching und Swipe-UI (7 h)

| Aufgabe | Stunden |
|---------|---------|
| Swipe-UI für Jobs (Arbeitnehmer) | 2 h |
| Swipe-UI für Kandidaten (Arbeitgeber) | 2 h |
| UI-Animationen und UX-Verbesserung (Swipe-Effekte) | 2 h |
| Match-Übersicht (Liste aller Matches) | 1 h |
| **Summe Phase 6** | **7 h** |

### Phase 7 – Wunschkriterien (5 h)

| Aufgabe | Stunden |
|---------|---------|
| Match-Benachrichtigung im Frontend (W01) | 2 h |
| Admin-Dashboard mit Statistiken (W04) | 2 h |
| Filterfunktion für Jobs und Profile (W05) | 1 h |
| **Summe Phase 7** | **5 h** |

### Phase 8 – Testing (14 h)

| Aufgabe | Stunden |
|---------|---------|
| Unit Tests: `JwtService` (Token generieren, validieren, abgelaufen) | 2 h |
| Unit Tests: MatchService (Match-Erzeugung) | 2 h |
| Unit Tests: `AuthService` (Registrierung, Login, Fehlerfall) | 1 h |
| Integration Tests: `AuthController` mit MockMvc | 2 h |
| Integration Tests: ProfileController mit MockMvc + JWT | 2 h |
| Integration Tests: CORS-Header (OPTIONS-Request) | 1 h |
| Manueller Test aller 12 Testfälle aus dem Pflichtenheft | 4 h |
| **Summe Phase 8** | **14 h** |

### Phase 9 – Dokumentation und Abschluss (15 h)

| Aufgabe | Stunden |
|---------|---------|
| Javadoc für alle public Methoden (Backend) | 3 h |
| README.md Backend: API-Dokumentation, Startanleitung, Testbenutzer | 2 h |
| README.md Frontend: Startanleitung, CORS-Erklärung | 1 h |
| Kurzanleitung für die 3 Benutzerrollen | 2 h |
| CORS-Erklärungsdokument (für Prüfungspräsentation) | 1 h |
| Präsentationsvorbereitung (Folien, Live-Demo-Ablauf) | 4 h |
| Finaler Code-Review, Bugfixes, Abgabe vorbereiten | 2 h |
| **Summe Phase 9** | **15 h** |

---

## Gesamtübersicht

| Phase | Bezeichnung | Stunden |
|-------|-------------|---------|
| 1 | Planung und Architekturdesign | 14 h |
| 2 | Backend: Basis-Infrastruktur | 12 h |
| 3 | Backend: Fachliche REST-Endpunkte | 12 h |
| 4 | Frontend: Infrastruktur und Auth | 5 h |
| 5 | Frontend: Profil- und Jobverwaltung | 12 h |
| 6 | Frontend: Matching und Swipe-UI | 7 h |
| 7 | Wunschkriterien | 5 h |
| 8 | Testing | 14 h |
| 9 | Dokumentation und Abschluss | 15 h |
| | **Gesamt** | **96 h** |

**Reiner Programmieraufwand** (Phase 1 Coding-Anteil + Phasen 2–7): ~60–65 h (Mindestanforderung: 50 h)

---

## Zeitlicher Ablauf (8 Wochen)

```
Woche 1:  ██████████████████████████████████████████████ Phase 1 – Planung (14h)
Woche 2:  ██████████████████████████████████████ Phase 2 – Backend Basis (12h)
Woche 3:  ██████████████████████████████████████ Phase 3 – Backend API, Teil 1 (12h)
Woche 4:  ████████████████████████ Phase 3 – Backend API, Teil 2 + Phase 4 (5h)
Woche 5:  ██████████████████████████████████████ Phase 5 – Frontend Profil- und Jobverwaltung (12h)
Woche 6:  █████████████████████████████████ Phase 6 (5h) + Phase 7 Wunschkriterien (5h)
Woche 7:  ██████████████████████████████████████ Phase 8 – Testing (12h)
Woche 8:  ██████████████████████████████████████████████ Phase 9 – Doku + Abschluss (14h)
```

---

## Risiken und Gegenmaßnahmen

| Risiko | Wahrscheinlichkeit | Gegenmaßnahme |
|--------|-------------------|---------------|
| JWT-Integration komplexer als geplant | hoch | Spring Security Docs + Beispielprojekte vorab studieren; früh (Phase 2) beginnen |
| CORS-Probleme im Browser | mittel | Browser DevTools für Diagnose; CORS-Config früh testen (Ende Phase 2) |
| Frontend-Backend-Integration schlägt fehl | mittel | API zuerst isoliert mit REST-Client (Bruno/Postman) testen |
| Zeitüberschreitung beim Frontend-Design | mittel | Wunschkriterien (z. B. Filterfunktion, Match-UI) als letztes; Puffer in Phase 9 |
| Zwei laufende Apps erhöhen Entwicklungskomplexität | mittel | Klare Port-Trennung; beide Apps per Skript starten |
