package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.InscriptionAdministrativeFormationTestSamples.*;
import static sn.ugb.gir.domain.OperateurTestSamples.*;
import static sn.ugb.gir.domain.PaiementFormationPriveeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class PaiementFormationPriveeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementFormationPrivee.class);
        PaiementFormationPrivee paiementFormationPrivee1 = getPaiementFormationPriveeSample1();
        PaiementFormationPrivee paiementFormationPrivee2 = new PaiementFormationPrivee();
        assertThat(paiementFormationPrivee1).isNotEqualTo(paiementFormationPrivee2);

        paiementFormationPrivee2.setId(paiementFormationPrivee1.getId());
        assertThat(paiementFormationPrivee1).isEqualTo(paiementFormationPrivee2);

        paiementFormationPrivee2 = getPaiementFormationPriveeSample2();
        assertThat(paiementFormationPrivee1).isNotEqualTo(paiementFormationPrivee2);
    }

    @Test
    void inscriptionAdministrativeFormationTest() throws Exception {
        PaiementFormationPrivee paiementFormationPrivee = getPaiementFormationPriveeRandomSampleGenerator();
        InscriptionAdministrativeFormation inscriptionAdministrativeFormationBack =
            getInscriptionAdministrativeFormationRandomSampleGenerator();

        paiementFormationPrivee.setInscriptionAdministrativeFormation(inscriptionAdministrativeFormationBack);
        assertThat(paiementFormationPrivee.getInscriptionAdministrativeFormation()).isEqualTo(inscriptionAdministrativeFormationBack);

        paiementFormationPrivee.inscriptionAdministrativeFormation(null);
        assertThat(paiementFormationPrivee.getInscriptionAdministrativeFormation()).isNull();
    }

    @Test
    void operateurTest() throws Exception {
        PaiementFormationPrivee paiementFormationPrivee = getPaiementFormationPriveeRandomSampleGenerator();
        Operateur operateurBack = getOperateurRandomSampleGenerator();

        paiementFormationPrivee.setOperateur(operateurBack);
        assertThat(paiementFormationPrivee.getOperateur()).isEqualTo(operateurBack);

        paiementFormationPrivee.operateur(null);
        assertThat(paiementFormationPrivee.getOperateur()).isNull();
    }
}
