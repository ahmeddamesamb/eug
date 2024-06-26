package sn.ugb.gir.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class InscriptionAdministrativeFormationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InscriptionAdministrativeFormation getInscriptionAdministrativeFormationSample1() {
        return new InscriptionAdministrativeFormation().id(1L);
    }

    public static InscriptionAdministrativeFormation getInscriptionAdministrativeFormationSample2() {
        return new InscriptionAdministrativeFormation().id(2L);
    }

    public static InscriptionAdministrativeFormation getInscriptionAdministrativeFormationRandomSampleGenerator() {
        return new InscriptionAdministrativeFormation().id(longCount.incrementAndGet());
    }
}
