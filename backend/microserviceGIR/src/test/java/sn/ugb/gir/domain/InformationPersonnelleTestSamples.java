package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InformationPersonnelleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InformationPersonnelle getInformationPersonnelleSample1() {
        return new InformationPersonnelle()
            .id(1L)
            .nomEtu("nomEtu1")
            .nomJeuneFilleEtu("nomJeuneFilleEtu1")
            .prenomEtu("prenomEtu1")
            .statutMarital("statutMarital1")
            .regime(1)
            .profession("profession1")
            .adresseEtu("adresseEtu1")
            .telEtu("telEtu1")
            .emailEtu("emailEtu1")
            .adresseParent("adresseParent1")
            .telParent("telParent1")
            .emailParent("emailParent1")
            .nomParent("nomParent1")
            .prenomParent("prenomParent1")
            .photo("photo1")
            .emailUser("emailUser1");
    }

    public static InformationPersonnelle getInformationPersonnelleSample2() {
        return new InformationPersonnelle()
            .id(2L)
            .nomEtu("nomEtu2")
            .nomJeuneFilleEtu("nomJeuneFilleEtu2")
            .prenomEtu("prenomEtu2")
            .statutMarital("statutMarital2")
            .regime(2)
            .profession("profession2")
            .adresseEtu("adresseEtu2")
            .telEtu("telEtu2")
            .emailEtu("emailEtu2")
            .adresseParent("adresseParent2")
            .telParent("telParent2")
            .emailParent("emailParent2")
            .nomParent("nomParent2")
            .prenomParent("prenomParent2")
            .photo("photo2")
            .emailUser("emailUser2");
    }

    public static InformationPersonnelle getInformationPersonnelleRandomSampleGenerator() {
        return new InformationPersonnelle()
            .id(longCount.incrementAndGet())
            .nomEtu(UUID.randomUUID().toString())
            .nomJeuneFilleEtu(UUID.randomUUID().toString())
            .prenomEtu(UUID.randomUUID().toString())
            .statutMarital(UUID.randomUUID().toString())
            .regime(intCount.incrementAndGet())
            .profession(UUID.randomUUID().toString())
            .adresseEtu(UUID.randomUUID().toString())
            .telEtu(UUID.randomUUID().toString())
            .emailEtu(UUID.randomUUID().toString())
            .adresseParent(UUID.randomUUID().toString())
            .telParent(UUID.randomUUID().toString())
            .emailParent(UUID.randomUUID().toString())
            .nomParent(UUID.randomUUID().toString())
            .prenomParent(UUID.randomUUID().toString())
            .photo(UUID.randomUUID().toString())
            .emailUser(UUID.randomUUID().toString());
    }
}
