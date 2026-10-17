package enrollment.subsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleService {
    private final Map<String, List<Course>> timetables = new HashMap<>();
    private final EventLog log;

    public ScheduleService(EventLog log) {
        this.log = log;
    }

    public void add(String studentId, Course course) {
        List<Course> timetable = timetableOf(studentId);
        for (Course existing : timetable) {
            if (existing.slot().equals(course.slot())) {
                log.record("Schedule", course.code() + " clashes with " + existing.code() + " at " + course.slot());
                throw new ScheduleConflictException(course, existing);
            }
        }
        timetable.add(course);
        log.record("Schedule", "added " + course.code() + " at " + course.slot() + " for " + studentId);
    }

    public void remove(String studentId, Course course) {
        if (timetableOf(studentId).remove(course)) {
            log.record("Schedule", "removed " + course.code() + " for " + studentId);
        }
    }

    public boolean isEnrolled(String studentId, String courseCode) {
        return timetableOf(studentId).stream().anyMatch(course -> course.code().equals(courseCode));
    }

    public List<String> coursesOf(String studentId) {
        return timetableOf(studentId).stream().map(course -> course.code() + " " + course.slot()).toList();
    }

    private List<Course> timetableOf(String studentId) {
        return timetables.computeIfAbsent(studentId, id -> new ArrayList<>());
    }
}
