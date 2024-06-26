package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.AnneeAcademiqueTestSamples.*;
import static sn.ugb.gir.domain.EtudiantTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeFormationTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeTestSamples.*;
import static sn.ugb.gir.domain.ProcessusInscriptionAdministrativeTestSamples.*;
import static sn.ugb.gir.domain.TypeAdmissionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InscriptionAdministrativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionAdministrative.class);
        InscriptionAdministrative inscriptionAdministrative1 = getInscriptionAdministrativeSample1();
        InscriptionAdministrative inscriptionAdministrative2 = new InscriptionAdministrative();
        assertThat(inscriptionAdministrative1).isNotEqualTo(inscriptionAdministrative2);

        inscriptionAdministrative2.setId(inscriptionAdministrative1.getId());
        assertThat(inscriptionAdministrative1).isEqualTo(inscriptionAdministrative2);

        inscriptionAdministrative2 = getInscriptionAdministrativeSample2();
        assertThat(inscriptionAdministrative1).isNotEqualTo(inscriptionAdministrative2);
    }

    @Test
    void typeAdmissionTest() throws Exception {
        InscriptionAdministrative inscriptionAdministrative = getInscriptionAdministrativeRandomSampleGenerator();
        TypeAdmission typeAdmissionBack = getTypeAdmissionRandomSampleGenerator();

        inscriptionAdministrative.setTypeAdmission(typeAdmissionBack);
        assertThat(inscriptionAdministrative.getTypeAdmission()).isEqualTo(typeAdmissionBack);

        inscriptionAdministrative.typeAdmission(null);
        assertThat(inscriptionAdministrative.getTypeAdmission()).isNull();
    }

    @Test
    void anneeAcademiqueTest() throws Exception {
        InscriptionAdministrative inscriptionAdministrative = getInscriptionAdministrativeRandomSampleGenerator();
        AnneeAcademique anneeAcademiqueBack = getAnneeAcademiqueRandomSampleGenerator();

        inscriptionAdministrative.setAnneeAcademique(anneeAcademiqueBack);
        assertThat(inscriptionAdministrative.getAnneeAcademique()).isEqualTo(anneeAcademiqueBack);

        inscriptionAdministrative.anneeAcademique(null);
        assertThat(inscriptionAdministrative.getAnneeAcademique()).isNull();
    }

    @Test
    void etudiantTest() throws Exception {
        InscriptionAdministrative inscriptionAdministrative = getInscriptionAdministrativeRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        inscriptionAdministrative.setEtudiant(etudiantBack);
        assertThat(inscriptionAdministrative.getEtudiant()).isEqualTo(etudiantBack);

        inscriptionAdministrative.etudiant(null);
        assertThat(inscriptionAdministrative.getEtudiant()).isNull();
    }

    @Test
    void processusDinscriptionAdministrativeTest() throws Exception {
        InscriptionAdministrative inscriptionAdministrative = getInscriptionAdministrativeRandomSampleGenerator();
        ProcessusInscriptionAdministrative processusInscriptionAdministrativeBack =
            getProcessusInscriptionAdministrativeRandomSampleGenerator();

        inscriptionAdministrative.setProcessusDinscriptionAdministrative(processusInscriptionAdministrativeBack);
        assertThat(inscriptionAdministrative.getProcessusDinscriptionAdministrative()).isEqualTo(processusInscriptionAdministrativeBack);
        assertThat(processusInscriptionAdministrativeBack.getInscriptionAdministrative()).isEqualTo(inscriptionAdministrative);

        inscriptionAdministrative.processusDinscriptionAdministrative(null);
        assertThat(inscriptionAdministrative.getProcessusDinscriptionAdministrative()).isNull();
        assertThat(processusInscriptionAdministrativeBack.getInscriptionAdministrative()).isNull();
    }

    @Test
    void inscriptionAdministrativeFormationsTest() throws Exception {
        InscriptionAdministrative inscriptionAdministrative = getInscriptionAdministrativeRandomSampleGenerator();
        InscriptionAdministrativeFormation inscriptionAdministrativeFormationBack =
            getInscriptionAdministrativeFormationRandomSampleGenerator();

        inscriptionAdministrative.addInscriptionAdministrativeFormations(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrative.getInscriptionAdministrativeFormations()).containsOnly(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrativeFormationBack.getInscriptionAdministrative()).isEqualTo(inscriptionAdministrative);

        inscriptionAdministrative.removeInscriptionAdministrativeFormations(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrative.getInscriptionAdministrativeFormations())
            .doesNotContain(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrativeFormationBack.getInscriptionAdministrative()).isNull();

        inscriptionAdministrative.inscriptionAdministrativeFormations(new HashSet<>(Set.of(inscriptionAdministrativeFormationBack)));
        assertThat(inscriptionAdministrative.getInscriptionAdministrativeFormations()).containsOnly(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrativeFormationBack.getInscriptionAdministrative()).isEqualTo(inscriptionAdministrative);

        inscriptionAdministrative.setInscriptionAdministrativeFormations(new HashSet<>());
        assertThat(inscriptionAdministrative.getInscriptionAdministrativeFormations())
            .doesNotContain(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrativeFormationBack.getInscriptionAdministrative()).isNull();
    }
}
