package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PaiementFormationPriveeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PaiementFormationPrivee getPaiementFormationPriveeSample1() {
        return new PaiementFormationPrivee()
            .id(1L)
            .moisPaiement("moisPaiement1")
            .anneePaiement("anneePaiement1")
            .payerMensualiteYN(1)
            .emailUser("emailUser1");
    }

    public static PaiementFormationPrivee getPaiementFormationPriveeSample2() {
        return new PaiementFormationPrivee()
            .id(2L)
            .moisPaiement("moisPaiement2")
            .anneePaiement("anneePaiement2")
            .payerMensualiteYN(2)
            .emailUser("emailUser2");
    }

    public static PaiementFormationPrivee getPaiementFormationPriveeRandomSampleGenerator() {
        return new PaiementFormationPrivee()
            .id(longCount.incrementAndGet())
            .moisPaiement(UUID.randomUUID().toString())
            .anneePaiement(UUID.randomUUID().toString())
            .payerMensualiteYN(intCount.incrementAndGet())
            .emailUser(UUID.randomUUID().toString());
    }
}
