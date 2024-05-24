package sn.ugb.aua.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LaboratoireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Laboratoire getLaboratoireSample1() {
        return new Laboratoire().id(1L).nomLaboratoire("nomLaboratoire1").laboratoireCotutelleYN(1);
    }

    public static Laboratoire getLaboratoireSample2() {
        return new Laboratoire().id(2L).nomLaboratoire("nomLaboratoire2").laboratoireCotutelleYN(2);
    }

    public static Laboratoire getLaboratoireRandomSampleGenerator() {
        return new Laboratoire()
            .id(longCount.incrementAndGet())
            .nomLaboratoire(UUID.randomUUID().toString())
            .laboratoireCotutelleYN(intCount.incrementAndGet());
    }
}
