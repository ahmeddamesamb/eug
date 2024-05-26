package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FormationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Formation getFormationSample1() {
        return new Formation()
            .id(1L)
            .fraisDossierYN(1)
            .classeDiplomanteYN(1)
            .libelleDiplome("libelleDiplome1")
            .codeFormation("codeFormation1")
            .nbreCreditsMin(1)
            .estParcoursYN(1)
            .lmdYN(1);
    }

    public static Formation getFormationSample2() {
        return new Formation()
            .id(2L)
            .fraisDossierYN(2)
            .classeDiplomanteYN(2)
            .libelleDiplome("libelleDiplome2")
            .codeFormation("codeFormation2")
            .nbreCreditsMin(2)
            .estParcoursYN(2)
            .lmdYN(2);
    }

    public static Formation getFormationRandomSampleGenerator() {
        return new Formation()
            .id(longCount.incrementAndGet())
            .fraisDossierYN(intCount.incrementAndGet())
            .classeDiplomanteYN(intCount.incrementAndGet())
            .libelleDiplome(UUID.randomUUID().toString())
            .codeFormation(UUID.randomUUID().toString())
            .nbreCreditsMin(intCount.incrementAndGet())
            .estParcoursYN(intCount.incrementAndGet())
            .lmdYN(intCount.incrementAndGet());
    }
}
