package sn.ugb.gd.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RapportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Rapport getRapportSample1() {
        return new Rapport()
            .id(1L)
            .libelleRapport("libelleRapport1")
            .descriptionRapport("descriptionRapport1")
            .contenuRapport("contenuRapport1");
    }

    public static Rapport getRapportSample2() {
        return new Rapport()
            .id(2L)
            .libelleRapport("libelleRapport2")
            .descriptionRapport("descriptionRapport2")
            .contenuRapport("contenuRapport2");
    }

    public static Rapport getRapportRandomSampleGenerator() {
        return new Rapport()
            .id(longCount.incrementAndGet())
            .libelleRapport(UUID.randomUUID().toString())
            .descriptionRapport(UUID.randomUUID().toString())
            .contenuRapport(UUID.randomUUID().toString());
    }
}
