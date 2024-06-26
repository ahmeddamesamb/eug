package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SerieTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Serie getSerieSample1() {
        return new Serie().id(1L).codeSerie("codeSerie1").libelleSerie("libelleSerie1").sigleSerie("sigleSerie1");
    }

    public static Serie getSerieSample2() {
        return new Serie().id(2L).codeSerie("codeSerie2").libelleSerie("libelleSerie2").sigleSerie("sigleSerie2");
    }

    public static Serie getSerieRandomSampleGenerator() {
        return new Serie()
            .id(longCount.incrementAndGet())
            .codeSerie(UUID.randomUUID().toString())
            .libelleSerie(UUID.randomUUID().toString())
            .sigleSerie(UUID.randomUUID().toString());
    }
}
