package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeFormationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeFormation getTypeFormationSample1() {
        return new TypeFormation().id(1L).libelleTypeFormation("libelleTypeFormation1");
    }

    public static TypeFormation getTypeFormationSample2() {
        return new TypeFormation().id(2L).libelleTypeFormation("libelleTypeFormation2");
    }

    public static TypeFormation getTypeFormationRandomSampleGenerator() {
        return new TypeFormation().id(longCount.incrementAndGet()).libelleTypeFormation(UUID.randomUUID().toString());
    }
}
