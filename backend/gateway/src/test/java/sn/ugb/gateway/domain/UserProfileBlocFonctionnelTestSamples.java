package sn.ugb.gateway.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileBlocFonctionnelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserProfileBlocFonctionnel getUserProfileBlocFonctionnelSample1() {
        return new UserProfileBlocFonctionnel().id(1L);
    }

    public static UserProfileBlocFonctionnel getUserProfileBlocFonctionnelSample2() {
        return new UserProfileBlocFonctionnel().id(2L);
    }

    public static UserProfileBlocFonctionnel getUserProfileBlocFonctionnelRandomSampleGenerator() {
        return new UserProfileBlocFonctionnel().id(longCount.incrementAndGet());
    }
}
