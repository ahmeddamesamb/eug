package sn.ugb.grh.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EnseignantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Enseignant getEnseignantSample1() {
        return new Enseignant()
            .id(1L)
            .titreCoEncadreur("titreCoEncadreur1")
            .nomEnseignant("nomEnseignant1")
            .prenomEnseignant("prenomEnseignant1")
            .emailEnseignant("emailEnseignant1")
            .telephoneEnseignant("telephoneEnseignant1")
            .titresId(1)
            .adresse("adresse1")
            .numeroPoste(1);
    }

    public static Enseignant getEnseignantSample2() {
        return new Enseignant()
            .id(2L)
            .titreCoEncadreur("titreCoEncadreur2")
            .nomEnseignant("nomEnseignant2")
            .prenomEnseignant("prenomEnseignant2")
            .emailEnseignant("emailEnseignant2")
            .telephoneEnseignant("telephoneEnseignant2")
            .titresId(2)
            .adresse("adresse2")
            .numeroPoste(2);
    }

    public static Enseignant getEnseignantRandomSampleGenerator() {
        return new Enseignant()
            .id(longCount.incrementAndGet())
            .titreCoEncadreur(UUID.randomUUID().toString())
            .nomEnseignant(UUID.randomUUID().toString())
            .prenomEnseignant(UUID.randomUUID().toString())
            .emailEnseignant(UUID.randomUUID().toString())
            .telephoneEnseignant(UUID.randomUUID().toString())
            .titresId(intCount.incrementAndGet())
            .adresse(UUID.randomUUID().toString())
            .numeroPoste(intCount.incrementAndGet());
    }
}
