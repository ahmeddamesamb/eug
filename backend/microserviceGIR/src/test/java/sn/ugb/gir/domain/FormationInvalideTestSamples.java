package sn.ugb.gir.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FormationInvalideTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FormationInvalide getFormationInvalideSample1() {
        return new FormationInvalide().id(1L);
    }

    public static FormationInvalide getFormationInvalideSample2() {
        return new FormationInvalide().id(2L);
    }

    public static FormationInvalide getFormationInvalideRandomSampleGenerator() {
        return new FormationInvalide().id(longCount.incrementAndGet());
    }
}
