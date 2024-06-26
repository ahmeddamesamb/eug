package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CycleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cycle getCycleSample1() {
        return new Cycle().id(1L).libelleCycle("libelleCycle1");
    }

    public static Cycle getCycleSample2() {
        return new Cycle().id(2L).libelleCycle("libelleCycle2");
    }

    public static Cycle getCycleRandomSampleGenerator() {
        return new Cycle().id(longCount.incrementAndGet()).libelleCycle(UUID.randomUUID().toString());
    }
}
