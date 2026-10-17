package enrollment.subsystem;

import java.io.PrintStream;

public class EventLog {
    private final PrintStream out;

    public EventLog(PrintStream out) {
        this.out = out;
    }

    public void record(String source, String message) {
        out.println("    [" + source + "] " + message);
    }
}
