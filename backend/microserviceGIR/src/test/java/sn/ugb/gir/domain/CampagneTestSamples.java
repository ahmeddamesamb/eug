package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CampagneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Campagne getCampagneSample1() {
        return new Campagne().id(1L).libelleCampagne("libelleCampagne1").libelleAbrege("libelleAbrege1");
    }

    public static Campagne getCampagneSample2() {
        return new Campagne().id(2L).libelleCampagne("libelleCampagne2").libelleAbrege("libelleAbrege2");
    }

    public static Campagne getCampagneRandomSampleGenerator() {
        return new Campagne()
            .id(longCount.incrementAndGet())
            .libelleCampagne(UUID.randomUUID().toString())
            .libelleAbrege(UUID.randomUUID().toString());
    }
}
