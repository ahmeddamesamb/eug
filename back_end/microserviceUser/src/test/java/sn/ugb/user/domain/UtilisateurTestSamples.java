package sn.ugb.user.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UtilisateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Utilisateur getUtilisateurSample1() {
        return new Utilisateur()
            .id(1L)
            .nomUser("nomUser1")
            .prenomUser("prenomUser1")
            .emailUser("emailUser1")
            .motDePasse("motDePasse1")
            .role("role1")
            .matricule("matricule1")
            .departement("departement1")
            .statut("statut1");
    }

    public static Utilisateur getUtilisateurSample2() {
        return new Utilisateur()
            .id(2L)
            .nomUser("nomUser2")
            .prenomUser("prenomUser2")
            .emailUser("emailUser2")
            .motDePasse("motDePasse2")
            .role("role2")
            .matricule("matricule2")
            .departement("departement2")
            .statut("statut2");
    }

    public static Utilisateur getUtilisateurRandomSampleGenerator() {
        return new Utilisateur()
            .id(longCount.incrementAndGet())
            .nomUser(UUID.randomUUID().toString())
            .prenomUser(UUID.randomUUID().toString())
            .emailUser(UUID.randomUUID().toString())
            .motDePasse(UUID.randomUUID().toString())
            .role(UUID.randomUUID().toString())
            .matricule(UUID.randomUUID().toString())
            .departement(UUID.randomUUID().toString())
            .statut(UUID.randomUUID().toString());
    }
}
