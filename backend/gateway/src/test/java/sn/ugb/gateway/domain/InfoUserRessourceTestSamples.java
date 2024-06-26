package sn.ugb.gateway.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class InfoUserRessourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InfoUserRessource getInfoUserRessourceSample1() {
        return new InfoUserRessource().id(1L);
    }

    public static InfoUserRessource getInfoUserRessourceSample2() {
        return new InfoUserRessource().id(2L);
    }

    public static InfoUserRessource getInfoUserRessourceRandomSampleGenerator() {
        return new InfoUserRessource().id(longCount.incrementAndGet());
    }
}
