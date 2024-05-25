package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;

import java.util.List;

public class InscriptionAdministrativeFormationGenerator {
    private static final Faker faker = new Faker();

    public static InscriptionAdministrativeFormation generateInscriptionAdministrativeFormation(List<Formation> formationList, List<InscriptionAdministrative> inscriptionAdministrativeList) {
        InscriptionAdministrativeFormation inscription = new InscriptionAdministrativeFormation();
        inscription.setInscriptionPrincipaleYN(faker.bool().bool() ? 1 : 0);
        inscription.setInscriptionAnnuleeYN(faker.bool().bool() ? 1 : 0);
        inscription.setPaiementFraisOblYN(faker.bool().bool() ? 1 : 0);
        inscription.setPaiementFraisIntegergYN(faker.bool().bool() ? 1 : 0);
        inscription.setCertificatDelivreYN(faker.bool().bool() ? 1 : 0);
        inscription.setFormation(formationList.get(faker.random().nextInt(formationList.size())));
        inscription.setInscriptionAdministrative(inscriptionAdministrativeList.get(faker.random().nextInt(inscriptionAdministrativeList.size())));

        return inscription;
    }
}
