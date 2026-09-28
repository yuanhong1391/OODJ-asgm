# Admin integration for manager-refactor

Base: `yuanhong1391/OODJ-asgm`, branch `manager-refactor`, commit `1507346` (feat: update main.java).

This delivery adds Admin code to that project. It does not change any existing teammate source, form, build configuration or sample data file. It is not the old nested `admin-module` project. Copy the contents of this package into the root of the group's **manager-refactor** checkout, so the new files join its existing `src` folder. Do not copy the old `hms` packages or the old Admin `users.txt`.

## 1. Build and run before changing the shared login

1. Open this project root in NetBeans (the directory containing `build.xml` and `nbproject`).
2. Use JDK 26, as selected by the existing group project. Clean and Build the project.
3. Under Source Packages / default package, right-click `AdminLauncher.java` and select **Run File**, or press Shift+F6. F6 still starts the group's original `Main` and `Login`.
4. On first run, create the first administrator. The confirmation displays the generated login ID, normally `ADM001`. Keep your chosen password. Use **the ID**, not the username, to sign in.
5. All paths are relative to the group project root. Keep the NetBeans working directory at that root. Running from another directory creates a separate data folder.

The Admin dashboard and all nine input panels include `.form` files. Open `src/views/admin/AdminDashboard.java` or `src/views/admin/forms/UserForm.java` and choose **Design**. Runtime dashboard cards and tables are built by Swing code; the presence of `.form` files should not be described as proof that all UI work was performed manually by drag and drop. Review the lecturer's expectations yourself.

## 2. One shared Login hook (for the group integrator to apply)

This code is supplied as instructions only. It has **not** been applied to the existing `src/views/Login.java`.

In `btnLoginActionPerformed`, find the existing empty-input validation. **After that validation and before `UserService userService = new UserService();`**, insert:

```java
// Admin accounts are separate; Doctor/Patient/Manager keep the existing login flow.
try {
    if (views.admin.AdminIntegration.tryOpen(inputID, inputPassword)) {
        this.dispose();
        return;
    }
} catch (java.io.IOException ex) {
    javax.swing.JOptionPane.showMessageDialog(this,
            "Cannot read hospital data: " + ex.getMessage(),
            "Login error", javax.swing.JOptionPane.ERROR_MESSAGE);
    return;
}
```

No change to `UserService`, `models.User`, Doctor, Patient, Manager, their dashboards, or `Main` is required for this hook. `Main` already opens `Login`. Because their logout buttons already open `Login`, they will also return to the shared login after this hook is added. Admin logout uses the same `Login` through a callback.

Create the first Admin using `AdminLauncher` before trying Admin from the group Login. Wrong Admin credentials return to the existing generic failure flow. Existing Doctor/Patient/Manager authentication continues unchanged.

**Without this hook:** Admin is fully runnable with `AdminLauncher`, and its users and test requests share the group's data. The original Login will not recognize Admin. Do not claim the four-role login is already connected just by copying the files.

## 3. Shared objects and data formats

`models.AdminStaff extends models.User`. The adapter constructs the group's actual `Doctor`, `Patient` and `Manager` classes and uses their overridden `toTxtRecord()` methods. No duplicate clinical user hierarchy is introduced.

`data/users.txt` keeps the group's existing comma-separated rows without a header:

```text
Doctor: id,username,password,name,phone,email,Doctor,specialization,roomNumber
Patient: id,username,password,name,phone,email,Patient,bloodType,emergencyContact,medicalHistory
Manager: id,username,password,name,phone,email,Manager
```

Admin creates and edits all three clinical/staff roles in that file. Role cannot be changed on an existing account. Existing IDs and role-specific fields are preserved during updates. Blank optional clinical fields use `Not specified`; blank contact fields use `-` so the group's `split(",")` does not lose trailing values. New doctor IDs use `D001`-style IDs to satisfy Manager roster validation. The shared User has no active flag, so this version does not pretend that clinical account deactivation is supported.

The existing group login uses plaintext password comparison. Doctor/Patient/Manager retain that format for compatibility. Admin credentials alone are PBKDF2 hashes in `data/admin_accounts.txt` (seven fields in the same common order, role `Admin`). **Do not move Admin records into `users.txt`**: the unchanged `UserService` skips unsupported roles and later rewrites that file. This separate Admin file prevents accidental account loss without editing that service.

New files are created on first save; none are required to be seeded:

| File under data/ | Content | Consumers |
|---|---|---|
| admin_accounts.txt | Admin ID, username, hash, name, phone, email, role; no header | Admin authentication |
| admin_assignments.txt | doctorId,managerId | Admin doctor-manager assignments |
| admin_assets.txt | id,type,name,parentId,status | Admin asset registry |
| admin_allocations.txt | id,assetId,patientId,doctorId,requestId,start,end,status | Admin room/bed/test allocations |
| admin_rates.txt | id,consultationType,amount,active | Admin consultation rates |
| admin_insurance.txt | id,name,active | Admin insurance networks |

The five Admin management files have a header row. Fields cannot contain commas, tabs or line breaks. All files are text files, with no database or extra runtime library.

## 4. Real Doctor-to-Admin test workflow

The existing `Doctor.requestsTest()` writes `data/lab_tests.txt`:

```text
testId,patientId,doctorId,date,testType,result,status
```

The Admin request queue reads that same file. Blood Test (Full Blood Count) and Urine Analysis map to LAB; Chest X-Ray maps to XRAY; CT Scan (Abdomen), MRI Scan, MRI Scan (Brain / Spine) and Ultrasound map to IMAGING. Unknown test names display UNKNOWN and cannot be scheduled until a matching mapping is added. Existing `Allocated` records and `Scheduled` records without a local Admin allocation remain visible and read-only; Admin cannot infer their facility or times. They are preserved when other requests change.

1. Doctor creates a request. Admin clicks Refresh in Lab / imaging requests.
2. Admin creates the appropriate facility under Hospital assets.
3. Select the request and click Schedule selected request. Save a BOOKED allocation with the correct facility and times.
4. The shared file status changes from `Requested` to `Scheduled`.
5. Under Asset allocations, edit the allocation to COMPLETED. Then update the request to COMPLETED.
6. The shared file becomes `Completed`; the doctor's request date, test name and result are preserved. Admin does not invent or enter clinical test results.
7. Cancelling a booked allocation returns the request to `Requested`; explicitly cancelling the request writes `Cancelled`.

The existing Doctor dashboard loads its request table when opened. Reopen it or use its existing refresh/reload route to see Admin changes; there is no new background refresh code in the doctor's UI.

## 5. Boundaries to communicate honestly

- Assignment records refer to real group doctor/manager IDs. The existing Manager pages do not use these assignments to filter their screens; that would require separate group work.
- Rates and insurance settings persist and can be read with `AdminStore.read("rates")` / `read("insurance")`. Existing teammate billing/report screens have not been changed to consume them.
- Admin allocation overlap checks cover Admin room, bed and test allocations. The existing Patient appointment records only store a date, without an end time, so they are not silently treated as timed Admin allocations or rewritten. This version does not claim a unified appointment calendar or roster enforcement.
- User deletion is blocked by existing assignments, allocations, requests, and references in shared appointments, feedbacks, vitals, prescriptions, rosters, departments and payments. Asset history is preserved.
- This is a local coursework application. Run one demonstration session at a time and avoid simultaneous writes from separate processes. The unchanged group writer does not participate in a shared lock or database transaction. Admin uses temporary-file replacement and attempts rollback on write errors, but this is not a crash-proof multi-file database.
- Existing sample data and original teammate defects are not repaired by this delivery. Malformed shared user/request records cause an explicit error instead of being discarded.
- Old standalone Admin data is deliberately not imported over group data. Re-create only the needed demonstration records in this version, then take new report screenshots.

## 6. Validation and handoff

The original NetBeans Ant build compiled all 39 Java files with JDK 26. The isolated compatibility test exercises the **actual unchanged** group `UserService`, `Doctor`, `Patient` and `Manager`; fixtures never overwrite the project's real data. Run:

```powershell
.\admin-tests\run-tests.ps1 -JdkHome 'C:\path\to\jdk-26'
```

It compiles sources, runs 71 checks, then launches a separate JVM for three persistence checks. Test preview images are offscreen renders, not evidence of a manual NetBeans walkthrough. The actual NetBeans Design editor and the optional shared Login hook still need a short manual group acceptance test.

Before handoff: create a Doctor, Patient and Manager in Admin; log into each through the group's original Login using its ID and password; create a Doctor request and schedule it in Admin; close and reopen the app; verify the records remain. Then apply the optional Login hook and test all four roles plus logout.

For Yellow/Restricted GenAI: this delivery contains AI-assisted code adaptation and testing, not just debugging advice. Understand and review it, check the allowed scope with the lecturer, and record the actual assistance accurately in the AI usage log. The old diagram/report descriptions should be revised to match these classes and files.
