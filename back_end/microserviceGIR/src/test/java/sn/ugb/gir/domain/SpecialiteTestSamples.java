package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SpecialiteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Specialite getSpecialiteSample1() {
        return new Specialite()
            .id(1L)
            .nomSpecialites("nomSpecialites1")
            .sigleSpecialites("sigleSpecialites1")
            .specialiteParticulierYN(1)
            .specialitesPayanteYN(1);
    }

    public static Specialite getSpecialiteSample2() {
        return new Specialite()
            .id(2L)
            .nomSpecialites("nomSpecialites2")
            .sigleSpecialites("sigleSpecialites2")
            .specialiteParticulierYN(2)
            .specialitesPayanteYN(2);
    }

    public static Specialite getSpecialiteRandomSampleGenerator() {
        return new Specialite()
            .id(longCount.incrementAndGet())
            .nomSpecialites(UUID.randomUUID().toString())
            .sigleSpecialites(UUID.randomUUID().toString())
            .specialiteParticulierYN(intCount.incrementAndGet())
            .specialitesPayanteYN(intCount.incrementAndGet());
    }
}
