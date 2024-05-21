package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FraisTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeFormationTestSamples.*;
import static sn.ugb.gir.domain.OperateurTestSamples.*;
import static sn.ugb.gir.domain.PaiementFraisTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class PaiementFraisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementFrais.class);
        PaiementFrais paiementFrais1 = getPaiementFraisSample1();
        PaiementFrais paiementFrais2 = new PaiementFrais();
        assertThat(paiementFrais1).isNotEqualTo(paiementFrais2);

        paiementFrais2.setId(paiementFrais1.getId());
        assertThat(paiementFrais1).isEqualTo(paiementFrais2);

        paiementFrais2 = getPaiementFraisSample2();
        assertThat(paiementFrais1).isNotEqualTo(paiementFrais2);
    }

    @Test
    void fraisTest() throws Exception {
        PaiementFrais paiementFrais = getPaiementFraisRandomSampleGenerator();
        Frais fraisBack = getFraisRandomSampleGenerator();

        paiementFrais.setFrais(fraisBack);
        assertThat(paiementFrais.getFrais()).isEqualTo(fraisBack);

        paiementFrais.frais(null);
        assertThat(paiementFrais.getFrais()).isNull();
    }

    @Test
    void operateurTest() throws Exception {
        PaiementFrais paiementFrais = getPaiementFraisRandomSampleGenerator();
        Operateur operateurBack = getOperateurRandomSampleGenerator();

        paiementFrais.setOperateur(operateurBack);
        assertThat(paiementFrais.getOperateur()).isEqualTo(operateurBack);

        paiementFrais.operateur(null);
        assertThat(paiementFrais.getOperateur()).isNull();
    }

    @Test
    void inscriptionAdministrativeFormationTest() throws Exception {
        PaiementFrais paiementFrais = getPaiementFraisRandomSampleGenerator();
        InscriptionAdministrativeFormation inscriptionAdministrativeFormationBack =
            getInscriptionAdministrativeFormationRandomSampleGenerator();

        paiementFrais.setInscriptionAdministrativeFormation(inscriptionAdministrativeFormationBack);
        assertThat(paiementFrais.getInscriptionAdministrativeFormation()).isEqualTo(inscriptionAdministrativeFormationBack);

        paiementFrais.inscriptionAdministrativeFormation(null);
        assertThat(paiementFrais.getInscriptionAdministrativeFormation()).isNull();
    }
}
