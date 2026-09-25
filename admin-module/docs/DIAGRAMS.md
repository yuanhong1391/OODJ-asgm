# Admin module diagrams

These diagrams describe the implemented admin module. The ERD documents text-file relationships; no database is used. A patient and doctor are role-constrained records in `USER`, not separate user tables.

## Entity relationship diagram

```mermaid
erDiagram
    USER ||--o| ASSIGNMENT : "doctor role"
    USER ||--o{ ASSIGNMENT : "manager role"
    USER ||--o{ REQUEST : "requesting doctor"
    USER ||--o{ REQUEST : "patient"
    USER ||--o{ ALLOCATION : "patient"
    USER o|--o{ ALLOCATION : "optional doctor"
    ASSET o|--o{ ASSET : "ward contains beds"
    ASSET ||--o{ ALLOCATION : allocated
    REQUEST o|--o{ ALLOCATION : "optional test request"
    USER {
        string id PK
        string role
        string name
        string username UK
        string passwordHash
        string email
        string phone
        boolean active
    }
    ASSIGNMENT {
        string doctorId PK,FK
        string managerId FK
    }
    ASSET {
        string id PK
        string type
        string name UK
        string parentId FK
        string status
    }
    ALLOCATION {
        string id PK
        string assetId FK
        string patientId FK
        string doctorId FK
        string requestId FK
        datetime start
        datetime end
        string status
    }
    REQUEST {
        string id PK
        string doctorId FK
        string patientId FK
        string type
        string details
        string status
    }
    RATE {
        string id PK
        string consultationType UK
        decimal amount
        boolean active
    }
    INSURANCE_NETWORK {
        string id PK
        string name UK
        boolean active
    }
```

RATE and INSURANCE_NETWORK are independent configuration records until teammates integrate appointments/billing and patient policies. One request can have cancelled historical allocations, but only one non-cancelled allocation. A BED must have exactly one WARD parent; other asset types have no parent.

## Class diagram

```mermaid
classDiagram
    class User {
        <<abstract>>
        -String id
        -String name
        -String username
        -Role role
        +getId() String
        +getName() String
        +getRole() Role
        +canAdminister() boolean
        +welcomeMessage() String
    }
    class AdminStaff {
        +canAdminister() boolean
        +welcomeMessage() String
    }
    class ClinicalUser {
        +canAdminister() boolean
        +welcomeMessage() String
    }
    class HospitalService {
        -TextStore store
        -User current
        +setup(name, username, password)
        +login(username, password) User
        +logout()
        +list(table) List
        +save(table, values)
        +delete(table, id)
        +submitRequest(patientId, type, details)
    }
    class TextStore {
        -Path folder
        -FileLock lock
        +read(table) List
        +save(table, rows)
        +close()
    }
    class Passwords {
        ~hash(password) String
        ~matches(password, stored) boolean
    }
    class MainFrame {
        -HospitalService service
        -showLogin()
        -dashboard()
        -showTable(key)
        -editDialog(key, existing, preset)
    }
    class EditorPanel {
        #fields() Map
        +values() Map
        +setValues(values)
        +options(key, labels)
        +lock(keys)
    }
    User <|-- AdminStaff
    User <|-- ClinicalUser
    HospitalService --> User : session
    HospitalService --> TextStore : persists through
    HospitalService ..> Passwords : authenticates with
    MainFrame --> HospitalService : invokes
    MainFrame ..> EditorPanel : displays
    EditorPanel <|-- LoginForm
    EditorPanel <|-- SetupForm
    EditorPanel <|-- UserForm
    EditorPanel <|-- AssignmentForm
    EditorPanel <|-- AssetForm
    EditorPanel <|-- AllocationForm
    EditorPanel <|-- RequestForm
    EditorPanel <|-- RateForm
    EditorPanel <|-- InsuranceForm
```

`ClinicalUser` represents manager, doctor and patient identities through its role enum. Their separate domain behaviours and dashboards belong to teammates. Asset/request/rate records currently use field maps validated by `HospitalService`; the ERD entities are not all Java classes. This distinction should be preserved in the report.

## Use case diagram source

The following PlantUML source expresses actors and the admin use cases. Render it with a PlantUML tool if your report needs a UML image. The Mermaid overview below can be read directly without a renderer installation.

```plantuml
@startuml
left to right direction
actor "Admin Staff" as Admin
actor Doctor
rectangle "HMS Admin Module" {
  usecase "Log in / Log out" as Login
  usecase "Manage end users" as Users
  usecase "Assign doctor to manager" as Assign
  usecase "Manage physical assets" as Assets
  usecase "Allocate facility or bed" as Allocate
  usecase "Configure consultation rates" as Rates
  usecase "Manage accepted insurance" as Insurance
  usecase "Record incoming test request" as Intake
  usecase "Submit test request via service" as Submit
  usecase "Schedule test request" as Schedule
  usecase "Close test request" as Close
  usecase "Validate availability" as Availability
}
Admin --> Login
Admin --> Users
Admin --> Assign
Admin --> Assets
Admin --> Allocate
Admin --> Rates
Admin --> Insurance
Admin --> Intake
Admin --> Schedule
Admin --> Close
Doctor --> Submit
Schedule .> Allocate : <<include>>
Allocate .> Availability : <<include>>
note bottom of Submit : Doctor GUI is a team integration point
@enduml
```

```mermaid
flowchart LR
    A[Admin Staff] --> U([Manage end users])
    A --> D([Assign doctors to managers])
    A --> F([Manage rooms, wards and beds])
    A --> S([Schedule lab and imaging requests])
    A --> R([Configure consultation rates])
    A --> I([Manage insurance networks])
    DR[Doctor module] --> API([Submit request through service])
    API --> Q[(Request queue)]
    S --> Q
    S --> AL([Allocate matching facility])
    AL --> V([Validate availability and conflicts])
```

## Request workflow

```mermaid
stateDiagram-v2
    [*] --> OPEN
    OPEN --> SCHEDULED : Book matching facility
    SCHEDULED --> OPEN : Cancel allocation
    SCHEDULED --> COMPLETED : Complete allocation then request
    OPEN --> CANCELLED : Cancel request
    COMPLETED --> [*]
    CANCELLED --> [*]
```
