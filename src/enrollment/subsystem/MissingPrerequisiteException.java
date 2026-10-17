package enrollment.subsystem;

import java.util.Set;

public class MissingPrerequisiteException extends EnrollmentException {
    public MissingPrerequisiteException(String courseCode, Set<String> missing) {
        super(courseCode + " requires " + String.join(", ", missing));
    }
}
