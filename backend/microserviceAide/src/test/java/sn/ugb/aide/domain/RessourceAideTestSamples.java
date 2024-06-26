package sn.ugb.aide.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RessourceAideTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RessourceAide getRessourceAideSample1() {
        return new RessourceAide().id(1L).nom("nom1").libelle("libelle1");
    }

    public static RessourceAide getRessourceAideSample2() {
        return new RessourceAide().id(2L).nom("nom2").libelle("libelle2");
    }

    public static RessourceAide getRessourceAideRandomSampleGenerator() {
        return new RessourceAide().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString()).libelle(UUID.randomUUID().toString());
    }
}
