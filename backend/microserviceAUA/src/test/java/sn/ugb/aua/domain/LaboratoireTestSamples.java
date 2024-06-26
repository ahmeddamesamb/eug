package sn.ugb.aua.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LaboratoireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Laboratoire getLaboratoireSample1() {
        return new Laboratoire().id(1L).nom("nom1");
    }

    public static Laboratoire getLaboratoireSample2() {
        return new Laboratoire().id(2L).nom("nom2");
    }

    public static Laboratoire getLaboratoireRandomSampleGenerator() {
        return new Laboratoire().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString());
    }
}
