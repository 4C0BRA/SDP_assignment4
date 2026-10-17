package enrollment;

import enrollment.config.DemoUniversity;
import enrollment.subsystem.Course;
import enrollment.subsystem.CourseCatalog;
import enrollment.subsystem.EventLog;
import enrollment.subsystem.ScheduleService;
import enrollment.subsystem.SeatRegistry;

public class AdvancedClientDemo {
    public static void main(String[] args) {
        System.out.println("=== Advanced client: the registrar office manages seats and timetables directly, no facade");
        EventLog log = new EventLog(System.out);
        CourseCatalog catalog = DemoUniversity.catalogWithCourses();
        SeatRegistry seats = new SeatRegistry(log);
        ScheduleService schedule = new ScheduleService(log);

        Course databases = catalog.find("DB-210");
        System.out.println("  free seats in DB-210: " + seats.freeSeats(databases));

        seats.reserve(databases, "S-GUEST");
        schedule.add("S-GUEST", databases);
        System.out.println("  free seats in DB-210: " + seats.freeSeats(databases));
        System.out.println("  timetable of S-GUEST: " + schedule.coursesOf("S-GUEST"));
    }
}
