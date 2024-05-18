package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeHandicapTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeHandicap getTypeHandicapSample1() {
        return new TypeHandicap().id(1L).libelleTypeHandicap("libelleTypeHandicap1");
    }

    public static TypeHandicap getTypeHandicapSample2() {
        return new TypeHandicap().id(2L).libelleTypeHandicap("libelleTypeHandicap2");
    }

    public static TypeHandicap getTypeHandicapRandomSampleGenerator() {
        return new TypeHandicap().id(longCount.incrementAndGet()).libelleTypeHandicap(UUID.randomUUID().toString());
    }
}
