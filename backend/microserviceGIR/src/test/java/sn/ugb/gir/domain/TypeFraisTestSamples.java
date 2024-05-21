package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeFraisTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeFrais getTypeFraisSample1() {
        return new TypeFrais().id(1L).libelle("libelle1");
    }

    public static TypeFrais getTypeFraisSample2() {
        return new TypeFrais().id(2L).libelle("libelle2");
    }

    public static TypeFrais getTypeFraisRandomSampleGenerator() {
        return new TypeFrais().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
