package enrollment.subsystem;

import enrollment.model.Student;
import java.util.Set;
import java.util.TreeSet;

public class PrerequisiteChecker {
    private final EventLog log;

    public PrerequisiteChecker(EventLog log) {
        this.log = log;
    }

    public void check(Student student, Course course) {
        Set<String> missing = new TreeSet<>();
        for (String prerequisite : course.prerequisites()) {
            if (!student.hasCompleted(prerequisite)) {
                missing.add(prerequisite);
            }
        }
        if (!missing.isEmpty()) {
            log.record("Prerequisites", student.id() + " has not completed " + missing + " for " + course.code());
            throw new MissingPrerequisiteException(course.code(), missing);
        }
        log.record("Prerequisites", student.id() + " meets the requirements of " + course.code());
    }
}
