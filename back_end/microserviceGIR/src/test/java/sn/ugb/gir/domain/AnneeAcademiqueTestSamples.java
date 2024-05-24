package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AnneeAcademiqueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AnneeAcademique getAnneeAcademiqueSample1() {
        return new AnneeAcademique().id(1L).libelleAnneeAcademique("libelleAnneeAcademique1").anneeAc("anneeAc1").anneeCouranteYN(1);
    }

    public static AnneeAcademique getAnneeAcademiqueSample2() {
        return new AnneeAcademique().id(2L).libelleAnneeAcademique("libelleAnneeAcademique2").anneeAc("anneeAc2").anneeCouranteYN(2);
    }

    public static AnneeAcademique getAnneeAcademiqueRandomSampleGenerator() {
        return new AnneeAcademique()
            .id(longCount.incrementAndGet())
            .libelleAnneeAcademique(UUID.randomUUID().toString())
            .anneeAc(UUID.randomUUID().toString())
            .anneeCouranteYN(intCount.incrementAndGet());
    }
}
