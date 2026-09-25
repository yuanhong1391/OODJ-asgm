# APU Medical Centre — Admin Staff

Java Swing application for NetBeans 30. Data is stored in UTF-8 `.txt` files, with no database or external libraries.

## Open and run

1. In NetBeans choose **File → Open Project** and select this folder.
2. Select a JDK 17 or newer Java platform (the development machine has JDK 26).
3. Run the project. On first launch, create the initial administrator using the setup form. No default password is shipped.
4. Log in with that username and password.

Open `src/hms/ui/MainFrame.java` or any of the nine panels in `src/hms/ui/forms` in **Design** to edit the NetBeans GUI Builder forms. Each data-entry field has a matching component in its `.form` file. Navigation/content switching and searchable record tables are composed in Java. Build the project once before opening a panel so NetBeans can load its shared `EditorPanel` superclass. Do not claim to have manually dragged components you did not create yourself.

Without NetBeans, on Windows:

```powershell
.\build.ps1
.\run.ps1
.\build.ps1 -Test
```

The default data folder is `data` under the working directory. Override with `-Dhms.data=absolute-path` before `-jar`. Keep the same working directory/data location when running from NetBeans and the command line.

For a separate fictional demo, run `./run.ps1 -Demo`. It creates `demo-data` once and signs in with **admin.demo / Practice123!**. It does not overwrite the normal `data` folder. All demo accounts use the same sample password and contain no real patient data.

The local JDK is installed at the root of `D:\` and reports `java.home` as `D:`. The scripts and Ant targets normalise that path for this machine. If NetBeans asks for a Java platform, choose `D:\` as the JDK folder; do not select the `bin` directory. On teammates' machines, choose their own installed JDK folder.

## Scope

The interface includes a hospital-style navy/teal theme, a live operations dashboard, selected navigation states, readable tables with status badges, and styled sign-in/data-entry screens. See `docs/GUI_DESIGN.md` for the visual reference and preview images.

Implemented: initial setup, login/logout, all-role user CRUD, doctor–manager assignments, facility/ward/bed management, timed allocations, lab/imaging request scheduling and status management, consultation rates and insurance networks. Other roles have a clear integration notice after login: their complete dashboards are team integration work.

Read `docs/INTEGRATION.md` before teammates start implementing their modules. `docs/ADMIN_REPORT.md` contains design explanations and a report outline; `docs/TESTING.md` contains verification instructions. UML and ERD source is in `docs/DIAGRAMS.md`.

## Coursework use

This is AI-assisted work for you to inspect, understand and adapt. The supplied assignment brief requires disclosure, significant adaptation and your own reasoning. Keep your actual prompts and changes in `docs/AI_LOG.md`. Complete personal details, actual screenshots and reflection yourself; do not submit placeholder report material as completed evidence.
