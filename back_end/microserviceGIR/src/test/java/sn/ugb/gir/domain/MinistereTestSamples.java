package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MinistereTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Ministere getMinistereSample1() {
        return new Ministere().id(1L).nomMinistere("nomMinistere1").sigleMinistere("sigleMinistere1").enCoursYN(1);
    }

    public static Ministere getMinistereSample2() {
        return new Ministere().id(2L).nomMinistere("nomMinistere2").sigleMinistere("sigleMinistere2").enCoursYN(2);
    }

    public static Ministere getMinistereRandomSampleGenerator() {
        return new Ministere()
            .id(longCount.incrementAndGet())
            .nomMinistere(UUID.randomUUID().toString())
            .sigleMinistere(UUID.randomUUID().toString())
            .enCoursYN(intCount.incrementAndGet());
    }
}
