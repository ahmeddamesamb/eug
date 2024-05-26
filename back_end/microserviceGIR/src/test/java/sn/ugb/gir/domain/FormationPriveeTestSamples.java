package sn.ugb.gir.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FormationPriveeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FormationPrivee getFormationPriveeSample1() {
        return new FormationPrivee().id(1L).nombreMensualites(1).paiementPremierMoisYN(1).paiementDernierMoisYN(1).fraisDossierYN(1);
    }

    public static FormationPrivee getFormationPriveeSample2() {
        return new FormationPrivee().id(2L).nombreMensualites(2).paiementPremierMoisYN(2).paiementDernierMoisYN(2).fraisDossierYN(2);
    }

    public static FormationPrivee getFormationPriveeRandomSampleGenerator() {
        return new FormationPrivee()
            .id(longCount.incrementAndGet())
            .nombreMensualites(intCount.incrementAndGet())
            .paiementPremierMoisYN(intCount.incrementAndGet())
            .paiementDernierMoisYN(intCount.incrementAndGet())
            .fraisDossierYN(intCount.incrementAndGet());
    }
}
