package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FormationTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeFormationTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeTestSamples.*;
import static sn.ugb.gir.domain.InscriptionDoctoratTestSamples.*;
import static sn.ugb.gir.domain.PaiementFormationPriveeTestSamples.*;
import static sn.ugb.gir.domain.PaiementFraisTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    void inscriptionDoctoratsTest() throws Exception {
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation =
            getInscriptionAdministrativeFormationRandomSampleGenerator();
        InscriptionDoctorat inscriptionDoctoratBack = getInscriptionDoctoratRandomSampleGenerator();

        inscriptionAdministrativeFormation.addInscriptionDoctorats(inscriptionDoctoratBack);
        assertThat(inscriptionAdministrativeFormation.getInscriptionDoctorats()).containsOnly(inscriptionDoctoratBack);
        assertThat(inscriptionDoctoratBack.getInscriptionAdministrativeFormation()).isEqualTo(inscriptionAdministrativeFormation);

        inscriptionAdministrativeFormation.removeInscriptionDoctorats(inscriptionDoctoratBack);
        assertThat(inscriptionAdministrativeFormation.getInscriptionDoctorats()).doesNotContain(inscriptionDoctoratBack);
        assertThat(inscriptionDoctoratBack.getInscriptionAdministrativeFormation()).isNull();

        inscriptionAdministrativeFormation.inscriptionDoctorats(new HashSet<>(Set.of(inscriptionDoctoratBack)));
        assertThat(inscriptionAdministrativeFormation.getInscriptionDoctorats()).containsOnly(inscriptionDoctoratBack);
        assertThat(inscriptionDoctoratBack.getInscriptionAdministrativeFormation()).isEqualTo(inscriptionAdministrativeFormation);

        inscriptionAdministrativeFormation.setInscriptionDoctorats(new HashSet<>());
        assertThat(inscriptionAdministrativeFormation.getInscriptionDoctorats()).doesNotContain(inscriptionDoctoratBack);
        assertThat(inscriptionDoctoratBack.getInscriptionAdministrativeFormation()).isNull();
    }

    @Test
    void paiementFraisTest() throws Exception {
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation =
            getInscriptionAdministrativeFormationRandomSampleGenerator();
        PaiementFrais paiementFraisBack = getPaiementFraisRandomSampleGenerator();

        inscriptionAdministrativeFormation.addPaiementFrais(paiementFraisBack);
        assertThat(inscriptionAdministrativeFormation.getPaiementFrais()).containsOnly(paiementFraisBack);
        assertThat(paiementFraisBack.getInscriptionAdministrativeFormation()).isEqualTo(inscriptionAdministrativeFormation);

        inscriptionAdministrativeFormation.removePaiementFrais(paiementFraisBack);
        assertThat(inscriptionAdministrativeFormation.getPaiementFrais()).doesNotContain(paiementFraisBack);
        assertThat(paiementFraisBack.getInscriptionAdministrativeFormation()).isNull();

        inscriptionAdministrativeFormation.paiementFrais(new HashSet<>(Set.of(paiementFraisBack)));
        assertThat(inscriptionAdministrativeFormation.getPaiementFrais()).containsOnly(paiementFraisBack);
        assertThat(paiementFraisBack.getInscriptionAdministrativeFormation()).isEqualTo(inscriptionAdministrativeFormation);

        inscriptionAdministrativeFormation.setPaiementFrais(new HashSet<>());
        assertThat(inscriptionAdministrativeFormation.getPaiementFrais()).doesNotContain(paiementFraisBack);
        assertThat(paiementFraisBack.getInscriptionAdministrativeFormation()).isNull();
    }

    @Test
    void paiementFormationPriveesTest() throws Exception {
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation =
            getInscriptionAdministrativeFormationRandomSampleGenerator();
        PaiementFormationPrivee paiementFormationPriveeBack = getPaiementFormationPriveeRandomSampleGenerator();

        inscriptionAdministrativeFormation.addPaiementFormationPrivees(paiementFormationPriveeBack);
        assertThat(inscriptionAdministrativeFormation.getPaiementFormationPrivees()).containsOnly(paiementFormationPriveeBack);
        assertThat(paiementFormationPriveeBack.getInscriptionAdministrativeFormation()).isEqualTo(inscriptionAdministrativeFormation);

        inscriptionAdministrativeFormation.removePaiementFormationPrivees(paiementFormationPriveeBack);
        assertThat(inscriptionAdministrativeFormation.getPaiementFormationPrivees()).doesNotContain(paiementFormationPriveeBack);
        assertThat(paiementFormationPriveeBack.getInscriptionAdministrativeFormation()).isNull();

        inscriptionAdministrativeFormation.paiementFormationPrivees(new HashSet<>(Set.of(paiementFormationPriveeBack)));
        assertThat(inscriptionAdministrativeFormation.getPaiementFormationPrivees()).containsOnly(paiementFormationPriveeBack);
        assertThat(paiementFormationPriveeBack.getInscriptionAdministrativeFormation()).isEqualTo(inscriptionAdministrativeFormation);

        inscriptionAdministrativeFormation.setPaiementFormationPrivees(new HashSet<>());
        assertThat(inscriptionAdministrativeFormation.getPaiementFormationPrivees()).doesNotContain(paiementFormationPriveeBack);
        assertThat(paiementFormationPriveeBack.getInscriptionAdministrativeFormation()).isNull();
    }
}
