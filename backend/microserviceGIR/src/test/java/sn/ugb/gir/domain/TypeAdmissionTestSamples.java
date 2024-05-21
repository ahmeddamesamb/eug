package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeAdmissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeAdmission getTypeAdmissionSample1() {
        return new TypeAdmission().id(1L).libelle("libelle1");
    }

    public static TypeAdmission getTypeAdmissionSample2() {
        return new TypeAdmission().id(2L).libelle("libelle2");
    }

    public static TypeAdmission getTypeAdmissionRandomSampleGenerator() {
        return new TypeAdmission().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
