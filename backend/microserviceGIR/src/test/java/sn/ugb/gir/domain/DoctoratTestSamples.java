package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DoctoratTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Doctorat getDoctoratSample1() {
        return new Doctorat().id(1L).sujet("sujet1").encadreurId(1L).laboratoirId(1L);
    }

    public static Doctorat getDoctoratSample2() {
        return new Doctorat().id(2L).sujet("sujet2").encadreurId(2L).laboratoirId(2L);
    }

    public static Doctorat getDoctoratRandomSampleGenerator() {
        return new Doctorat()
            .id(longCount.incrementAndGet())
            .sujet(UUID.randomUUID().toString())
            .encadreurId(longCount.incrementAndGet())
            .laboratoirId(longCount.incrementAndGet());
    }
}
