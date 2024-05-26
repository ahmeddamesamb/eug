package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InscriptionDoctoratTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InscriptionDoctorat getInscriptionDoctoratSample1() {
        return new InscriptionDoctorat()
            .id(1L)
            .sourceFinancement("sourceFinancement1")
            .coEncadreurId("coEncadreurId1")
            .nombreInscription(1);
    }

    public static InscriptionDoctorat getInscriptionDoctoratSample2() {
        return new InscriptionDoctorat()
            .id(2L)
            .sourceFinancement("sourceFinancement2")
            .coEncadreurId("coEncadreurId2")
            .nombreInscription(2);
    }

    public static InscriptionDoctorat getInscriptionDoctoratRandomSampleGenerator() {
        return new InscriptionDoctorat()
            .id(longCount.incrementAndGet())
            .sourceFinancement(UUID.randomUUID().toString())
            .coEncadreurId(UUID.randomUUID().toString())
            .nombreInscription(intCount.incrementAndGet());
    }
}
