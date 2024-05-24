package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DoctoratTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Doctorat getDoctoratSample1() {
        return new Doctorat().id(1L).sujet("sujet1").encadreurId(1).laboratoirId(1);
    }

    public static Doctorat getDoctoratSample2() {
        return new Doctorat().id(2L).sujet("sujet2").encadreurId(2).laboratoirId(2);
    }

    public static Doctorat getDoctoratRandomSampleGenerator() {
        return new Doctorat()
            .id(longCount.incrementAndGet())
            .sujet(UUID.randomUUID().toString())
            .encadreurId(intCount.incrementAndGet())
            .laboratoirId(intCount.incrementAndGet());
    }
}
