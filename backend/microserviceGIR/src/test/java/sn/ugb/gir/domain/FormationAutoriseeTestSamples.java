package sn.ugb.gir.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FormationAutoriseeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FormationAutorisee getFormationAutoriseeSample1() {
        return new FormationAutorisee().id(1L);
    }

    public static FormationAutorisee getFormationAutoriseeSample2() {
        return new FormationAutorisee().id(2L);
    }

    public static FormationAutorisee getFormationAutoriseeRandomSampleGenerator() {
        return new FormationAutorisee().id(longCount.incrementAndGet());
    }
}
