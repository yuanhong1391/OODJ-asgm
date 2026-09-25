# Hospital Management System administrative staff contribution

This working report describes the supplied admin implementation. Add your cover details, your verified screenshots, your own design reflection, and your modifications before integrating it into the group report. It is not a claim that you personally wrote or manually tested all of the supplied code.

## Purpose and scope

The administrative staff module supports APU Medical Centre in maintaining users, assigning doctors to medical managers, allocating physical facilities, processing lab/imaging requests, and configuring consultation rates and accepted insurance networks. It is a Java Swing desktop application intended to be opened in NetBeans 30. Backend records are stored in text files as required by the assignment.

The implementation separates interface code, business rules and file storage. Admin screens call `HospitalService`; the service validates role permissions and record relationships before calling `TextStore`. This avoids duplicating validation across dialog boxes and future teammate screens.

## Requirements coverage

| Brief requirement | Implementation evidence |
|---|---|
| Create, read, update and delete end users | End users screen; `HospitalService.saveUser` and guarded `delete` |
| Assign doctors to medical managers | Doctor assignments screen; role checks and one assignment per doctor |
| Manage and allocate hospital assets | Assets and allocations screens; ward/bed relationship and interval conflict checks |
| Configure consultation rates | Rates screen; nonnegative decimal validation and MYR display |
| Configure accepted insurance networks | Insurance screen; unique name and accepted flag |
| Process doctor lab/imaging requests | Incoming request screen, doctor service API, scheduling and completion flow |
| Java Swing/AWT GUI | MainFrame and nine GUI Builder-compatible form panels |
| Text-file persistence | Seven `.txt` files with explicit headers and escaping |
| Continuous operation and validation | Modal feedback returns control to the running application; logout returns to login |

The original brief permits any Java IDE. NetBeans and editable `.form` files are the additional lecturer/user requirements for this project. GUI Builder compatibility does not establish that the student manually used drag and drop; demonstrate your own edits where required.

## Design decisions

The use case, class and ER diagrams are in `DIAGRAMS.md`. User identities use stable generated IDs so that editing a person's name does not invalidate references. Roles are validated in the service: a patient ID cannot be used as a doctor assignment. End-user deletion is supported for unreferenced records. Referenced users are retained to preserve history; they can be deactivated after active work and assignments are resolved.

Hospital assets include consultation rooms, wards, beds, laboratories, X-ray rooms and imaging rooms. Wards group beds rather than acting as a single bookable slot. Booking intervals include their start and exclude their end, allowing 09:00–10:00 and 10:00–11:00 to use the same asset. A patient may receive a consultation while staying in a bed, but cannot occupy two beds at once. These are design assumptions selected for the project, not clinical policy claims.

Lab and imaging requests retain the requesting doctor, patient, test type and description. Administrators allocate a matching facility. Request status SCHEDULED is derived from allocation data, reducing the number of files that need to change during scheduling. Clinical findings and interpretation are outside this module.

## Object-oriented concepts

### Encapsulation

`User` stores its ID, name, username and role in private final fields and exposes getters. `HospitalService` owns the session and provides guarded operations; screens cannot change its current user directly. `TextStore` owns the file path, schema definitions and exclusive lock, keeping persistence mechanics out of the GUI.

### Abstraction

`User` is abstract and declares `canAdminister` and `welcomeMessage`. `EditorPanel` defines a common contract for retrieving, setting and locking form fields without requiring MainFrame to know individual component names. This lets screen handling work with the common abstraction.

### Inheritance

`AdminStaff` and `ClinicalUser` inherit shared identity state and getters from `User`. The nine form classes inherit field-handling behaviour from `EditorPanel`, which itself extends Swing's `JPanel`. The implementation currently groups manager, doctor and patient identities in ClinicalUser; separate behaviour can be introduced during group integration when justified.

### Polymorphism

After login, MainFrame holds a `User` reference and calls `canAdminister` and `welcomeMessage`. The result depends on the concrete subclass. Similarly, MainFrame calls `values` on an EditorPanel whose overridden `fields` method supplies the components for the selected form. This is runtime method dispatch, rather than simply having several classes with different names.

### Composition and separation of responsibilities

MainFrame has a HospitalService, and HospitalService has a TextStore. Neither UI nor service inherits from the storage class. This composition lets service tests exercise workflow rules without clicking the interface. Non-user domain records are currently maps; creating typed asset/request/allocation classes is a possible future design improvement, especially as the group application grows.

## Storage and validation

Files are UTF-8 tab-separated text with escaped control characters. Temporary-file replacement reduces the chance of truncating a valid file during a save. A process-level folder lock prevents two running copies from overwriting the same data. Files with malformed headers or rows are rejected with an error instead of being silently reset.

Validation covers required fields, account names, passwords, optional contact details, role constraints, duplicate configuration values, nonnegative rates, date parsing, time ordering, facility availability, overlapping allocations and referenced-record deletion. Passwords are salted hashes rather than recoverable plaintext. The service checks authorisation in addition to hiding admin screens from other roles.

## Additional features

Searchable and sortable tables help locate records. Related user and asset IDs are displayed as readable names with full IDs in tooltips. First-run setup creates the initial administrator without a shipped live password. An optional fictional demo dataset lives separately from normal records. The service also prevents self-deletion and self-deactivation of the current administrator.

## Verification and screenshots

See `TESTING.md` for automated checks and the manual demonstration sequence. Automated checks exercised persistence, validation, permissions and request/booking state changes. All nine editor panels were instantiated and rendered offscreen. These checks do not replace opening the project, using Design view and running the workflows in the actual NetBeans application.

Capture your own final screenshots after performing the manual sequence:

1. NetBeans project and a form open in Design view.
2. Login and admin dashboard.
3. User creation/editing and a rejected duplicate username.
4. Doctor–manager assignment.
5. Ward/bed hierarchy and an allocation conflict message.
6. Incoming request, scheduled allocation and completed request.
7. Rates and accepted insurance networks.
8. Each of the seven text files opened with fictional demonstration records.

Explain what operation each screenshot proves. Do not label generated form previews as screenshots of a completed manual workflow.

## Limitations and conclusion

This is the admin contribution, not the full four-role HMS. Teammates must implement and integrate their dashboards and extend reference checks to their new data files. Clinical outcomes, billing calculations, insurance claim handling, recurring appointments and shift availability are not implemented here. Rates and insurance are configuration only. The desktop file store is not designed for multi-user network deployment, and there is no automatic schema migration or backup service.

The admin workflow is implemented around validated service methods and persistent text records. Before submission, the group needs to verify the integrated application, adapt the supplied design and code, and describe each member's actual contribution and understanding.

## References and contribution appendix

Primary requirements: `2607-OODJ-Assignment-QS (1) (1).docx`, supplied assignment brief. Implementation references: the accompanying source code, `DIAGRAMS.md`, `INTEGRATION.md` and `TESTING.md`. Add course materials and any external sources you actually consulted using your required citation format.

Complete your student name/ID, intake, module, dates, personal reflection, evidence screenshots and AI prompt log. Use `AI_LOG.md` as an honest starting record; retain this conversation as the full prompt/output source.
