#### Sebastian Steiner ####

---
# Anmeldung zur Lehrabschlussprüfung

## Praktische Prüfung – Applikationsentwicklung - Coding

### Betriebliches Projekt

---

**Name des Prüfungskandidaten / der Prüfungskandidatin:**

> _Sebastian Steiner_

---

**Titel des Projektes:**

> **JobSwiper – Webbasiertes Jobportal für Arbeitgeber und Arbeitnehmer**

---

## Executive Summary / Kurze Beschreibung des zu erstellenden Programmes

### Welches Problem soll gelöst werden?

Der Prozess der Arbeitssuche hat sich trotz Digitalisierung nur teilweise weiterentwickelt. Bewerbungen erfolgen häufig weiterhin über standardisierte E-Mail-Verfahren oder komplexe Online-Portale, die für viele Nutzer unübersichtlich und zeitaufwendig sind. Sowohl Arbeitssuchende als auch Arbeitgeber stehen vor der Herausforderung, passende Stellen bzw. geeignete Kandidaten effizient zu finden, während gleichzeitig ein persönlicher und unkomplizierter Austausch oft fehlt.

Viele bestehende Plattformen sind stark formalisiert, erfordern umfangreiche Profileingaben und bieten wenig Flexibilität im Erstkontakt zwischen beiden Parteien. Dies führt dazu, dass potenzielle Matches nicht zustande kommen oder der Bewerbungsprozess als unnötig kompliziert empfunden wird.

**JobSwiper** löst dieses Problem durch eine moderne, webbasierte Plattform, die den Bewerbungsprozess vereinfacht und intuitiver gestaltet. Durch ein Swipe-basiertes Matching-System können sowohl Arbeitnehmer als auch Arbeitgeber schnell und unkompliziert Interesse signalisieren. Die Anwendung schafft eine niedrigschwellige, benutzerfreundliche Umgebung, die einen direkteren und lockereren Austausch zwischen beiden Parteien ermöglicht und somit die Effizienz und Attraktivität der Jobsuche deutlich erhöht.

### Wer hat dieses Problem?

Arbeitssuchende Personen sowie Unternehmen, die offene Stellen besetzen möchten. Arbeitnehmer stehen vor der Herausforderung, passende Jobangebote schnell zu finden und sich effizient zu bewerben, ohne sich durch komplexe Bewerbungsprozesse arbeiten zu müssen. Gleichzeitig suchen Arbeitgeber nach geeigneten Kandidaten, haben jedoch oft Schwierigkeiten, aus einer Vielzahl an Bewerbungen die passenden Personen herauszufiltern.

Besonders betroffen sind junge Berufseinsteiger sowie Personen in der beruflichen Neuorientierung, die einen einfachen und intuitiven Zugang zum Arbeitsmarkt benötigen. Ebenso profitieren kleine und mittelständische Unternehmen, die ohne großen administrativen Aufwand passende Mitarbeiter finden möchten.

Durch die Vereinfachung des Erstkontakts zwischen beiden Parteien adressiert JobSwiper genau diese Zielgruppen und verbessert den Matching-Prozess zwischen Arbeitgebern und Arbeitnehmern erheblich.

### USP gegenüber bestehenden Lösungen

Plattformen wie LinkedIn, Xing oder klassische Jobbörsen sind häufig stark formalisiert, überladen mit Funktionen und bieten wenig intuitive Möglichkeiten für einen schnellen Erstkontakt zwischen Arbeitgebern und Arbeitnehmern. Zudem erfordern sie meist umfangreiche Profileingaben und einen hohen Zeitaufwand, bevor erste Interaktionen stattfinden können.

**JobSwiper** hebt sich davon durch folgende Eigenschaften ab:

- **intuitives Swipe-System** – schnelles und unkompliziertes Matching ähnlich moderner Dating-Apps
- **niederschwelliger Einstieg** – reduzierte, fokussierte Profile statt komplexer Bewerbungsformulare
- **direktes Matching-Prinzip** – Kontakt entsteht erst bei beidseitigem Interesse
- **zeitsparend** – schnelle Entscheidungen ohne langwierige Bewerbungsprozesse
- **moderne Architektur** – getrennte Backend/Frontend-Struktur mit REST-API und JWT-Authentifizierung
- **flexibel erweiterbar** – Grundlage für zusätzliche Features wie Chat, Bewertungen oder Empfehlungen

Dadurch bietet JobSwiper eine deutlich vereinfachte und benutzerfreundlichere Alternative zu klassischen Jobplattformen und fördert einen effizienteren Matching-Prozess.

### Kernelemente der Lösung

- **Backend-API** (Spring Boot, Port 8080): REST-Endpunkte zur Verwaltung von Benutzerprofilen, Jobangeboten und Matching-Prozessen; CORS-konfiguriert
- **Frontend** (separates Spring Boot Projekt, Port 8081): Benutzeroberfläche mit Thymeleaf und Bootstrap 5; ermöglicht das Swipe-basierte Matching und kommuniziert ausschließlich über die REST-API mit dem Backend
- **Matching-System**: Swipe-Mechanik, bei der Arbeitgeber und Arbeitnehmer Profile bzw. Jobangebote bewerten (Like/Dislike); bei beidseitigem Interesse entsteht ein Match
- **Benutzerprofile**: Reduzierte, fokussierte Profile für Arbeitnehmer (Skills, Kurzbeschreibung, Erfahrung) und Arbeitgeber (Unternehmensinfos, offene Stellen)
- **Rollenkonzept**: Trennung zwischen Arbeitnehmern, Arbeitgebern und optional Administratoren mit unterschiedlichen Berechtigungen
- **Sicherheit**: JWT-basierte Authentifizierung, CORS-Policy und serverseitige Validierung aller Eingaben

### Zielgruppe / Anwender

Arbeitssuchende Personen und Unternehmen, die offene Stellen effizient besetzen möchten, bilden die primäre Zielgruppe von JobSwiper.

**Arbeitnehmer** nutzen die Plattform, um schnell passende Jobangebote zu finden, ihr Profil unkompliziert darzustellen und durch das Swipe-System mit potenziellen Arbeitgebern zu matchen. Besonders angesprochen werden junge Berufseinsteiger, Quereinsteiger sowie Personen in der beruflichen Neuorientierung, die einen einfachen und intuitiven Zugang zum Arbeitsmarkt suchen.

**Arbeitgeber** verwenden JobSwiper, um geeignete Kandidaten effizient zu identifizieren, ohne sich durch umfangreiche Bewerbungsunterlagen arbeiten zu müssen. Insbesondere kleine und mittelständische Unternehmen profitieren von der vereinfachten Vorauswahl und dem direkten Matching-Prinzip.

Optional können **Administratoren** (z. B. Plattformbetreiber) das System verwalten, Benutzer kontrollieren und Inhalte moderieren.

---

## Hinweis zur Einreichung

Mit diesem Dokument werden folgende Unterlagen bei der Lehrlingsstelle eingereicht:
Pflichtenheft (`02_Pflichtenheft.md`)  
Projektplan mit Zeitschätzung (`03_Projektplan_Zeitschaetzung.md`)

Der Quellcode beider Teilprojekte wird spätestens 3 Wochen vor dem Prüfungstermin offengelegt.

---

**Ort, Datum:** ************\_************, den ************\_************

**Name und Unterschrift des Prüfungskandidaten / der Prüfungskandidatin:**

---

**Name und Unterschrift des Lehrberechtigten:**

---
