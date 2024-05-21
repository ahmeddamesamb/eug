package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeSelectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeSelection getTypeSelectionSample1() {
        return new TypeSelection().id(1L).libelle("libelle1");
    }

    public static TypeSelection getTypeSelectionSample2() {
        return new TypeSelection().id(2L).libelle("libelle2");
    }

    public static TypeSelection getTypeSelectionRandomSampleGenerator() {
        return new TypeSelection().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
