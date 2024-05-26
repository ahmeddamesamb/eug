package sn.ugb.gd.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeRapportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeRapport getTypeRapportSample1() {
        return new TypeRapport().id(1L).libelleTypeRapport("libelleTypeRapport1");
    }

    public static TypeRapport getTypeRapportSample2() {
        return new TypeRapport().id(2L).libelleTypeRapport("libelleTypeRapport2");
    }

    public static TypeRapport getTypeRapportRandomSampleGenerator() {
        return new TypeRapport().id(longCount.incrementAndGet()).libelleTypeRapport(UUID.randomUUID().toString());
    }
}
