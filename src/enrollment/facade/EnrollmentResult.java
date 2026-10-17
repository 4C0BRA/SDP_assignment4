package enrollment.facade;

public record EnrollmentResult(String courseCode, boolean successful, String message) {
    static EnrollmentResult success(String courseCode, String message) {
        return new EnrollmentResult(courseCode, true, message);
    }

    static EnrollmentResult failure(String courseCode, String message) {
        return new EnrollmentResult(courseCode, false, message);
    }

    @Override
    public String toString() {
        return (successful ? "SUCCESS " : "FAILED ") + courseCode + ": " + message;
    }
}
