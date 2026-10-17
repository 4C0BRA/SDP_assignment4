package enrollment;

import enrollment.config.DemoUniversity;
import enrollment.facade.EnrollmentFacade;
import enrollment.facade.EnrollmentResult;
import enrollment.model.Student;
import java.util.Set;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        EnrollmentFacade registrar = DemoUniversity.createEnrollment();

        Student aruzhan = new Student("S-ARUZHAN", "Aruzhan", "aruzhan@astanait.edu.kz", Set.of("SE-201"));
        Student dias = new Student("S-DIAS", "Dias", "dias@astanait.edu.kz", Set.of());
        Student timur = new Student("S-TIMUR", "Timur", "timur@astanait.edu.kz", Set.of("SE-201"));

        run("1. Successful enrollment", () -> registrar.enroll(aruzhan, "SE-301"));
        run("2. Missing prerequisite (nothing reserved)", () -> registrar.enroll(dias, "SE-301"));
        run("3. Last free seat", () -> registrar.enroll(aruzhan, "DB-210"));
        run("4. Course is full", () -> registrar.enroll(dias, "DB-210"));
        run("5. Time clash (seat released again)", () -> registrar.enroll(aruzhan, "MA-220"));
        run("6. Billing blocked (schedule and seat rolled back)", () -> registrar.enroll(timur, "SE-301"));
        run("7. Already enrolled", () -> registrar.enroll(aruzhan, "SE-301"));
        run("8. Drop a course (refund)", () -> registrar.drop(aruzhan, "SE-301"));
        run("9. Drop a course the student is not in", () -> registrar.drop(dias, "SE-301"));
        run("10. Unknown course code", () -> registrar.enroll(dias, "CS-999"));
    }

    private static void run(String title, Supplier<EnrollmentResult> action) {
        System.out.println("=== " + title);
        System.out.println("  -> " + action.get());
        System.out.println();
    }
}
