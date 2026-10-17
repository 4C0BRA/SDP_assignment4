package enrollment.subsystem;

public class CourseNotFoundException extends EnrollmentException {
    public CourseNotFoundException(String courseCode) {
        super("no course with code " + courseCode);
    }
}
