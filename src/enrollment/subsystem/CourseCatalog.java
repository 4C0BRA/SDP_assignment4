package enrollment.subsystem;

import java.util.HashMap;
import java.util.Map;

public class CourseCatalog {
    private final Map<String, Course> coursesByCode = new HashMap<>();

    public void add(Course course) {
        coursesByCode.put(course.code(), course);
    }

    public Course find(String courseCode) {
        Course course = coursesByCode.get(courseCode);
        if (course == null) {
            throw new CourseNotFoundException(courseCode);
        }
        return course;
    }
}
