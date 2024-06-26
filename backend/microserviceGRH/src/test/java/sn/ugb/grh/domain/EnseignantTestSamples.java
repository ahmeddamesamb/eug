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
            .nom("nom1")
            .prenom("prenom1")
            .email("email1")
            .telephone("telephone1")
            .titresId(1L)
            .adresse("adresse1")
            .numeroPoste(1)
            .photo("photo1");
    }

    public static Enseignant getEnseignantSample2() {
        return new Enseignant()
            .id(2L)
            .titreCoEncadreur("titreCoEncadreur2")
            .nom("nom2")
            .prenom("prenom2")
            .email("email2")
            .telephone("telephone2")
            .titresId(2L)
            .adresse("adresse2")
            .numeroPoste(2)
            .photo("photo2");
    }

    public static Enseignant getEnseignantRandomSampleGenerator() {
        return new Enseignant()
            .id(longCount.incrementAndGet())
            .titreCoEncadreur(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .titresId(longCount.incrementAndGet())
            .adresse(UUID.randomUUID().toString())
            .numeroPoste(intCount.incrementAndGet())
            .photo(UUID.randomUUID().toString());
    }
}
