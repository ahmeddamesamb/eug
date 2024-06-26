package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessusInscriptionAdministrativeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProcessusInscriptionAdministrative getProcessusInscriptionAdministrativeSample1() {
        return new ProcessusInscriptionAdministrative().id(1L).numeroQuitusCROUS(1).dateRemiseCarteDup(1).emailUser("emailUser1");
    }

    public static ProcessusInscriptionAdministrative getProcessusInscriptionAdministrativeSample2() {
        return new ProcessusInscriptionAdministrative().id(2L).numeroQuitusCROUS(2).dateRemiseCarteDup(2).emailUser("emailUser2");
    }

    public static ProcessusInscriptionAdministrative getProcessusInscriptionAdministrativeRandomSampleGenerator() {
        return new ProcessusInscriptionAdministrative()
            .id(longCount.incrementAndGet())
            .numeroQuitusCROUS(intCount.incrementAndGet())
            .dateRemiseCarteDup(intCount.incrementAndGet())
            .emailUser(UUID.randomUUID().toString());
    }
}
