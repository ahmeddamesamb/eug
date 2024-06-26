package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeOperationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeOperation getTypeOperationSample1() {
        return new TypeOperation().id(1L).libelleTypeOperation("libelleTypeOperation1");
    }

    public static TypeOperation getTypeOperationSample2() {
        return new TypeOperation().id(2L).libelleTypeOperation("libelleTypeOperation2");
    }

    public static TypeOperation getTypeOperationRandomSampleGenerator() {
        return new TypeOperation().id(longCount.incrementAndGet()).libelleTypeOperation(UUID.randomUUID().toString());
    }
}
