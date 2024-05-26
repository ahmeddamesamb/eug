package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FraisTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Frais getFraisSample1() {
        return new Frais().id(1L).descriptionFrais("descriptionFrais1").fraisPourAssimileYN(1).estEnApplicationYN(1);
    }

    public static Frais getFraisSample2() {
        return new Frais().id(2L).descriptionFrais("descriptionFrais2").fraisPourAssimileYN(2).estEnApplicationYN(2);
    }

    public static Frais getFraisRandomSampleGenerator() {
        return new Frais()
            .id(longCount.incrementAndGet())
            .descriptionFrais(UUID.randomUUID().toString())
            .fraisPourAssimileYN(intCount.incrementAndGet())
            .estEnApplicationYN(intCount.incrementAndGet());
    }
}
