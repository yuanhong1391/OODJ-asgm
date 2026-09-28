# Admin delivery validation

Date: 2026-09-28. Group base: manager-refactor / 1507346.

## Verified

- Existing NetBeans Ant build: BUILD SUCCESSFUL, JDK 26.
- 71 isolated compatibility checks passed using the unchanged group UserService, Doctor, Patient and Manager classes.
- Three more checks passed in a separate JVM: administrator authentication, shared user persistence, allocation/request persistence.
- Read-only inspection of the actual branch's sample files: six user records and six test request records parsed successfully.
- Ten Admin `.form` files parsed as valid XML.
- Offscreen Admin dashboard, login and user editor previews rendered; dashboard and user editor images visually inspected.
- The optional shared Login snippet compiled in a temporary copy of Login.java. The actual original Login.java remains unchanged.
- Git diff against 1507346 showed no modification to any tracked group source, form, data or build file. Delivery consists only of new files.

## Important cases covered

Admin-created Doctor, Patient and Manager credentials match the group's existing Login comparisons. Profile edits preserve specialization, room number, blood type, emergency contact and medical history. Original UserService saves do not erase the separately stored Admin account.

The actual Doctor.requestsTest method produces requests that Admin reads and schedules. Scheduled, Completed and Cancelled states are written to the shared lab_tests.txt file. Legacy Allocated records and external schedules remain unchanged. Existing expanded MRI names are recognized. Test results are preserved.

Validation covers duplicate usernames, bad email/phone, CSV delimiters, invalid passwords, user history deletion guards, ward/bed relationships, asset and patient allocation conflicts, invalid request transitions, non-negative two-decimal rates, duplicate insurance networks, logout and malformed-file protection.

## Not claimed as verified

No manual NetBeans Design-editor walkthrough or interactive four-role acceptance test has been performed. No live group data was modified for tests. The shared Login hook has not been applied, merged or pushed. Existing Manager screens do not yet filter by Admin assignments, and existing billing/report code does not consume Admin rates or insurance. Patient appointments remain separate from timed Admin allocations.

Reproduce the automated checks using admin-tests/run-tests.ps1. Complete the manual acceptance steps in ADMIN-INTEGRATION.md before group submission.
