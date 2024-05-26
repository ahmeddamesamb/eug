package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LyceeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Lycee getLyceeSample1() {
        return new Lycee()
            .id(1L)
            .nomLycee("nomLycee1")
            .codeLycee("codeLycee1")
            .villeLycee("villeLycee1")
            .academieLycee(1)
            .centreExamen("centreExamen1");
    }

    public static Lycee getLyceeSample2() {
        return new Lycee()
            .id(2L)
            .nomLycee("nomLycee2")
            .codeLycee("codeLycee2")
            .villeLycee("villeLycee2")
            .academieLycee(2)
            .centreExamen("centreExamen2");
    }

    public static Lycee getLyceeRandomSampleGenerator() {
        return new Lycee()
            .id(longCount.incrementAndGet())
            .nomLycee(UUID.randomUUID().toString())
            .codeLycee(UUID.randomUUID().toString())
            .villeLycee(UUID.randomUUID().toString())
            .academieLycee(intCount.incrementAndGet())
            .centreExamen(UUID.randomUUID().toString());
    }
}
