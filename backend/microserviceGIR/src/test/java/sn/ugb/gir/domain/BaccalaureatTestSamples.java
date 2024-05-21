package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BaccalaureatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Baccalaureat getBaccalaureatSample1() {
        return new Baccalaureat()
            .id(1L)
            .origineScolaire("origineScolaire1")
            .numeroTable(1)
            .natureBac("natureBac1")
            .mentionBac("mentionBac1");
    }

    public static Baccalaureat getBaccalaureatSample2() {
        return new Baccalaureat()
            .id(2L)
            .origineScolaire("origineScolaire2")
            .numeroTable(2)
            .natureBac("natureBac2")
            .mentionBac("mentionBac2");
    }

    public static Baccalaureat getBaccalaureatRandomSampleGenerator() {
        return new Baccalaureat()
            .id(longCount.incrementAndGet())
            .origineScolaire(UUID.randomUUID().toString())
            .numeroTable(intCount.incrementAndGet())
            .natureBac(UUID.randomUUID().toString())
            .mentionBac(UUID.randomUUID().toString());
    }
}
