package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProgrammationInscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProgrammationInscription getProgrammationInscriptionSample1() {
        return new ProgrammationInscription().id(1L).libelleProgrammation("libelleProgrammation1").emailUser("emailUser1");
    }

    public static ProgrammationInscription getProgrammationInscriptionSample2() {
        return new ProgrammationInscription().id(2L).libelleProgrammation("libelleProgrammation2").emailUser("emailUser2");
    }

    public static ProgrammationInscription getProgrammationInscriptionRandomSampleGenerator() {
        return new ProgrammationInscription()
            .id(longCount.incrementAndGet())
            .libelleProgrammation(UUID.randomUUID().toString())
            .emailUser(UUID.randomUUID().toString());
    }
}
