package enrollment.subsystem;

public class BillingBlockedException extends EnrollmentException {
    public BillingBlockedException(String studentId) {
        super("billing account of " + studentId + " is blocked (unpaid debt)");
    }
}
