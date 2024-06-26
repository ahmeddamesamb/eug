package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UniversiteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Universite getUniversiteSample1() {
        return new Universite().id(1L).nomUniversite("nomUniversite1").sigleUniversite("sigleUniversite1");
    }

    public static Universite getUniversiteSample2() {
        return new Universite().id(2L).nomUniversite("nomUniversite2").sigleUniversite("sigleUniversite2");
    }

    public static Universite getUniversiteRandomSampleGenerator() {
        return new Universite()
            .id(longCount.incrementAndGet())
            .nomUniversite(UUID.randomUUID().toString())
            .sigleUniversite(UUID.randomUUID().toString());
    }
}
