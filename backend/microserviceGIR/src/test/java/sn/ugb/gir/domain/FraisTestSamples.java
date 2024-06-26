package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FraisTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Frais getFraisSample1() {
        return new Frais().id(1L).descriptionFrais("descriptionFrais1");
    }

    public static Frais getFraisSample2() {
        return new Frais().id(2L).descriptionFrais("descriptionFrais2");
    }

    public static Frais getFraisRandomSampleGenerator() {
        return new Frais().id(longCount.incrementAndGet()).descriptionFrais(UUID.randomUUID().toString());
    }
}
