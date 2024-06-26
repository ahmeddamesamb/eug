package sn.ugb.gateway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BlocFonctionnelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BlocFonctionnel getBlocFonctionnelSample1() {
        return new BlocFonctionnel().id(1L).libelleBloc("libelleBloc1");
    }

    public static BlocFonctionnel getBlocFonctionnelSample2() {
        return new BlocFonctionnel().id(2L).libelleBloc("libelleBloc2");
    }

    public static BlocFonctionnel getBlocFonctionnelRandomSampleGenerator() {
        return new BlocFonctionnel().id(longCount.incrementAndGet()).libelleBloc(UUID.randomUUID().toString());
    }
}
