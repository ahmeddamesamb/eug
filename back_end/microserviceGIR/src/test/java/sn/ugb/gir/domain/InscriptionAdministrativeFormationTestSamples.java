package sn.ugb.gir.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InscriptionAdministrativeFormationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InscriptionAdministrativeFormation getInscriptionAdministrativeFormationSample1() {
        return new InscriptionAdministrativeFormation()
            .id(1L)
            .inscriptionPrincipaleYN(1)
            .inscriptionAnnuleeYN(1)
            .paiementFraisOblYN(1)
            .paiementFraisIntegergYN(1)
            .certificatDelivreYN(1);
    }

    public static InscriptionAdministrativeFormation getInscriptionAdministrativeFormationSample2() {
        return new InscriptionAdministrativeFormation()
            .id(2L)
            .inscriptionPrincipaleYN(2)
            .inscriptionAnnuleeYN(2)
            .paiementFraisOblYN(2)
            .paiementFraisIntegergYN(2)
            .certificatDelivreYN(2);
    }

    public static InscriptionAdministrativeFormation getInscriptionAdministrativeFormationRandomSampleGenerator() {
        return new InscriptionAdministrativeFormation()
            .id(longCount.incrementAndGet())
            .inscriptionPrincipaleYN(intCount.incrementAndGet())
            .inscriptionAnnuleeYN(intCount.incrementAndGet())
            .paiementFraisOblYN(intCount.incrementAndGet())
            .paiementFraisIntegergYN(intCount.incrementAndGet())
            .certificatDelivreYN(intCount.incrementAndGet());
    }
}
