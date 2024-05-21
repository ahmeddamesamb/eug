package sn.ugb.gir.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FormationValideTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FormationValide getFormationValideSample1() {
        return new FormationValide().id(1L).valideYN(1);
    }

    public static FormationValide getFormationValideSample2() {
        return new FormationValide().id(2L).valideYN(2);
    }

    public static FormationValide getFormationValideRandomSampleGenerator() {
        return new FormationValide().id(longCount.incrementAndGet()).valideYN(intCount.incrementAndGet());
    }
}
