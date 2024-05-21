package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DisciplineSportiveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DisciplineSportive getDisciplineSportiveSample1() {
        return new DisciplineSportive().id(1L).libelleDisciplineSportive("libelleDisciplineSportive1");
    }

    public static DisciplineSportive getDisciplineSportiveSample2() {
        return new DisciplineSportive().id(2L).libelleDisciplineSportive("libelleDisciplineSportive2");
    }

    public static DisciplineSportive getDisciplineSportiveRandomSampleGenerator() {
        return new DisciplineSportive().id(longCount.incrementAndGet()).libelleDisciplineSportive(UUID.randomUUID().toString());
    }
}
