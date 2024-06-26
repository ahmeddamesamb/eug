package sn.ugb.gateway.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class InfosUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InfosUser getInfosUserSample1() {
        return new InfosUser().id(1L);
    }

    public static InfosUser getInfosUserSample2() {
        return new InfosUser().id(2L);
    }

    public static InfosUser getInfosUserRandomSampleGenerator() {
        return new InfosUser().id(longCount.incrementAndGet());
    }
}
