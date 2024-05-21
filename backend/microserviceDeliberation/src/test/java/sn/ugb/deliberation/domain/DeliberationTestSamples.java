package sn.ugb.deliberation.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DeliberationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Deliberation getDeliberationSample1() {
        return new Deliberation().id(1L).estValidee(1);
    }

    public static Deliberation getDeliberationSample2() {
        return new Deliberation().id(2L).estValidee(2);
    }

    public static Deliberation getDeliberationRandomSampleGenerator() {
        return new Deliberation().id(longCount.incrementAndGet()).estValidee(intCount.incrementAndGet());
    }
}
