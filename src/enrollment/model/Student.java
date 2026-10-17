package enrollment.model;

import java.util.Objects;
import java.util.Set;

public record Student(String id, String name, String email, Set<String> completedCourses) {
    public Student {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(email, "email");
        completedCourses = Set.copyOf(completedCourses);
    }

    public boolean hasCompleted(String courseCode) {
        return completedCourses.contains(courseCode);
    }
}
