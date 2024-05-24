package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProgrammationInscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProgrammationInscription getProgrammationInscriptionSample1() {
        return new ProgrammationInscription().id(1L).libelleProgrammation("libelleProgrammation1").ouvertYN(1).emailUser("emailUser1");
    }

    public static ProgrammationInscription getProgrammationInscriptionSample2() {
        return new ProgrammationInscription().id(2L).libelleProgrammation("libelleProgrammation2").ouvertYN(2).emailUser("emailUser2");
    }

    public static ProgrammationInscription getProgrammationInscriptionRandomSampleGenerator() {
        return new ProgrammationInscription()
            .id(longCount.incrementAndGet())
            .libelleProgrammation(UUID.randomUUID().toString())
            .ouvertYN(intCount.incrementAndGet())
            .emailUser(UUID.randomUUID().toString());
    }
}
