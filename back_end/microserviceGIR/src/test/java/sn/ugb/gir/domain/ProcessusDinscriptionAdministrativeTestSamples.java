package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessusDinscriptionAdministrativeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProcessusDinscriptionAdministrative getProcessusDinscriptionAdministrativeSample1() {
        return new ProcessusDinscriptionAdministrative()
            .id(1L)
            .inscriptionDemarreeYN(1)
            .inscriptionAnnuleeYN(1)
            .apteYN(1)
            .beneficiaireCROUSYN(1)
            .nouveauCodeBUYN(1)
            .quitusBUYN(1)
            .exporterBUYN(1)
            .fraisObligatoiresPayesYN(1)
            .numeroQuitusCROUS(1)
            .carteEturemiseYN(1)
            .carteDupremiseYN(1)
            .dateRemiseCarteDup(1)
            .inscritAdministrativementYN(1)
            .inscritOnlineYN(1)
            .emailUser("emailUser1");
    }

    public static ProcessusDinscriptionAdministrative getProcessusDinscriptionAdministrativeSample2() {
        return new ProcessusDinscriptionAdministrative()
            .id(2L)
            .inscriptionDemarreeYN(2)
            .inscriptionAnnuleeYN(2)
            .apteYN(2)
            .beneficiaireCROUSYN(2)
            .nouveauCodeBUYN(2)
            .quitusBUYN(2)
            .exporterBUYN(2)
            .fraisObligatoiresPayesYN(2)
            .numeroQuitusCROUS(2)
            .carteEturemiseYN(2)
            .carteDupremiseYN(2)
            .dateRemiseCarteDup(2)
            .inscritAdministrativementYN(2)
            .inscritOnlineYN(2)
            .emailUser("emailUser2");
    }

    public static ProcessusDinscriptionAdministrative getProcessusDinscriptionAdministrativeRandomSampleGenerator() {
        return new ProcessusDinscriptionAdministrative()
            .id(longCount.incrementAndGet())
            .inscriptionDemarreeYN(intCount.incrementAndGet())
            .inscriptionAnnuleeYN(intCount.incrementAndGet())
            .apteYN(intCount.incrementAndGet())
            .beneficiaireCROUSYN(intCount.incrementAndGet())
            .nouveauCodeBUYN(intCount.incrementAndGet())
            .quitusBUYN(intCount.incrementAndGet())
            .exporterBUYN(intCount.incrementAndGet())
            .fraisObligatoiresPayesYN(intCount.incrementAndGet())
            .numeroQuitusCROUS(intCount.incrementAndGet())
            .carteEturemiseYN(intCount.incrementAndGet())
            .carteDupremiseYN(intCount.incrementAndGet())
            .dateRemiseCarteDup(intCount.incrementAndGet())
            .inscritAdministrativementYN(intCount.incrementAndGet())
            .inscritOnlineYN(intCount.incrementAndGet())
            .emailUser(UUID.randomUUID().toString());
    }
}
