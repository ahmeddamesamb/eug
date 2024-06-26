package sn.ugb.gir.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InscriptionAdministrativeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InscriptionAdministrative getInscriptionAdministrativeSample1() {
        return new InscriptionAdministrative().id(1L).ordreInscription(1);
    }

    public static InscriptionAdministrative getInscriptionAdministrativeSample2() {
        return new InscriptionAdministrative().id(2L).ordreInscription(2);
    }

    public static InscriptionAdministrative getInscriptionAdministrativeRandomSampleGenerator() {
        return new InscriptionAdministrative().id(longCount.incrementAndGet()).ordreInscription(intCount.incrementAndGet());
    }
}
