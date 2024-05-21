package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeBourseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeBourse getTypeBourseSample1() {
        return new TypeBourse().id(1L).libelle("libelle1");
    }

    public static TypeBourse getTypeBourseSample2() {
        return new TypeBourse().id(2L).libelle("libelle2");
    }

    public static TypeBourse getTypeBourseRandomSampleGenerator() {
        return new TypeBourse().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
