package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UfrTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Ufr getUfrSample1() {
        return new Ufr().id(1L).libeleUfr("libeleUfr1").sigleUfr("sigleUfr1").systemeLMDYN(1).ordreStat(1);
    }

    public static Ufr getUfrSample2() {
        return new Ufr().id(2L).libeleUfr("libeleUfr2").sigleUfr("sigleUfr2").systemeLMDYN(2).ordreStat(2);
    }

    public static Ufr getUfrRandomSampleGenerator() {
        return new Ufr()
            .id(longCount.incrementAndGet())
            .libeleUfr(UUID.randomUUID().toString())
            .sigleUfr(UUID.randomUUID().toString())
            .systemeLMDYN(intCount.incrementAndGet())
            .ordreStat(intCount.incrementAndGet());
    }
}
