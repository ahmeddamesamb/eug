package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EtudiantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Etudiant getEtudiantSample1() {
        return new Etudiant()
            .id(1L)
            .codeEtu("codeEtu1")
            .ine("ine1")
            .codeBU("codeBU1")
            .emailUGB("emailUGB1")
            .lieuNaissEtu("lieuNaissEtu1")
            .sexe("sexe1")
            .numDocIdentite("numDocIdentite1");
    }

    public static Etudiant getEtudiantSample2() {
        return new Etudiant()
            .id(2L)
            .codeEtu("codeEtu2")
            .ine("ine2")
            .codeBU("codeBU2")
            .emailUGB("emailUGB2")
            .lieuNaissEtu("lieuNaissEtu2")
            .sexe("sexe2")
            .numDocIdentite("numDocIdentite2");
    }

    public static Etudiant getEtudiantRandomSampleGenerator() {
        return new Etudiant()
            .id(longCount.incrementAndGet())
            .codeEtu(UUID.randomUUID().toString())
            .ine(UUID.randomUUID().toString())
            .codeBU(UUID.randomUUID().toString())
            .emailUGB(UUID.randomUUID().toString())
            .lieuNaissEtu(UUID.randomUUID().toString())
            .sexe(UUID.randomUUID().toString())
            .numDocIdentite(UUID.randomUUID().toString());
    }
}
