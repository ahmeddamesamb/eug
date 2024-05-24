package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FormationTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeFormationTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InscriptionAdministrativeFormationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionAdministrativeFormation.class);
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation1 = getInscriptionAdministrativeFormationSample1();
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation2 = new InscriptionAdministrativeFormation();
        assertThat(inscriptionAdministrativeFormation1).isNotEqualTo(inscriptionAdministrativeFormation2);

        inscriptionAdministrativeFormation2.setId(inscriptionAdministrativeFormation1.getId());
        assertThat(inscriptionAdministrativeFormation1).isEqualTo(inscriptionAdministrativeFormation2);

        inscriptionAdministrativeFormation2 = getInscriptionAdministrativeFormationSample2();
        assertThat(inscriptionAdministrativeFormation1).isNotEqualTo(inscriptionAdministrativeFormation2);
    }

    @Test
    void inscriptionAdministrativeTest() throws Exception {
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation =
            getInscriptionAdministrativeFormationRandomSampleGenerator();
        InscriptionAdministrative inscriptionAdministrativeBack = getInscriptionAdministrativeRandomSampleGenerator();

        inscriptionAdministrativeFormation.setInscriptionAdministrative(inscriptionAdministrativeBack);
        assertThat(inscriptionAdministrativeFormation.getInscriptionAdministrative()).isEqualTo(inscriptionAdministrativeBack);

        inscriptionAdministrativeFormation.inscriptionAdministrative(null);
        assertThat(inscriptionAdministrativeFormation.getInscriptionAdministrative()).isNull();
    }

    @Test
    void formationTest() throws Exception {
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation =
            getInscriptionAdministrativeFormationRandomSampleGenerator();
        Formation formationBack = getFormationRandomSampleGenerator();

        inscriptionAdministrativeFormation.setFormation(formationBack);
        assertThat(inscriptionAdministrativeFormation.getFormation()).isEqualTo(formationBack);

        inscriptionAdministrativeFormation.formation(null);
        assertThat(inscriptionAdministrativeFormation.getFormation()).isNull();
    }
}
