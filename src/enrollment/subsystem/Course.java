package enrollment.subsystem;

import java.util.Set;

public record Course(String code, String title, int credits, String slot, int capacity, Set<String> prerequisites) {
    public Course {
        if (credits <= 0 || capacity <= 0) {
            throw new IllegalArgumentException("credits and capacity must be positive");
        }
        prerequisites = Set.copyOf(prerequisites);
    }
}
