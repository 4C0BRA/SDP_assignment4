package enrollment.subsystem;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final List<String> sentMessages = new ArrayList<>();
    private final EventLog log;

    public NotificationService(EventLog log) {
        this.log = log;
    }

    public void send(String email, String message) {
        sentMessages.add(email + ": " + message);
        log.record("Notification", "to " + email + ": " + message);
    }

    public List<String> sentMessages() {
        return List.copyOf(sentMessages);
    }
}
