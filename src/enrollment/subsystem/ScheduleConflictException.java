package enrollment.subsystem;

public class ScheduleConflictException extends EnrollmentException {
    public ScheduleConflictException(Course course, Course existing) {
        super(course.code() + " at " + course.slot() + " clashes with " + existing.code());
    }
}
