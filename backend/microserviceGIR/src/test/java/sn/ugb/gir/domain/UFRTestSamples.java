package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UFRTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UFR getUFRSample1() {
        return new UFR().id(1L).libeleUFR("libeleUFR1").sigleUFR("sigleUFR1").systemeLMDYN(1).ordreStat(1);
    }

    public static UFR getUFRSample2() {
        return new UFR().id(2L).libeleUFR("libeleUFR2").sigleUFR("sigleUFR2").systemeLMDYN(2).ordreStat(2);
    }

    public static UFR getUFRRandomSampleGenerator() {
        return new UFR()
            .id(longCount.incrementAndGet())
            .libeleUFR(UUID.randomUUID().toString())
            .sigleUFR(UUID.randomUUID().toString())
            .systemeLMDYN(intCount.incrementAndGet())
            .ordreStat(intCount.incrementAndGet());
    }
}
