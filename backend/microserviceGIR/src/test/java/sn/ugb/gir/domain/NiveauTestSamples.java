package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NiveauTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Niveau getNiveauSample1() {
        return new Niveau()
            .id(1L)
            .libelleNiveau("libelleNiveau1")
            .cycleNiveau("cycleNiveau1")
            .codeNiveau("codeNiveau1")
            .anneeEtude("anneeEtude1");
    }

    public static Niveau getNiveauSample2() {
        return new Niveau()
            .id(2L)
            .libelleNiveau("libelleNiveau2")
            .cycleNiveau("cycleNiveau2")
            .codeNiveau("codeNiveau2")
            .anneeEtude("anneeEtude2");
    }

    public static Niveau getNiveauRandomSampleGenerator() {
        return new Niveau()
            .id(longCount.incrementAndGet())
            .libelleNiveau(UUID.randomUUID().toString())
            .cycleNiveau(UUID.randomUUID().toString())
            .codeNiveau(UUID.randomUUID().toString())
            .anneeEtude(UUID.randomUUID().toString());
    }
}
