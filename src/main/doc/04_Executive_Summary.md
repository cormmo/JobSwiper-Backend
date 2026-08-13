#### Sebastian Steiner ####

---
# Executive Summary
## JobSwiper – Webbasiertes Jobportal für Arbeitgeber und Arbeitnehmer

---

## Ausgangssituation

Der Bewerbungsprozess ist trotz Digitalisierung oft noch zeitaufwendig und stark formalisiert. Arbeitssuchende müssen sich durch komplexe Portale und lange Bewerbungsformulare arbeiten, während Arbeitgeber viele Bewerbungen sichten müssen, bevor passende Kandidaten erkennbar sind. Ein schneller, unkomplizierter Erstkontakt zwischen beiden Seiten fehlt häufig.

## Lösung

**JobSwiper** ist eine webbasierte Anwendung, die die Kontaktanbahnung zwischen Arbeitnehmern und Arbeitgebern vereinfacht. Arbeitnehmer bewerten Stellenangebote per Like oder Dislike, Arbeitgeber bewerten Arbeitnehmerprofile auf dieselbe Weise. Wenn beide Seiten Interesse zeigen, erzeugt das System automatisch ein Match, das anschließend in einer Übersicht angezeigt wird.

Die Anwendung besteht aus zwei getrennten Spring Boot Projekten:

```text
jobswiper-backend  (Port 8080)  -> REST-API, Datenbank, Authentifizierung
jobswiper-frontend (Port 8081)  -> Weboberfläche, API-Kommunikation
```

Durch diese Trennung kommuniziert das Frontend ausschließlich über HTTP mit dem Backend. Die notwendige CORS-Konfiguration ist ein bewusst gewähltes technisches Kernmerkmal und kann in der Prüfung praxisnah demonstriert werden.

## Kernfunktionen

| Bereich | Inhalt |
|---------|--------|
| Benutzerverwaltung | Registrierung, Login, Rollen für Arbeitnehmer, Arbeitgeber und Admin |
| Profile | Arbeitnehmerprofile und Arbeitgeberprofile mit relevanten Stammdaten |
| Stellenangebote | Erstellen, Bearbeiten und Deaktivieren von Jobangeboten |
| Swipe-Matching | Like/Dislike-Entscheidungen und automatische Match-Erstellung |
| Administration | Übersicht über Benutzer, Stellenangebote und Matches |
| Sicherheit | JWT-Authentifizierung, BCrypt-Passwortspeicherung, serverseitige Validierung |

## Technische Umsetzung

Das Backend wird mit Java 21, Spring Boot 4, Spring Security, Spring Data JPA und H2 umgesetzt. Es stellt eine REST-API bereit, kapselt alle Datenzugriffe und enthält die Geschäftslogik für Authentifizierung, Profile, Stellenangebote, Swipes und Matches.

Das Frontend ist ebenfalls eine eigenständige Spring Boot Anwendung mit Thymeleaf und Bootstrap 5. Es stellt ein responsives Web-Interface bereit und ruft das Backend über REST-Endpunkte auf. Tests werden mit JUnit 5, Mockito und Spring Boot Test / MockMvc umgesetzt.

## Relevanz für die LAP

JobSwiper verbindet einen verständlichen Anwendungsfall mit praxisnahen Technologien der modernen Webentwicklung. Besonders demonstrierbar sind die getrennte Frontend-/Backend-Architektur, CORS, JWT-Authentifizierung, Datenpersistenz über JPA/H2 sowie das zentrale Swipe-Matching.

Das Projekt erfüllt die Pflichtanforderungen des betrieblichen Projekts: eigenständig lauffähige Applikation, Datenbankanbindung, webbasiertes responsives Frontend, Sicherheitskonzept, Java als Entwicklungssprache und ein geplanter Programmieraufwand von mehr als 50 Stunden.
