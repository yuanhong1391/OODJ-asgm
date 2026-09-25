# Verification and demonstration guide

## Automated verification

Run `./build.ps1 -Test` from the project directory. This compiles Java 17-compatible classes, builds `dist/HMSAdmin.jar`, runs `test/hms/ServiceTest.java` and renders the nine editor panels into `build/form-previews`. Tests use separate temporary directories rather than application data. No JUnit download is required.

The suite covers initial setup, password checks, admin permissions, duplicate users, self-account protection, doctor–manager constraints, room/doctor/patient conflicts, adjacent intervals, ward/bed availability, request scheduling/completion, currency precision, insurance uniqueness, text escaping, persistence after restart, file ownership and malformed file recovery. It also checks editor field construction and label fit. Form label checks are UI smoke checks, not separate business scenarios.

The installed NetBeans Ant distribution has also been used to run the `test` and `jar` targets. A successful Ant build proves compilation and test execution; it is not evidence that NetBeans Design view or the interactive UI has been manually verified.

## Manual acceptance sequence

1. Open the project folder in NetBeans 30. Select an installed JDK 17 or newer. Build it.
2. Open `hms.ui.forms.UserForm`, `AllocationForm` and `MainFrame` in Design view. Verify components appear in the inspector, can be selected and can be moved using the selected layout. Save a small intentional text/layout edit and rebuild. Keep `.java` and `.form` together.
3. Run the project. Create an initial admin, then log in. Verify an incorrect password is rejected.
4. Add a manager, two doctors and two patients. Edit a patient's name and verify it persists after refresh. Attempt a duplicate username and verify an error. Delete an unreferenced temporary user.
5. Assign a doctor to a manager, then edit the assignment. Verify a patient is not offered as a doctor.
6. Create a ward and bed, two consultation rooms, a lab, an X-ray room and an imaging room.
7. Book a consultation. Try the same room/time with another patient; expect a conflict. Try the same doctor/time in another room; expect a conflict. Try an adjacent time; expect success.
8. Allocate a bed to a patient. Verify the whole ward cannot be allocated and cannot enter maintenance while its bed is booked.
9. Record an incoming LAB request on behalf of a doctor. Select it and schedule a LAB allocation. Verify the queue shows SCHEDULED. Try an X-ray facility for a LAB request; expect rejection.
10. Edit the allocation to COMPLETED without changing its other fields. Update the request status to COMPLETED. Verify closed request details cannot be changed.
11. Try cancellation and rescheduling with another request. Cancel the booked allocation before cancelling the request, or create a new allocation after cancellation.
12. Add/edit a consultation rate and an insurance network. Verify negative rates and duplicate names are rejected.
13. Search by a user's name and by ID. Sort a table, select a record and edit it; verify the selected record is the one updated.
14. Log out and back in, then close/reopen the app. Verify records persist. Try a second copy pointed at the same data folder; expect the ownership message.

## Fictional demonstration data

`./run.ps1 -Demo` generates an isolated fictional dataset and launches the application against it. The sample administrator is `admin.demo`, password `Practice123!`. Demo users and facilities are for learning only. To use that dataset from NetBeans, set Run VM Options to `-Dhms.data=demo-data`; remove the option to return to the normal `data` folder.

Do not edit a data file while the application is running. Keep a copy of the complete closed data folder before experimenting. If a file is rejected, correct the malformed record/header or restore a valid copy; the app deliberately does not erase it.

## Remaining manual verification

Interactive NetBeans Design-view round trips and full desktop click-through are still to be performed on the user's machine. Offscreen panel renders and service tests do not test native window sizing, keyboard focus, display scaling, or NetBeans form regeneration.
