package enrollment.subsystem;

public class AlreadyEnrolledException extends EnrollmentException {
    public AlreadyEnrolledException(String studentId, String courseCode) {
        super(studentId + " is already enrolled in " + courseCode);
    }
}
