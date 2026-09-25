# Shared application integration contract

This module is the starting project for one group application. It provides the admin screens and shared login/data services. The other three full dashboards are not implemented here.

## Team boundaries

| Owner | Work supplied here | Work to integrate |
|---|---|---|
| Admin staff | Users, assignments, assets, allocations, request processing, rates, insurance | Group-wide presentation and report consolidation |
| Medical managers | Manager accounts and doctor assignments | Profile, departments, shift rosters, reports |
| Doctors | Doctor accounts and authenticated `submitRequest` method | Profile, vitals, notes, prescriptions, request-entry screen |
| Patients | Patient accounts referenced by requests and allocations | Profile, booking, history, prescriptions, feedback |

Use one `TextStore`, one `HospitalService`, and one logged-in session in the application. Do not give every panel its own store: the data folder has an exclusive lock. Teammates can reuse `service.currentUser()` to obtain the session identity. Add dashboard routing at the non-admin branch in `MainFrame.showLogin`. It currently shows an integration notice and returns to login.

## Files and fields

Every file is UTF-8, with a mandatory tab-separated header. Each subsequent line is one record. Field order must match the header exactly. A backslash is stored as `\\`, a tab as `\t`, a newline as `\n`, and a carriage return as `\r`. Use `TextStore` to read/write this format rather than implementing another parser.

| File | Header fields, in order |
|---|---|
| users.txt | id, role, name, username, passwordHash, email, phone, active |
| assignments.txt | doctorId, managerId |
| assets.txt | id, type, name, parentId, status |
| allocations.txt | id, assetId, patientId, doctorId, requestId, start, end, status |
| requests.txt | id, doctorId, patientId, type, details, status |
| rates.txt | id, consultationType, amount, active |
| insurance.txt | id, name, active |

IDs are generated with an entity prefix and UUID. `doctorId` is the primary key in assignments, allowing at most one manager per doctor. Never replace an ID with a name or generate IDs from row counts. Foreign keys reference the existing IDs in users/assets/requests. Empty optional foreign keys are empty strings, not the literal string `null`.

Roles are `ADMIN`, `MANAGER`, `DOCTOR`, `PATIENT`. Booleans are lowercase `true`/`false`. Times are local ISO date-times, for example `2026-09-27T09:00`. Agree on one hospital timezone; this version has no timezone conversion. Monetary values are MYR decimal strings with two fractional digits.

Passwords use `iterations:base64salt:base64hash` with PBKDF2-HMAC-SHA256. Login belongs to the service; other modules must never read or display password hashes. Passwords cannot be retrieved; admins can replace them.

## Service entry points

```java
// Shared startup only:
TextStore store = new TextStore(Path.of("data"));
HospitalService service = new HospitalService(store);
User session = service.login(username, password);

// Doctor module, after doctor login:
service.submitRequest(patientId, "LAB", "Full blood count requested");

// Admin module:
List<Map<String, String>> requests = service.list("requests");
service.save("allocations", allocationFields);
service.logout();
```

`list`, `save` and `delete` are admin-only. Add explicit role-scoped methods for teammates, such as a doctor's own request list or a patient's own appointments. Do not remove the admin guard to make a new dashboard work. For the doctor screen, add an authorised patient-selection method when integrating; `submitRequest` currently accepts an existing patient ID and verifies the user is an active patient. Clinical access/appointment rules must be agreed with the team.

## Request and allocation lifecycle

Requests store `OPEN`, `COMPLETED` or `CANCELLED`. `SCHEDULED` is a display status derived from an open request with a non-cancelled allocation. Creating an allocation updates only allocations.txt, avoiding a partially saved request-plus-allocation transaction.

1. A doctor calls `submitRequest`, or admin records an incoming doctor request using the UI.
2. Admin selects the open request and chooses **Schedule selected request**.
3. The service verifies matching doctor, patient and facility type, availability, times and conflicts.
4. Admin marks its allocation COMPLETED, then marks the request COMPLETED. Completion records administrative status, not clinical results.
5. To reschedule, edit a BOOKED allocation or cancel it and create a new one. To cancel a request, cancel its BOOKED allocation first.

Closed requests and finished/cancelled allocations are retained as history. Request clinical details cannot be edited by admin after creation. A replacement request is used for corrections. UI intake on behalf of a doctor is a convenience for this standalone module demonstration, not proof that a doctor personally signed the request.

Consultation rooms require doctors and patients. Beds require a ward and patient; a doctor is optional. Test rooms require a matching request. All allocations have a finite start/end. Half-open intervals `[start,end)` permit adjacent bookings. The same asset cannot have overlapping BOOKED allocations. Patients cannot have overlapping non-bed allocations or two simultaneous beds. An inpatient stay may overlap a test or consultation. Doctors cannot have two simultaneous consultation-room allocations; ordering a lab test does not mean the doctor occupies that lab slot.

## Before adding appointments, billing or patient insurance

The original planning ERD included appointments and patient insurance as future integration points. They are not stored by this module. Agree on their schema with the owners first.

- Add appointment/roster checks to allocation validation so booking and allocation screens share availability rules.
- Expand user deletion and role-change checks to cover new appointment, prescription, feedback and clinical-record references.
- Rates and insurance can currently be deleted because this module stores no billing/policy references. Once those files exist, block referenced deletions and deactivate records instead.
- For billing, copy the agreed price to the invoice/appointment when charged; do not recalculate historical bills from a newly edited rate.
- Keep a backup while all writers are closed before changing a header. There is no automatic schema migration.

This implementation is for one desktop process, not concurrent installations or a network server. File replacement is atomic where the filesystem supports it; a fallback replacement is used otherwise. There is no general multi-file transaction manager.
