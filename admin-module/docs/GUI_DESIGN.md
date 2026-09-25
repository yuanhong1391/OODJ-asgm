# Hospital administration interface

The revised interface uses navy headings, teal primary actions, pale grey work areas and white content panels. Segoe UI typography, consistent input padding, selected navigation states, status badges and alternating table rows make daily administration easier to scan.

The dashboard uses actual stored records: registered users, active doctors, assets whose status is AVAILABLE, and OPEN requests awaiting scheduling. Available assets is a configuration count, not a claim that those assets are unbooked at the present time. The request queue displays up to five pending items, with an action to view all requests.

## Reference

[Sunway Medical Centre's current website](https://www.sunwaymedical.com/en/) was visually reviewed on 25 September 2026. Its spacious header, clear service navigation, turquoise action accents and prominent service shortcuts informed the direction. This app uses an original staff-workspace layout and APU branding; no hospital photographs, logos, claims or website code were copied.

## NetBeans editing

The existing `.java`/`.form` pairs remain intact. Shared styling is applied outside guarded blocks through `HospitalTheme` and `EditorPanel.applyTheme`. MainFrame applies runtime navigation layout. `SignInPanel` arranges the existing login/setup fields vertically; their `.form` definitions retain the editable base grid. `DashboardPanel` is composed in Java. Runtime theme/layout overrides will not all appear in GUI Builder's base preview.

## Preview evidence

Images in `docs/previews` are offscreen renders of production Swing components with isolated fictional records. They show the dashboard at normal/compact sizes, sign-in, initial setup and the user form. The dashboard preview assembles the same header/navigation theme around the actual dashboard panel. These are not screenshots of NetBeans or a manual desktop click-through.
