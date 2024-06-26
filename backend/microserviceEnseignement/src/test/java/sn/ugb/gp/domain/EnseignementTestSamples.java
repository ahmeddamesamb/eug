package sn.ugb.gp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EnseignementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Enseignement getEnseignementSample1() {
        return new Enseignement().id(1L).libelleEnseignements("libelleEnseignements1").nombreInscrits(1);
    }

    public static Enseignement getEnseignementSample2() {
        return new Enseignement().id(2L).libelleEnseignements("libelleEnseignements2").nombreInscrits(2);
    }

    public static Enseignement getEnseignementRandomSampleGenerator() {
        return new Enseignement()
            .id(longCount.incrementAndGet())
            .libelleEnseignements(UUID.randomUUID().toString())
            .nombreInscrits(intCount.incrementAndGet());
    }
}
