package sn.ugb.gateway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoriqueConnexionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistoriqueConnexion getHistoriqueConnexionSample1() {
        return new HistoriqueConnexion().id(1L).adresseIp("adresseIp1");
    }

    public static HistoriqueConnexion getHistoriqueConnexionSample2() {
        return new HistoriqueConnexion().id(2L).adresseIp("adresseIp2");
    }

    public static HistoriqueConnexion getHistoriqueConnexionRandomSampleGenerator() {
        return new HistoriqueConnexion().id(longCount.incrementAndGet()).adresseIp(UUID.randomUUID().toString());
    }
}
