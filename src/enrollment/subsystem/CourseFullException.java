package enrollment.subsystem;

public class CourseFullException extends EnrollmentException {
    public CourseFullException(String courseCode, int capacity) {
        super(courseCode + " is full (" + capacity + " of " + capacity + " seats taken)");
    }
}
