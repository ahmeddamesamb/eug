package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PaiementFraisTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PaiementFrais getPaiementFraisSample1() {
        return new PaiementFrais().id(1L).obligatoireYN(1).echeancePayeeYN(1).emailUser("emailUser1");
    }

    public static PaiementFrais getPaiementFraisSample2() {
        return new PaiementFrais().id(2L).obligatoireYN(2).echeancePayeeYN(2).emailUser("emailUser2");
    }

    public static PaiementFrais getPaiementFraisRandomSampleGenerator() {
        return new PaiementFrais()
            .id(longCount.incrementAndGet())
            .obligatoireYN(intCount.incrementAndGet())
            .echeancePayeeYN(intCount.incrementAndGet())
            .emailUser(UUID.randomUUID().toString());
    }
}
