package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.*;

import java.util.List;

public class InscriptionAdministrativeGenerator {
    public static Faker getFaker() {
        return faker;
    }

    static final Faker faker = new Faker();

    public static InscriptionAdministrative generateInscriptionAdministrative(List<Etudiant> etudiant, List<AnneeAcademique> anneeAcademique, List<TypeAdmission> typeAdmission) {
        InscriptionAdministrative inscription = new InscriptionAdministrative();
        inscription.setNouveauInscritYN(faker.bool().bool() ? 1 : 0);
        inscription.setRepriseYN(faker.bool().bool() ? 1 : 0);
        inscription.setAutoriseYN(faker.bool().bool() ? 1 : 0);
        inscription.setOrdreInscription(faker.number().numberBetween(1, 100));
        inscription.setEtudiant(etudiant.get(faker.random().nextInt(etudiant.size())));
        inscription.setAnneeAcademique(anneeAcademique.get(faker.random().nextInt(anneeAcademique.size())));
        inscription.setTypeAdmission(typeAdmission.get(faker.random().nextInt(typeAdmission.size())));

        return inscription;
    }
}
