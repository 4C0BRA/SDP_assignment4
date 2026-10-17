package enrollment.subsystem;

public abstract class EnrollmentException extends RuntimeException {
    protected EnrollmentException(String message) {
        super(message);
    }
}
