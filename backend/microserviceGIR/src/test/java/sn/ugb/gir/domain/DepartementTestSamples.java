package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepartementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Departement getDepartementSample1() {
        return new Departement().id(1L).nomDepatement("nomDepatement1");
    }

    public static Departement getDepartementSample2() {
        return new Departement().id(2L).nomDepatement("nomDepatement2");
    }

    public static Departement getDepartementRandomSampleGenerator() {
        return new Departement().id(longCount.incrementAndGet()).nomDepatement(UUID.randomUUID().toString());
    }
}
