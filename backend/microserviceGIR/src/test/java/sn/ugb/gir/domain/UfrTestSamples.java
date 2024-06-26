package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UfrTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ufr getUfrSample1() {
        return new Ufr().id(1L).libelleUfr("libelleUfr1").sigleUfr("sigleUfr1").prefixe("prefixe1");
    }

    public static Ufr getUfrSample2() {
        return new Ufr().id(2L).libelleUfr("libelleUfr2").sigleUfr("sigleUfr2").prefixe("prefixe2");
    }

    public static Ufr getUfrRandomSampleGenerator() {
        return new Ufr()
            .id(longCount.incrementAndGet())
            .libelleUfr(UUID.randomUUID().toString())
            .sigleUfr(UUID.randomUUID().toString())
            .prefixe(UUID.randomUUID().toString());
    }
}
