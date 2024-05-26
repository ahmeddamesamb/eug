package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DoctoratTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeFormationTestSamples.*;
import static sn.ugb.gir.domain.InscriptionDoctoratTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InscriptionDoctoratTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionDoctorat.class);
        InscriptionDoctorat inscriptionDoctorat1 = getInscriptionDoctoratSample1();
        InscriptionDoctorat inscriptionDoctorat2 = new InscriptionDoctorat();
        assertThat(inscriptionDoctorat1).isNotEqualTo(inscriptionDoctorat2);

        inscriptionDoctorat2.setId(inscriptionDoctorat1.getId());
        assertThat(inscriptionDoctorat1).isEqualTo(inscriptionDoctorat2);

        inscriptionDoctorat2 = getInscriptionDoctoratSample2();
        assertThat(inscriptionDoctorat1).isNotEqualTo(inscriptionDoctorat2);
    }

    @Test
    void doctoratTest() throws Exception {
        InscriptionDoctorat inscriptionDoctorat = getInscriptionDoctoratRandomSampleGenerator();
        Doctorat doctoratBack = getDoctoratRandomSampleGenerator();

        inscriptionDoctorat.setDoctorat(doctoratBack);
        assertThat(inscriptionDoctorat.getDoctorat()).isEqualTo(doctoratBack);

        inscriptionDoctorat.doctorat(null);
        assertThat(inscriptionDoctorat.getDoctorat()).isNull();
    }

    @Test
    void inscriptionAdministrativeFormationTest() throws Exception {
        InscriptionDoctorat inscriptionDoctorat = getInscriptionDoctoratRandomSampleGenerator();
        InscriptionAdministrativeFormation inscriptionAdministrativeFormationBack =
            getInscriptionAdministrativeFormationRandomSampleGenerator();

        inscriptionDoctorat.setInscriptionAdministrativeFormation(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionDoctorat.getInscriptionAdministrativeFormation()).isEqualTo(inscriptionAdministrativeFormationBack);

        inscriptionDoctorat.inscriptionAdministrativeFormation(null);
        assertThat(inscriptionDoctorat.getInscriptionAdministrativeFormation()).isNull();
    }
}
