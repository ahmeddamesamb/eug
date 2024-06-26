package sn.ugb.edt.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PlanningTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Planning getPlanningSample1() {
        return new Planning().id(1L);
    }

    public static Planning getPlanningSample2() {
        return new Planning().id(2L);
    }

    public static Planning getPlanningRandomSampleGenerator() {
        return new Planning().id(longCount.incrementAndGet());
    }
}
