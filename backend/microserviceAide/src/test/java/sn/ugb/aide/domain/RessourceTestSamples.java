package sn.ugb.aide.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RessourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ressource getRessourceSample1() {
        return new Ressource().id(1L).nom("nom1").description("description1");
    }

    public static Ressource getRessourceSample2() {
        return new Ressource().id(2L).nom("nom2").description("description2");
    }

    public static Ressource getRessourceRandomSampleGenerator() {
        return new Ressource().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
