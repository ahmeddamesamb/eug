package sn.ugb.gateway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RessourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ressource getRessourceSample1() {
        return new Ressource().id(1L).libelle("libelle1");
    }

    public static Ressource getRessourceSample2() {
        return new Ressource().id(2L).libelle("libelle2");
    }

    public static Ressource getRessourceRandomSampleGenerator() {
        return new Ressource().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
