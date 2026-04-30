## Backend Package-Struktur (JobSwiper)

```text
at.bbrz.jobswiper.backend
├── config
│   ├── CorsConfig
│   ├── SecurityConfig
│   └── JwtConfig
│
├── controller
│   ├── AuthController
│   ├── ProfileController
│   ├── JobController
│   ├── SwipeController
│   ├── MatchController
│   └── AdminController
│
├── dto
│   ├── auth
│   ├── profile
│   ├── job
│   ├── swipe
│   └── match
│
├── entity
│   ├── User
│   ├── EmployeeProfile
│   ├── EmployerProfile
│   ├── WorkExperience
│   ├── JobOffer
│   ├── SwipeDecision
│   └── Match
│
├── enums
│   ├── Role
│   ├── SwipeType
│   └── MatchStatus
│
├── repository
│   ├── UserRepository
│   ├── EmployeeProfileRepository
│   ├── EmployerProfileRepository
│   ├── WorkExperienceRepository
│   ├── JobOfferRepository
│   ├── SwipeDecisionRepository
│   └── MatchRepository
│
├── service
│   ├── AuthService
│   ├── JwtService
│   ├── ProfileService
│   ├── JobService
│   ├── SwipeService
│   ├── MatchService
│   └── AdminService
│
├── exception
│   ├── GlobalExceptionHandler
│   ├── ResourceNotFoundException
│   ├── UnauthorizedException
│   └── BadRequestException
│
└── JobswiperBackendApplication

````
### Architektur

Das Backend folgt einer klassischen Schichtenarchitektur:

Controller → Service → Repository → Entity → Datenbank

- **Controller**: HTTP-Endpunkte
- **Service**: Geschäftslogik
- **Repository**: Datenbankzugriff (JPA)
- **Entity**: Datenbankmodell