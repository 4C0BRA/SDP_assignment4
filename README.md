# Assignment 4 — Facade Pattern

**Student:** Ansar Moltabayev · **Group:** SE-2530 · **Course:** ShP-2216 Software Design Patterns
**Topic:** Option C — Course Enrollment Facade

`EnrollmentFacade` gives the client two simple methods, `enroll(student, courseCode)` and
`drop(student, courseCode)`. Behind them it coordinates six subsystem classes in the right order
and undoes the steps that already ran when a later step fails.

## Structure

```
src/enrollment/
├── Main.java                  client — uses only EnrollmentFacade
├── AdvancedClientDemo.java    advanced client — uses SeatRegistry and ScheduleService directly
├── config/DemoUniversity.java builds the subsystem with sample courses and returns the facade
├── model/Student.java         student id, email and completed courses
├── facade/
│   ├── EnrollmentFacade.java  the facade: enroll / drop + rollback
│   └── EnrollmentResult.java  simple result returned to the client
└── subsystem/
    ├── CourseCatalog.java         finds a course by code
    ├── PrerequisiteChecker.java   checks completed prerequisites
    ├── SeatRegistry.java          reserves / releases seats, knows capacity
    ├── ScheduleService.java       student timetable, detects time clashes
    ├── TuitionBilling.java        invoices credits × 25 000 KZT, refunds, blocked accounts
    ├── NotificationService.java   sends messages to the student
    ├── Course.java, EventLog.java
    └── EnrollmentException.java + 6 specific exceptions
docs/uml/facade.puml, facade.png   UML class diagram
docs/demo-output.txt               captured output of both demos
```

## Order of steps in `enroll`

| Step | Subsystem call | Undone on a later failure by |
|---|---|---|
| 1 | `CourseCatalog.find` | — |
| 2 | `PrerequisiteChecker.check` | — |
| 3 | `SeatRegistry.reserve` | `release` |
| 4 | `ScheduleService.add` | `remove` |
| 5 | `TuitionBilling.charge` | — (last step that can fail) |
| 6 | `NotificationService.send` | — |

Any `EnrollmentException` is caught in the facade: finished steps are undone in reverse order,
the student is notified, and the client gets `EnrollmentResult` with `successful = false`.

## Build and run (JDK 17)

```
javac --release 17 -d out $(find src -name "*.java")
java -cp out enrollment.Main
java -cp out enrollment.AdvancedClientDemo
```

Or open the folder in IntelliJ IDEA and use the `Main` / `AdvancedClientDemo` run configurations.

## Demo scenarios (`enrollment.Main`)

| # | Scenario | Result |
|---|---|---|
| 1 | Aruzhan → SE-301 (has SE-201) | SUCCESS, TUE 10:00, 125000 KZT |
| 2 | Dias → SE-301 without SE-201 | FAILED, nothing reserved |
| 3 | Aruzhan → DB-210 (1 seat) | SUCCESS, last seat |
| 4 | Dias → DB-210 | FAILED, course full |
| 5 | Aruzhan → MA-220 (same slot as SE-301) | FAILED, seat released again |
| 6 | Timur → SE-301 (blocked account) | FAILED, schedule entry and seat rolled back |
| 7 | Aruzhan → SE-301 again | FAILED, already enrolled |
| 8 | Aruzhan drops SE-301 | SUCCESS, refund 125000 KZT |
| 9 | Dias drops SE-301 | FAILED, not enrolled |
| 10 | Dias → CS-999 | FAILED, no such course |

## What the facade hides and what stays accessible

The client does not know the six subsystem classes, their order, the `Course` objects, seat
rosters, invoices or the rollback rules. The subsystem classes never reference the facade and stay
public, so an advanced client — `AdvancedClientDemo`, a registrar tool — can check free seats and
edit timetables directly.
