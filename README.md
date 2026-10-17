# DueWise — Student Assignment Planner

MO-IT114 Mobile Development · Milestone 2: Functional Mobile App UI Implementation
Group: Devie Dela Cruz, Ibrahim Lagyal, GV Roma, Jeah May Pareja

DueWise helps a college student break an assignment into smaller tasks, give each task a target date, and track what is done. It is built with Kotlin and Jetpack Compose and follows our approved Week 4 prototype plus the 12 design decisions our group agreed on before Week 6.

---

## 1. Open and run the app

1. Open **Android Studio** → **File → Open** → select this `DueWise` folder.
2. Wait for **Gradle sync** to finish (bottom status bar). The first sync downloads libraries and can take a few minutes.
   - If Android Studio offers to **upgrade the Android Gradle Plugin**, you can accept. The app works either way.
3. In the device dropdown at the top, pick an emulator (e.g., **Medium Phone API 36**).
4. Press **Run ▶**. The app opens on the **Today** screen.

Requirements: minSdk 26 (Android 8.0) or newer, compileSdk 35.

---

## 2. Project structure

```
app/src/main/java/com/duewise/app/
├── MainActivity.kt              Entry point; creates the ViewModel and starts the app
├── data/
│   ├── Models.kt                Assignment, Task, TaskDraft, Reminder
│   └── SampleData.kt            Starting data copied from the prototype
├── navigation/
│   ├── Routes.kt                ALL route strings and route builders (one place)
│   ├── DueWiseNavHost.kt        The one NavController + the one NavHost + every navigation call
│   └── BottomNavBar.kt          Today / Upcoming / Completed / Settings tabs
├── ui/
│   ├── DueWiseViewModel.kt      Shared state (assignments, tasks) and the events that change it
│   ├── components/              Reusable composables
│   │   ├── Cards.kt             DueWiseCard, TaskCard, CompletionToggle
│   │   ├── Buttons.kt           PrimaryButton, OutlinedActionButton
│   │   ├── Inputs.kt            LabeledTextField, DateField (+ calendar dialog)
│   │   ├── Dialogs.kt           ConfirmDialog
│   │   └── Layout.kt            ScrollScreen, ScreenHeader, SectionLabel, EmptyState, LabeledValue, AppLogo
│   ├── screens/                 One composable per screen (stateless about app data)
│   │   ├── TodayScreen.kt
│   │   ├── TaskListScreen.kt    UpcomingScreen + CompletedScreen
│   │   ├── TaskScreens.kt       TaskDetailsScreen, EditTaskScreen, DeleteTaskScreen
│   │   ├── AssignmentScreens.kt AssignmentDetailsScreen, DeleteAssignmentScreen
│   │   ├── PlanScreens.kt       NewAssignmentScreen (step 1), AddTasksScreen (step 2)
│   │   ├── NotificationsScreen.kt
│   │   ├── SettingsScreen.kt
│   │   └── LoginScreen.kt
│   └── theme/                   Colors, type scale and theme from the prototype
└── util/DateFormats.kt          The one date format used everywhere ("September 20, 2026")
```

**How the layers connect**

- **Screens** only draw the UI and report user events through lambdas (`onOpenTask`, `onSave`…). They never call the NavController or change shared data.
- **DueWiseNavHost.kt** receives those events, updates the ViewModel, and makes the navigation call.
- **DueWiseViewModel** holds the assignments and tasks as Compose state lists, so every screen showing them updates automatically (recomposition).
- **Local UI state** (form drafts, dialog visibility, password visibility) lives inside each screen with `rememberSaveable`, so it survives rotation.

---

## 3. Screens and navigation

| Screen | Route | Opened from |
| --- | --- | --- |
| Today (start) | `today` | App launch, bottom bar, Sign In |
| Upcoming | `upcoming` | Bottom bar, "See all in Upcoming", Save plan |
| Completed | `completed` | Bottom bar |
| Settings | `settings` | Bottom bar |
| Notifications | `notifications` | Bell on Today |
| Task details | `task_details/{taskId}` | Any task card, notification, assignment's task list |
| Edit task | `edit_task/{taskId}` | Task details → Edit task |
| Delete task | `delete_task/{taskId}` | Task details → Delete task |
| Assignment details | `assignment_details/{assignmentId}` | Assignment name on Task details |
| Delete assignment | `delete_assignment/{assignmentId}` | Assignment details → Delete assignment |
| New assignment (step 1) | `new_assignment` | Today → + Add assignment |
| Add smaller tasks (step 2) | `add_tasks/{title}/{subject}/{deadline}` | Continue to Tasks |
| Login | `login` | Settings → Log Out |

Main task flow: **Today → + Add assignment → New assignment → Continue to Tasks → Add smaller tasks → + Add task → Save plan & view tasks → Upcoming** (new tasks appear in date order). Back from Upcoming returns to Today.

**Navigation safety**

- One `NavController` (`rememberNavController()` in `DueWiseApp`) and one `NavHost`.
- Every navigation call is inside a user event handler; nothing navigates during recomposition.
- `ifCurrent { … }` runs a navigation call only when the screen is fully shown (`RESUMED`). Taps during a transition are ignored, so double taps cannot open a screen twice or pop twice.
- Bottom-bar tabs use `launchSingleTop`, `saveState` and `restoreState`, so tabs never stack.
- Task details ↔ Assignment details goes **back** instead of stacking a loop when you came from that same screen.
- Arguments are primitives only: `Int` ids, `String` title and subject (URL-encoded), and the deadline as a `Long` epoch day.

### What changes state vs. what is movement only

| Action | Type | Result |
| --- | --- | --- |
| Tap ○ / ✓ on a task card, or Mark task completed | State change | Task moves between Upcoming and Completed on every screen |
| Save changes (Edit task) | State change | New name/date shown everywhere |
| Delete task / Delete assignment (confirm) | State change | Removed from all lists, return to Today |
| Save plan & view tasks | State change | New assignment and tasks appear in Upcoming |
| Back arrow, system Back, Cancel, bottom-bar tabs | Movement only | Nothing is saved or removed |
| Typing in a form, picking a date | Local draft | Kept only until Save; discarded on Cancel/Back |

---

## 4. Agreed design decisions (applied)

| # | Decision | Where in code |
| --- | --- | --- |
| A1 | Tap the circle = complete/undo; tap the card = open Task details | `TaskCard`, `CompletionToggle` |
| A2 | No ⋮ menu on cards | `TaskCard` |
| A3 | Today shows the next 3 pending tasks + completed + "See all" | `TodayScreen`, `nextPendingTasks()` |
| A4 | Every list sorted by target date | `DueWiseViewModel` |
| A5 | One date format: "September 20, 2026" | `util/DateFormats.kt` |
| A6 | "Task" everywhere, never "step" | All screens |
| A7 | No priority | `NewAssignmentScreen` |
| A8 | No task description | `TaskDetailsScreen` |
| A9 | App starts at Today; login reached only through Log Out | `DueWiseNavHost` (`startDestination`) |
| A10 | Delete assignment kept, with confirmation | `DeleteAssignmentScreen` |
| A11 | No back arrows on tab screens | Today, Upcoming, Completed, Settings |
| A12 | Red text only for delete/log-out actions | `OutlinedActionButton` usages |

Notes:
- Login checks only that both fields are filled in. Real authentication and the Google/Apple/Phone options are deferred, as stated in our MS1 scope.
- Data is kept in memory while the app runs (no database), matching the MS1 scope.
- Research Paper sample values (subject **English**, deadline **September 29, 2026**) are placeholders. Change them in `data/SampleData.kt` if the group picked different ones.

---

## 5. Milestone 2 test script

Run on the emulator. Tick each item; if something fails, fix it and run the whole phase again.

### Phase 1 — Stability testing

- [ ] **Forward navigation:** Today → tap "Create wireframes" → Task details opens for that task.
- [ ] **Back stack:** Today → task → assignment name → a task in the list → press Back repeatedly; each press returns one screen, ending on Today, then the app closes.
- [ ] **Rapid tapping:** double-tap / triple-tap a task card, "+ Add assignment", "Edit task" and the bell. Only one screen opens each time (one Back returns to the previous screen).
- [ ] **Rapid back:** triple-tap the back arrow on Edit task. Only one screen is popped.
- [ ] **User-triggered only:** open the app and rotate the emulator; no screen opens by itself.
- [ ] **Arguments:** open Find sources, Create wireframes and Write results from Today; each shows its own name, assignment and dates. Plan a new assignment named "Math Quiz / Ch. 3" (with a slash) and confirm the title appears correctly on step 2.
- [ ] **State kept:** type a title on New assignment → Continue → Back; the title is still there. Rotate on Edit task; the typed draft stays.
- [ ] **State consistent:** complete "Find sources" on Upcoming → it shows ✓ on Today and Completed and "Mark as not done" on its Task details.
- [ ] **Rapid interaction:** tap the ○ on one card quickly 6 times; it ends in a sensible state, no crash.

### Phase 2 — Structural refinement

- [ ] No unused composables or parameters (Android Studio: **Code → Inspect Code**, then check "Unused declaration").
- [ ] No `Log.d`, `println` or commented-out experiments (**Edit → Find → Find in Files**: `Log.`, `println`, `//TODO`).
- [ ] Every route string is in `Routes.kt` only (search for `"task_details` — it should appear only there).
- [ ] All navigation calls are in `DueWiseNavHost.kt`.

### Phase 3 — Visual alignment (compare with the Figma screens)

- [ ] Same screen order and layout hierarchy as the prototype.
- [ ] Same side padding (20dp) and gaps between cards (12dp) on every screen.
- [ ] Titles, card titles and labels use the same sizes everywhere.
- [ ] Buttons are full width and the same height; delete actions are red.
- [ ] Navigation sequence matches the agreed flow in Section 3.

### Phase 4 — Final walkthrough

- [ ] Start at Today → + Add assignment → fill step 1 → Continue → add 2 tasks → Save plan & view tasks → see them on Upcoming.
- [ ] Open one of the new tasks → Edit task → change the date → Save → check the new order on Upcoming.
- [ ] Mark it completed → check Completed → tap ✓ to undo.
- [ ] Delete the task (confirm), then delete its assignment from Assignment details (confirm).
- [ ] Return step by step with Back until the app closes.
- [ ] Repeat the whole walkthrough quickly a second time; behavior is the same.

---

## 6. Push to a group-owned GitHub repository

1. On GitHub, create a free **Organization** for the group (profile picture → **Your organizations → New organization**) and invite all four members.
2. In the organization, click **New repository** → name it `DueWise` → **Private** → do **not** add a README, .gitignore or license → **Create repository**.
3. In Android Studio, open **Terminal** (bottom bar) in the project folder and run, replacing the URL with your repository's URL:

```bash
git init
git add .
git commit -m "DueWise Milestone 2: functional Compose UI"
git branch -M main
git remote add origin https://github.com/YOUR-ORG/DueWise.git
git push -u origin main
```

4. On GitHub, open the repository → **Settings → Collaborators and teams → Add people** → add your mentor.
5. Open the repository link in a private browser window while signed in as someone else (or ask the mentor) to confirm it is not empty and all folders are there.
