package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaysTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pays getPaysSample1() {
        return new Pays()
            .id(1L)
            .libellePays("libellePays1")
            .paysEnAnglais("paysEnAnglais1")
            .nationalite("nationalite1")
            .codePays("codePays1");
    }

    public static Pays getPaysSample2() {
        return new Pays()
            .id(2L)
            .libellePays("libellePays2")
            .paysEnAnglais("paysEnAnglais2")
            .nationalite("nationalite2")
            .codePays("codePays2");
    }

    public static Pays getPaysRandomSampleGenerator() {
        return new Pays()
            .id(longCount.incrementAndGet())
            .libellePays(UUID.randomUUID().toString())
            .paysEnAnglais(UUID.randomUUID().toString())
            .nationalite(UUID.randomUUID().toString())
            .codePays(UUID.randomUUID().toString());
    }
}
