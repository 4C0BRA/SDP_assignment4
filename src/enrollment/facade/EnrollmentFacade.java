package enrollment.facade;

import enrollment.model.Student;
import enrollment.subsystem.Course;
import enrollment.subsystem.CourseCatalog;
import enrollment.subsystem.EnrollmentException;
import enrollment.subsystem.NotificationService;
import enrollment.subsystem.PrerequisiteChecker;
import enrollment.subsystem.ScheduleService;
import enrollment.subsystem.SeatRegistry;
import enrollment.subsystem.TuitionBilling;
import java.util.ArrayDeque;
import java.util.Deque;

public class EnrollmentFacade {
    private final CourseCatalog catalog;
    private final PrerequisiteChecker prerequisites;
    private final SeatRegistry seats;
    private final ScheduleService schedule;
    private final TuitionBilling billing;
    private final NotificationService notifications;

    public EnrollmentFacade(CourseCatalog catalog, PrerequisiteChecker prerequisites, SeatRegistry seats,
                            ScheduleService schedule, TuitionBilling billing, NotificationService notifications) {
        this.catalog = catalog;
        this.prerequisites = prerequisites;
        this.seats = seats;
        this.schedule = schedule;
        this.billing = billing;
        this.notifications = notifications;
    }

    public EnrollmentResult enroll(Student student, String courseCode) {
        Deque<Runnable> rollback = new ArrayDeque<>();
        try {
            Course course = catalog.find(courseCode);
            prerequisites.check(student, course);

            seats.reserve(course, student.id());
            rollback.push(() -> seats.release(course, student.id()));

            schedule.add(student.id(), course);
            rollback.push(() -> schedule.remove(student.id(), course));

            int amount = billing.charge(student.id(), course);
            notifications.send(student.email(), "You are enrolled in " + course.code() + " " + course.title()
                    + ", " + course.slot() + ". Invoice: " + amount + " KZT");
            return EnrollmentResult.success(course.code(), "enrolled, " + course.slot() + ", " + amount + " KZT");
        } catch (EnrollmentException failure) {
            rollback.forEach(Runnable::run);
            notifications.send(student.email(), "Enrollment in " + courseCode + " failed: " + failure.getMessage());
            return EnrollmentResult.failure(courseCode, failure.getMessage());
        }
    }

    public EnrollmentResult drop(Student student, String courseCode) {
        if (!schedule.isEnrolled(student.id(), courseCode)) {
            return EnrollmentResult.failure(courseCode, student.id() + " is not enrolled in this course");
        }
        Course course = catalog.find(courseCode);
        schedule.remove(student.id(), course);
        seats.release(course, student.id());
        int refund = billing.refund(student.id(), course);
        notifications.send(student.email(), "You dropped " + course.code() + ". Refund: " + refund + " KZT");
        return EnrollmentResult.success(course.code(), "dropped, refund " + refund + " KZT");
    }
}
