package enrollment.subsystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TuitionBilling {
    private static final int PRICE_PER_CREDIT = 25_000;

    private final Set<String> blockedAccounts = new HashSet<>();
    private final Map<String, Integer> charges = new HashMap<>();
    private final EventLog log;

    public TuitionBilling(EventLog log) {
        this.log = log;
    }

    public void blockAccount(String studentId) {
        blockedAccounts.add(studentId);
    }

    public int charge(String studentId, Course course) {
        if (blockedAccounts.contains(studentId)) {
            log.record("Billing", "refused " + course.code() + " for " + studentId + ": account blocked");
            throw new BillingBlockedException(studentId);
        }
        int amount = course.credits() * PRICE_PER_CREDIT;
        charges.put(chargeKey(studentId, course), amount);
        log.record("Billing", "invoiced " + amount + " KZT to " + studentId + " for " + course.code());
        return amount;
    }

    public int refund(String studentId, Course course) {
        Integer amount = charges.remove(chargeKey(studentId, course));
        if (amount == null) {
            return 0;
        }
        log.record("Billing", "refunded " + amount + " KZT to " + studentId + " for " + course.code());
        return amount;
    }

    private static String chargeKey(String studentId, Course course) {
        return studentId + "/" + course.code();
    }
}
