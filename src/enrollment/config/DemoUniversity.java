package enrollment.config;

import enrollment.facade.EnrollmentFacade;
import enrollment.subsystem.Course;
import enrollment.subsystem.CourseCatalog;
import enrollment.subsystem.EventLog;
import enrollment.subsystem.NotificationService;
import enrollment.subsystem.PrerequisiteChecker;
import enrollment.subsystem.ScheduleService;
import enrollment.subsystem.SeatRegistry;
import enrollment.subsystem.TuitionBilling;
import java.util.Set;

public final class DemoUniversity {
    private DemoUniversity() {
    }

    public static EnrollmentFacade createEnrollment() {
        EventLog log = new EventLog(System.out);
        return new EnrollmentFacade(
                catalogWithCourses(),
                new PrerequisiteChecker(log),
                new SeatRegistry(log),
                new ScheduleService(log),
                billingWithBlockedAccounts(log),
                new NotificationService(log));
    }

    public static CourseCatalog catalogWithCourses() {
        CourseCatalog catalog = new CourseCatalog();
        catalog.add(new Course("SE-201", "Object-Oriented Programming", 5, "MON 09:00", 30, Set.of()));
        catalog.add(new Course("SE-301", "Software Design Patterns", 5, "TUE 10:00", 30, Set.of("SE-201")));
        catalog.add(new Course("DB-210", "Databases", 5, "WED 14:00", 1, Set.of()));
        catalog.add(new Course("MA-220", "Discrete Mathematics", 4, "TUE 10:00", 25, Set.of()));
        return catalog;
    }

    private static TuitionBilling billingWithBlockedAccounts(EventLog log) {
        TuitionBilling billing = new TuitionBilling(log);
        billing.blockAccount("S-TIMUR");
        return billing;
    }
}
