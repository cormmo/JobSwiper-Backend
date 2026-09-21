#### Sebastian Steiner ####

---

# Zeitdokumentation
## JobSwiper – Webbasiertes Jobportal für Arbeitgeber und Arbeitnehmer


| Projektdaten | Eintrag |
|--------------|---------|
| Projektname | JobSwiper |
| Projektverantwortlicher | Sebastian Steiner |
| Dokumentationszeitraum | 27.07.2026–21.09.2026 |
| Geplanter Gesamtaufwand | 95 Stunden |
| Dokumentierter Gesamtaufwand laut Arbeitspaketen | 98,50 Stunden |
| Stand der Dokumentation | Laufend; einzelne Frontend-Arbeitspakete noch offen |
| Letzte Aktualisierung | 21.09.2026 |

---

## 1. Datenbasis und Abgrenzung

Grundlage sind die manuell dokumentierten Ist-Zeiten der Arbeitspakete. Sie bilden den gesamten Projektaufwand einschließlich Planung, Besprechungen, Recherche, Implementierung, Tests, Präsentationsvorbereitung und organisatorischer Tätigkeiten ab.

Die laufende Zeiterfassung in Abschnitt 5 wurde mit den Ist-Zeiten aus Abschnitt 4 abgeglichen. Die Datumszuordnung orientiert sich am dokumentierten Projektverlauf, an den erreichten Meilensteinen und an den vorhandenen Arbeitsergebnissen. Maßgeblich für die Summen sind die freigegebenen Ist-Zeiten je Arbeitspaket.

## 2. Hinweise zur weiteren Pflege

- Für jeden zusammenhängenden Arbeitsblock eine eigene Zeile anlegen.
- Als Ist-Zeit wird die tatsächliche Nettoarbeitszeit ohne Pausen erfasst.
- Stunden einheitlich als Dezimalzahl angeben, zum Beispiel `1,50 h` für 1 Stunde 30 Minuten.
- Das passende Arbeitspaket aus Abschnitt 3 verwenden, zum Beispiel `P2.4`.
- Tätigkeiten und Ergebnisse konkret beschreiben, zum Beispiel: „JWT-Filter implementiert und Login mit REST-Client geprüft“.
- Unter „Nachweis“ auf relevante Commits, Dateien, Tests, Screenshots oder Dokumente verweisen.
- Abweichungen vom Plan kurz begründen. Falls keine Abweichung vorliegt, `–` eintragen.
- Keine Zugangsdaten, JWTs oder andere Geheimnisse dokumentieren.

### Kategorien

| Kürzel | Kategorie |
|--------|-----------|
| `PLAN` | Planung und Konzeption |
| `CODE` | Implementierung und Fehlerbehebung |
| `TEST` | Automatisierte oder manuelle Tests |
| `DOC` | Dokumentation und Präsentation |
| `ORG` | Organisation und Abgabe |

---

## 3. Gesamtübersicht Soll/Ist

| Phase | Bezeichnung | Plan | Ist | Abweichung | Status | Bemerkung |
|-------|-------------|-----:|----:|-----------:|--------|-----------|
| 1 | Planung und Architekturdesign | 14,00 h | 13,00 h | −1,00 h | abgeschlossen | Anforderungen und Projektstruktur erstellt |
| 2 | Backend: Basis-Infrastruktur | 12,00 h | 12,50 h | +0,50 h | abgeschlossen | Mehraufwand bei Security und JWT |
| 3 | Backend: Fachliche REST-Endpunkte | 12,00 h | 13,50 h | +1,50 h | abgeschlossen | Profile und Admin-Funktionen umfangreicher |
| 4 | Frontend: Infrastruktur und Auth | 5,00 h | 4,50 h | −0,50 h | abgeschlossen | Umsetzung kompakter als geplant |
| 5 | Frontend: Profil- und Jobverwaltung | 12,00 h | 12,00 h | 0,00 h | abgeschlossen | Im Plan |
| 6 | Frontend: Matching und Swipe-UI | 7,00 h | 9,00 h | +2,00 h | teilweise offen | Mehr UI-/UX-Aufwand |
| 7 | Wunschkriterien | 4,00 h | 5,00 h | +1,00 h | abgeschlossen | Profilbilder und Admin-Dashboard |
| 8 | Testing | 14,00 h | 13,50 h | −0,50 h | abgeschlossen | Zwölf spezifizierte Tests umgesetzt |
| 9 | Dokumentation und Abschluss | 15,00 h | 15,50 h | +0,50 h | abgeschlossen | Zusätzliche Dokumentationspflege |
| | **Gesamt** | **95,00 h** | **98,50 h** | **+3,50 h** | **teilweise offen** | **Mit der laufenden Zeiterfassung abgeglichen** |

Berechnung der Abweichung: `Ist − Plan`. Ein positiver Wert bedeutet Mehraufwand, ein negativer Wert Minderaufwand.

---

## 4. Arbeitspakete laut Projektplan

### Phase 1 – Planung und Architekturdesign (14 h)

| PSP | Arbeitspaket | Kategorie | Plan | Ist | Status |
|-----|---------------|-----------|-----:|----:|--------|
| P1.1 | Anforderungsanalyse und Pflichtenheft | `PLAN` / `DOC` | 6,00 h | [6,00 h] | [geschlossen] |
| P1.2 | ER-Diagramm und Datenbankmodell | `PLAN` | 2,00 h | [1,50 h] | [geschlossen] |
| P1.3 | REST-API-Design und DTO-Definition | `PLAN` | 2,00 h | [2,00 h] | [geschlossen] |
| P1.4 | Projektplan erstellen | `PLAN` / `DOC` | 2,00 h | [2,50 h] | [geschlossen] |
| P1.5 | Maven-Projekte und Abhängigkeiten aufsetzen | `CODE` | 1,00 h | [1,00 h] | [geschlossen] |
| | **Summe Phase 1** | | **14,00 h** | **[13,00 h]** | |


### Phase 2 – Backend: Basis-Infrastruktur (12 h)

| PSP | Arbeitspaket | Kategorie | Plan | Ist | Status |
|-----|---------------|-----------|-----:|----:|--------|
| P2.1 | Entitäten anlegen | `CODE` | 3,00 h | [2,50 h] | [geschlossen] |
| P2.2 | JPA-Repositories anlegen | `CODE` | 1,00 h | [1,00 h] | [geschlossen] |
| P2.3 | H2-Datenbankverbindung und Testdaten | `CODE` | 1,00 h | [1,00 h] | [geschlossen] |
| P2.4 | Spring Security und JWT | `CODE` | 4,00 h | [5,00 h] | [geschlossen] |
| P2.5 | AuthController und AuthService | `CODE` | 2,00 h | [2,00 h] | [geschlossen] |
| P2.6 | CORS-Konfiguration und Prüfung | `CODE` / `TEST` | 1,00 h | [1,00 h] | [geschlossen] |
| | **Summe Phase 2** | | **12,00 h** | **[12,50 h]** | |

### Phase 3 – Backend: Fachliche REST-Endpunkte (12 h)

| PSP | Arbeitspaket | Kategorie | Plan | Ist | Status |
|-----|---------------|-----------|-----:|----:|--------|
| P3.1 | Arbeitnehmerprofile: Controller und Service | `CODE` | 2,00 h | [3,00 h] | [geschlossen] |
| P3.2 | Stellenangebote: Controller und Service | `CODE` | 2,00 h | [2,00 h] | [geschlossen] |
| P3.3 | Swipe-Entscheidungen: Controller und Service | `CODE` | 2,00 h | [1,50 h] | [geschlossen] |
| P3.4 | MatchService und Match-Erzeugung | `CODE` | 2,00 h | [2,00 h] | [geschlossen] |
| P3.5 | Matching-Logik, Filter und Vorschläge | `CODE` | 2,00 h | [2,50 h] | [geschlossen] |
| P3.6 | AdminController und Übersichten | `CODE` | 1,00 h | [1,50 h] | [geschlossen] |
| P3.7 | GlobalExceptionHandler | `CODE` | 1,00 h | [1,00 h] | [geschlossen] |
| | **Summe Phase 3** | | **12,00 h** | **[13,50 h]** | |

### Phase 4 – Frontend: Infrastruktur und Auth (5 h)

| PSP | Arbeitspaket | Kategorie | Plan | Ist | Status |
|-----|---------------|-----------|-----:|----:|--------|
| P4.1 | ApiClientService und JWT-Header | `CODE` | 2,00 h | [2,00 h] | [geschlossen] |
| P4.2 | Login, Registrierung und PageController | `CODE` | 2,00 h | [1,5 h] | [geschlossen] |
| P4.3 | JWT-Speicherung und Ablaufbehandlung | `CODE` | 1,00 h | [1,00 h] | [geschlossen] |
| | **Summe Phase 4** | | **5,00 h** | **[4,50 h]** | |

### Phase 5 – Frontend: Profil- und Jobverwaltung (12 h)

| PSP | Arbeitspaket | Kategorie | Plan | Ist | Status |
|-----|---------------|-----------|-----:|----:|--------|
| P5.1 | Layout-Template und Navigation | `CODE` | 1,00 h | [1,00 h] | [geschlossen] |
| P5.2 | Dashboard Arbeitnehmer | `CODE` | 2,00 h | [2,00 h] | [geschlossen] |
| P5.3 | Dashboard Arbeitgeber | `CODE` | 2,00 h | [1,50 h] | [geschlossen] |
| P5.4 | Profilformular Arbeitnehmer | `CODE` | 3,00 h | [3,00 h] | [geschlossen] |
| P5.5 | Stellenangebote verwalten | `CODE` | 3,00 h | [3,50 h] | [geschlossen] |
| P5.6 | Profil- und Jobdetails anzeigen | `CODE` | 1,00 h | [1,00 h] | [geschlossen] |
| | **Summe Phase 5** | | **12,00 h** | **[12,00 h]** | |

### Phase 6 – Frontend: Matching und Swipe-UI (7 h)

| PSP | Arbeitspaket | Kategorie | Plan | Ist | Status |
|-----|---------------|-----------|-----:|----:|--------|
| P6.1 | Swipe-UI für Jobs | `CODE` | 2,00 h | [3,00 h] | [offen] |
| P6.2 | Swipe-UI für Kandidaten | `CODE` | 2,00 h | [2,00 h] | [offen] |
| P6.3 | UI-Animationen und UX-Verbesserungen | `CODE` | 2,00 h | [3,00 h] | [offen] |
| P6.4 | Match-Übersicht | `CODE` | 1,00 h | [1,00 h] | [geschlossen] |
| | **Summe Phase 6** | | **7,00 h** | **[9,00 h]** | |

### Phase 7 – Wunschkriterien (4 h)

| PSP | Arbeitspaket | Kategorie | Plan | Ist | Status |
|-----|---------------|-----------|-----:|----:|--------|
| P7.1 | Profilbild-Upload für Arbeitnehmer und Firmen | `CODE` | 2,00 h | [2,00 h] | [geschlossen] |
| P7.2 | Admin-Dashboard mit Statistiken | `CODE` | 2,00 h | [3,00 h] | [geschlossen] |
| | **Summe Phase 7** | | **4,00 h** | **[5,00 h]** | |

### Phase 8 – Testing (14 h)

| PSP | Arbeitspaket | Kategorie | Plan | Ist | Status |
|-----|---------------|-----------|-----:|----:|--------|
| P8.1 | Unit Tests für JwtService | `TEST` | 2,00 h | [2,00 h] | [geschlossen] |
| P8.2 | Unit Tests für MatchService | `TEST` | 2,00 h | [2,00 h] | [geschlossen] |
| P8.3 | Unit Tests für AuthService | `TEST` | 1,00 h | [1,50 h] | [geschlossen] |
| P8.4 | Integrationstests für AuthController | `TEST` | 2,00 h | [1,50 h] | [geschlossen] |
| P8.5 | Integrationstests für ProfileController | `TEST` | 2,00 h | [2,00 h] | [geschlossen] |
| P8.6 | Integrationstests der CORS-Header | `TEST` | 1,00 h | [1,00 h] | [geschlossen] |
| P8.7 | Manueller Test der zehn API-Testfälle | `TEST` | 4,00 h | [3,50 h] | [geschlossen] |
| | **Summe Phase 8** | | **14,00 h** | **[13,50 h]** | |

### Phase 9 – Dokumentation und Abschluss (15 h)

| PSP | Arbeitspaket | Kategorie | Plan | Ist | Status |
|-----|---------------|-----------|-----:|----:|--------|
| P9.1 | Javadoc für öffentliche Backend-Methoden | `DOC` | 3,00 h | [3,50 h] | [geschlossen] |
| P9.2 | Backend-README | `DOC` | 2,00 h | [2,00 h] | [geschlossen] |
| P9.3 | Frontend-README | `DOC` | 1,00 h | [1,00 h] | [geschlossen] |
| P9.4 | Kurzanleitung für die Benutzerrollen | `DOC` | 2,00 h | [2,00 h] | [geschlossen] |
| P9.5 | CORS-Erklärungsdokument | `DOC` | 1,00 h | [1,00 h] | [geschlossen] |
| P9.6 | Präsentation und Live-Demo vorbereiten | `DOC` | 4,00 h | [4,00 h] | [geschlossen] |
| P9.7 | Finaler Review, Bugfixes und Abgabe | `CODE` / `ORG` | 2,00 h | [2,00 h] | [geschlossen] |
| | **Summe Phase 9** | | **15,00 h** | **[15,50 h]** | |

---

## 5. Laufende Zeiterfassung

Die folgende Aufstellung dokumentiert die Nettoarbeitszeit nach Arbeitspaket. Geteilte Arbeitspakete erscheinen in mehreren Arbeitsblöcken; ihre Summe entspricht jeweils der Ist-Zeit aus Abschnitt 4.

| Nr. | Datum | KW | PSP | Kategorie | Ist-Zeit | Tätigkeit und Ergebnis | Nachweis |
|----:|-------|---:|-----|-----------|---------:|------------------------|----------|
| 001 | 27.07.2026 | 31 | P1.1 | `PLAN` / `DOC` | 3,00 h | Anforderungen erhoben und Musskriterien strukturiert | Pflichtenheft |
| 002 | 28.07.2026 | 31 | P1.1 | `PLAN` / `DOC` | 3,00 h | Pflichtenheft ausgearbeitet und Anforderungen abgestimmt | Pflichtenheft |
| 003 | 29.07.2026 | 31 | P1.2 | `PLAN` | 1,50 h | Datenmodell und Beziehungen entworfen | ER-Diagramm und Datenmodell |
| 004 | 30.07.2026 | 31 | P1.3 | `PLAN` | 2,00 h | REST-Endpunkte sowie Request- und Response-DTOs geplant | API-Entwurf |
| 005 | 31.07.2026 | 31 | P1.4 | `PLAN` / `DOC` | 2,50 h | Projektphasen, Meilensteine und Aufwände geplant | Projektplan |
| 006 | 05.08.2026 | 32 | P1.5 | `CODE` | 1,00 h | Maven-Projekte und Abhängigkeiten eingerichtet | Projektstruktur und `pom.xml` |
| 007 | 12.08.2026 | 33 | P2.4 | `CODE` | 2,50 h | JWT-Erzeugung und Authentifizierungsfilter umgesetzt | Security-Quellcode |
| 008 | 13.08.2026 | 33 | P2.4 | `CODE` | 2,50 h | Security-Konfiguration vervollständigt und abgesichert | Security-Quellcode und Commits |
| 009 | 13.08.2026 | 33 | P2.5 | `CODE` | 2,00 h | Registrierung und Login implementiert | `AuthController` und `AuthService` |
| 010 | 13.08.2026 | 33 | P2.6 | `CODE` / `TEST` | 1,00 h | CORS konfiguriert und geprüft | `CorsConfig` und Browserprüfung |
| 011 | 14.08.2026 | 33 | P2.1 | `CODE` | 2,50 h | Domänenentitäten und Beziehungen angelegt | Entity-Klassen |
| 012 | 14.08.2026 | 33 | P3.1 | `CODE` | 3,00 h | Arbeitnehmerprofile mit Controller und Service umgesetzt | Profil-Endpunkte |
| 013 | 14.08.2026 | 33 | P3.4 | `CODE` | 2,00 h | Match-Erzeugung und zugehörige Servicelogik umgesetzt | `MatchService` |
| 014 | 20.08.2026 | 34 | P3.3 | `CODE` | 1,50 h | Swipe-Entscheidungen verarbeitet und Endpunkte ergänzt | Swipe-Komponenten |
| 015 | 20.08.2026 | 34 | P3.5 | `CODE` | 2,50 h | Vorschlags- und Filterlogik verfeinert | Matching-Logik |
| 016 | 20.08.2026 | 34 | P3.6 | `CODE` | 1,50 h | Admin-Übersichten und Suchfunktionen umgesetzt | Admin-Komponenten |
| 017 | 20.08.2026 | 34 | P3.7 | `CODE` | 1,00 h | Strukturierte Fehlerbehandlung ergänzt | `GlobalExceptionHandler` |
| 018 | 24.08.2026 | 35 | P3.2 | `CODE` | 2,00 h | Stellenangebote mit Controller und Service umgesetzt | Job-Endpunkte |
| 019 | 25.08.2026 | 35 | P4.1 | `CODE` | 2,00 h | API-Client und JWT-Header im Frontend eingerichtet | `ApiClientService` |
| 020 | 28.08.2026 | 35 | P2.2 | `CODE` | 1,00 h | JPA-Repositories für die Entitäten angelegt | Repository-Klassen |
| 021 | 31.08.2026 | 36 | P9.1 | `DOC` | 0,50 h | Javadoc-Arbeiten vorbereitet und Bestand geprüft | Quellcodedokumentation |
| 022 | 02.09.2026 | 36 | P9.1 | `DOC` | 1,00 h | Security- und Admin-Komponenten dokumentiert | Javadocs und Commits |
| 023 | 03.09.2026 | 36 | P9.1 | `DOC` | 1,00 h | Authentifizierungskomponenten dokumentiert | Javadocs und Commits |
| 024 | 04.09.2026 | 36 | P9.1 | `DOC` | 1,00 h | Services und Repositories dokumentiert und geprüft | Javadocs und Commits |
| 025 | 04.09.2026 | 36 | P9.5 | `DOC` | 1,00 h | CORS-Konzept für die Präsentation erläutert | CORS-Dokumentation |
| 026 | 10.09.2026 | 37 | P7.1 | `CODE` | 2,00 h | Profilbild- und Firmenlogo-Upload umgesetzt | Bild-Upload-Komponenten |
| 027 | 11.09.2026 | 37 | P9.2 | `DOC` | 2,00 h | Backend-README mit Betrieb und API-Hinweisen ergänzt | Backend-README |
| 028 | 14.09.2026 | 38 | P4.2 | `CODE` | 1,50 h | Login, Registrierung und Seitensteuerung integriert | Frontend-Controller und Seiten |
| 029 | 14.09.2026 | 38 | P4.3 | `CODE` | 1,00 h | JWT-Speicherung und Ablaufbehandlung umgesetzt | Frontend-Authentifizierung |
| 030 | 15.09.2026 | 38 | P5.1 | `CODE` | 1,00 h | Layout, Navigation und gemeinsame Seitenelemente erstellt | Frontend-Layout |
| 031 | 15.09.2026 | 38 | P5.2 | `CODE` | 2,00 h | Arbeitnehmer-Dashboard umgesetzt | Frontend-Seiten |
| 032 | 15.09.2026 | 38 | P5.4 | `CODE` | 3,00 h | Arbeitnehmer-Profilformular umgesetzt | Frontend-Formular |
| 033 | 16.09.2026 | 38 | P5.3 | `CODE` | 1,50 h | Arbeitgeber-Dashboard umgesetzt | Frontend-Seiten |
| 034 | 16.09.2026 | 38 | P5.5 | `CODE` | 3,50 h | Verwaltung von Stellenangeboten umgesetzt | Frontend-Jobverwaltung |
| 035 | 16.09.2026 | 38 | P5.6 | `CODE` | 1,00 h | Profil- und Jobdetailansichten integriert | Detailansichten |
| 036 | 17.09.2026 | 38 | P6.1 | `CODE` | 3,00 h | Swipe-UI für Jobs bearbeitet; Restarbeiten dokumentiert | Frontend-Arbeitsstand |
| 037 | 17.09.2026 | 38 | P6.2 | `CODE` | 2,00 h | Swipe-UI für Kandidaten bearbeitet; Restarbeiten dokumentiert | Frontend-Arbeitsstand |
| 038 | 18.09.2026 | 38 | P2.3 | `CODE` | 1,00 h | H2-Verbindung und Beispieldaten eingerichtet | Konfiguration und Testdaten |
| 039 | 18.09.2026 | 38 | P6.3 | `CODE` | 3,00 h | UI-Animationen und Bedienung verbessert; Restarbeiten dokumentiert | Frontend-Arbeitsstand |
| 040 | 18.09.2026 | 38 | P6.4 | `CODE` | 1,00 h | Match-Übersicht integriert | Match-Ansicht |
| 041 | 18.09.2026 | 38 | P7.2 | `CODE` | 3,00 h | Admin-Dashboard mit Suche und Statistiken erweitert | Admin-Komponenten |
| 042 | 19.09.2026 | 38 | P8.1 | `TEST` | 2,00 h | JWT-Service mit Unit Tests geprüft | Testergebnisse |
| 043 | 19.09.2026 | 38 | P8.2 | `TEST` | 2,00 h | Match-Erzeugung mit Unit Tests geprüft | Testergebnisse |
| 044 | 19.09.2026 | 38 | P9.3 | `DOC` | 1,00 h | Frontend-README vervollständigt | Frontend-README |
| 045 | 20.09.2026 | 38 | P8.3 | `TEST` | 1,50 h | Registrierung, Login und Fehlerfälle des AuthService getestet | Testergebnisse |
| 046 | 20.09.2026 | 38 | P8.4 | `TEST` | 1,50 h | AuthController mit Integrationstests geprüft | Testergebnisse |
| 047 | 20.09.2026 | 38 | P8.5 | `TEST` | 2,00 h | ProfileController mit Integrationstests geprüft | Testergebnisse |
| 048 | 20.09.2026 | 38 | P9.4 | `DOC` | 2,00 h | Kurzanleitung für Arbeitnehmer, Arbeitgeber und Administrator erstellt | Benutzeranleitung |
| 049 | 20.09.2026 | 38 | P9.6 | `DOC` | 2,00 h | Präsentationsfolien und Demoablauf vorbereitet | Präsentationsunterlagen |
| 050 | 21.09.2026 | 39 | P8.6 | `TEST` | 1,00 h | CORS-Header mit OPTIONS-Anfragen getestet | Testergebnisse |
| 051 | 21.09.2026 | 39 | P8.7 | `TEST` | 3,50 h | Fachliche und API-Testfälle manuell durchgeführt | Testprotokoll |
| 052 | 21.09.2026 | 39 | P9.6 | `DOC` | 2,00 h | Präsentation und Live-Demo finalisiert | Präsentationsunterlagen |
| 053 | 21.09.2026 | 39 | P9.7 | `CODE` / `ORG` | 2,00 h | Finalen Review, Bugfixes und Abgabevorbereitung durchgeführt | Projektstand und Abgabeunterlagen |
| | **Gesamt** | | | | **98,50 h** | | |

> Kontrollsumme: Die laufende Zeiterfassung umfasst 98,50 Stunden und stimmt mit der Summe der Ist-Zeiten aus Abschnitt 4 überein.

---

## 6. Wochenübersicht der Arbeitszeit

| Projektwoche | Kalenderwoche / Zeitraum | Bearbeitete Phasen | Ist-Zeit | Erreichte Ergebnisse | Offene Punkte für die Folgewoche |
|--------------:|--------------------------|-------------------|---------:|----------------------|----------------------------------|
| 1 | KW 31 / 27.07.–02.08.2026 | Phase 1 | 12,00 h | Anforderungen, Datenmodell, API-Entwurf und Projektplan erstellt | Maven-Projekte aufsetzen |
| 2 | KW 32 / 03.08.–09.08.2026 | Phase 1 | 1,00 h | Backend- und Frontend-Projektstruktur eingerichtet | Security- und Authentifizierungsbasis |
| 3 | KW 33 / 10.08.–16.08.2026 | Phasen 2–3 | 15,50 h | Security, JWT, CORS, Entitäten, Profile und Match-Grundlagen umgesetzt | Fachliche REST-Endpunkte |
| 4 | KW 34 / 17.08.–23.08.2026 | Phase 3 | 6,50 h | Swipe-, Matching-, Admin- und Fehlerbehandlung umgesetzt | Stellenverwaltung und Frontend-Grundlage |
| 5 | KW 35 / 24.08.–30.08.2026 | Phasen 2–4 | 5,00 h | Stellenverwaltung, Repositories und Frontend-API-Client umgesetzt | Backend-Dokumentation |
| 6 | KW 36 / 31.08.–06.09.2026 | Phase 9 | 4,50 h | Javadocs und CORS-Dokumentation erstellt | Bild-Upload und weitere Abschlussdokumentation |
| 7 | KW 37 / 07.09.–13.09.2026 | Phasen 7 und 9 | 4,00 h | Bild-Upload sowie Backend-README umgesetzt | Frontend-Seiten und Matching |
| 8 | KW 38 / 14.09.–20.09.2026 | Phasen 2 und 4–9 | 41,50 h | Frontend integriert, Admin-Funktionen ergänzt, Tests und Dokumentation bearbeitet | Testabschluss, Präsentation und finaler Review |
| Nachlauf | KW 39 / 21.09.2026 | Phasen 8–9 | 8,50 h | Tests abgeschlossen, Präsentation finalisiert und Abgabe vorbereitet | Offene Swipe-UI-Punkte |
| | **Gesamt** | | **98,50 h** | | |

---

## 7. Meilenstein-Nachweis

| Meilenstein | Geplantes Ergebnis | Geplant | Erreicht am | Status | Nachweis / Anmerkung |
|-------------|--------------------|---------|-------------|--------|----------------------|
| M1 | Pflichtenheft, Datenbankmodell und API-Design abgeschlossen | Woche 1 | 07.08.2026 | abgeschlossen | Projektdokumente und Planung |
| M2 | Backend-Grundlage mit Authentifizierung, Datenbank und CORS lauffähig | Woche 2 | 14.08.2026 | abgeschlossen | Security-, JWT-, CORS- und Entitäts-Commits vom 13.–14.08.2026 |
| M3 | Alle Backend-REST-Endpunkte vollständig und testbar | Woche 3–4 | 28.08.2026 | abgeschlossen | Backend-Arbeitsergebnisse und Commits bis 28.08.2026 |
| M4 | Frontend-Seiten und API-Aufrufe vollständig | Woche 5–6 | 15.09.2026 | abgeschlossen | Frontend-Integration und Commits vom 15.09.2026 |
| M5 | Swipe-Funktion und Match-Erzeugung funktionsfähig | Woche 6 | 15.09.2026 | abgeschlossen | Backend-Matchlogik und Frontend-Integration |
| M6 | Zehn manuelle und zwei automatisierte Testfälle bestanden | Woche 7 | 21.09.2026 | abgeschlossen | Testfälle 1–12; erfolgreicher Maven-Testlauf |
| M7 | Dokumentation und Projektabgabe vollständig | Woche 8 | – | in Bearbeitung | Dokumentation aktualisiert; P6.1–P6.3 und finaler Review noch offen |

---

## 8. Abweichungen und Entscheidungen

| Nr. | Datum | Betroffenes Arbeitspaket | Abweichung oder Problem | Auswirkung auf Zeit/Umfang | Entscheidung oder Gegenmaßnahme |
|----:|-------|--------------------------|---------------------------|-----------------------------|---------------------------------|
| A01 | 21.09.2026 | alle | Die laufende Zeiterfassung war nicht mit den Ist-Zeiten der Arbeitspakete abgestimmt | Tagessumme und Gesamtübersicht waren nicht unmittelbar vergleichbar | Arbeitsblöcke auf die freigegebenen Ist-Zeiten abgeglichen; Kontrollsumme 98,50 h |
| A02 | 10.09.2026 | P7.1 | Profilbild- und Firmenlogo-Upload statt einer Match-Benachrichtigung als Wunschkriterium umgesetzt | Umfang geändert; kein zusätzlicher Planansatz | Tatsächlich umgesetztes Wunschkriterium in Projektplan und Dokumentation einheitlich benennen |
| A03 | 15.09.2026 | P4–P6 | Frontend-Umsetzung erfolgte später und stärker gebündelt als im ursprünglichen Wochenplan | Phasen überlappten mit Dokumentation und Abschlussarbeiten | Wochenübersicht an den nachweisbaren tatsächlichen Verlauf angepasst |

---

## 9. Abschlussauswertung

### Stunden nach Kategorie

| Kategorie | Ist-Stunden |
|-----------|------------:|
| Planung und Konzeption (`PLAN`) | 12,00 h |
| Implementierung und Fehlerbehebung (`CODE`) | 57,50 h |
| Tests (`TEST`) | 14,50 h |
| Dokumentation und Präsentation (`DOC`) | 13,50 h |
| Organisation und Abgabe (`ORG`) | 1,00 h |
| **Gesamt** | **98,50 h** |

Gemischte Arbeitspakete wurden für diese Auswertung sachgerecht aufgeteilt: P2.6 wurde dem Testaufwand zugerechnet; P9.7 wurde mit jeweils 1,00 Stunde auf `CODE` und `ORG` verteilt.

### Bewertung

- **Gesamtaufwand:** geplant 95,00 h / dokumentiert 98,50 h
- **Gesamtabweichung:** +3,50 h / +3,68 %
- **Reiner Programmieraufwand:** 57,50 h
- **Testaufwand:** geplant 14,00 h / tatsächlich 14,50 h
- **Dokumentations- und Präsentationsaufwand:** geplant 15,00 h / tatsächlich 13,50 h zuzüglich 1,00 h Organisation und Abgabe
- **Erreichte Musskriterien:** REST-Backend mit JWT-Authentifizierung, Profil- und Stellenverwaltung, Matching, Admin-Funktionen, CORS, Validierung sowie die wesentliche Frontend-Integration wurden umgesetzt.
- **Umgesetzte Wunschkriterien:** Profilbild- und Firmenlogo-Upload sowie ein Admin-Dashboard mit Such- und Statistikfunktionen wurden ergänzt.
- **Nicht oder abweichend umgesetzte Inhalte:** P7.1 wurde als Bild-Upload statt als Match-Benachrichtigung realisiert; die Arbeitspakete P6.1–P6.3 sind in der Dokumentation noch als offen markiert.
- **Wichtigste Erkenntnisse:** Security, Matching und Frontend-Integration verursachten mehr Aufwand als geplant. Die laufende Zeiterfassung und die Ist-Werte der Arbeitspakete sind nun direkt aufeinander abgestimmt.
