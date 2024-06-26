package sn.ugb.gateway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceUser getServiceUserSample1() {
        return new ServiceUser().id(1L).nom("nom1");
    }

    public static ServiceUser getServiceUserSample2() {
        return new ServiceUser().id(2L).nom("nom2");
    }

    public static ServiceUser getServiceUserRandomSampleGenerator() {
        return new ServiceUser().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString());
    }
}
