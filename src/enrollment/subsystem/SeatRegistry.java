package enrollment.subsystem;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SeatRegistry {
    private final Map<String, Set<String>> rostersByCourse = new HashMap<>();
    private final EventLog log;

    public SeatRegistry(EventLog log) {
        this.log = log;
    }

    public void reserve(Course course, String studentId) {
        Set<String> roster = rosterOf(course);
        if (roster.contains(studentId)) {
            throw new AlreadyEnrolledException(studentId, course.code());
        }
        if (roster.size() >= course.capacity()) {
            log.record("Seats", course.code() + " has no free seats for " + studentId);
            throw new CourseFullException(course.code(), course.capacity());
        }
        roster.add(studentId);
        log.record("Seats", "reserved seat in " + course.code() + " for " + studentId
                + " (" + freeSeats(course) + " left)");
    }

    public void release(Course course, String studentId) {
        if (rosterOf(course).remove(studentId)) {
            log.record("Seats", "released seat in " + course.code() + " of " + studentId);
        }
    }

    public int freeSeats(Course course) {
        return course.capacity() - rosterOf(course).size();
    }

    private Set<String> rosterOf(Course course) {
        return rostersByCourse.computeIfAbsent(course.code(), code -> new LinkedHashSet<>());
    }
}
