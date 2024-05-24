package sn.ugb.aide.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RessourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ressource getRessourceSample1() {
        return new Ressource().id(1L).nomRessource("nomRessource1").descriptionRessource("descriptionRessource1");
    }

    public static Ressource getRessourceSample2() {
        return new Ressource().id(2L).nomRessource("nomRessource2").descriptionRessource("descriptionRessource2");
    }

    public static Ressource getRessourceRandomSampleGenerator() {
        return new Ressource()
            .id(longCount.incrementAndGet())
            .nomRessource(UUID.randomUUID().toString())
            .descriptionRessource(UUID.randomUUID().toString());
    }
}
