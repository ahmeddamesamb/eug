package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.AnneeAcademiqueTestSamples.*;
import static sn.ugb.gir.domain.FormationInvalideTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeTestSamples.*;
import static sn.ugb.gir.domain.ProgrammationInscriptionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class AnneeAcademiqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnneeAcademique.class);
        AnneeAcademique anneeAcademique1 = getAnneeAcademiqueSample1();
        AnneeAcademique anneeAcademique2 = new AnneeAcademique();
        assertThat(anneeAcademique1).isNotEqualTo(anneeAcademique2);

        anneeAcademique2.setId(anneeAcademique1.getId());
        assertThat(anneeAcademique1).isEqualTo(anneeAcademique2);

        anneeAcademique2 = getAnneeAcademiqueSample2();
        assertThat(anneeAcademique1).isNotEqualTo(anneeAcademique2);
    }

    @Test
    void inscriptionAdministrativesTest() throws Exception {
        AnneeAcademique anneeAcademique = getAnneeAcademiqueRandomSampleGenerator();
        InscriptionAdministrative inscriptionAdministrativeBack = getInscriptionAdministrativeRandomSampleGenerator();

        anneeAcademique.addInscriptionAdministratives(inscriptionAdministrativeBack);
        assertThat(anneeAcademique.getInscriptionAdministratives()).containsOnly(inscriptionAdministrativeBack);
        assertThat(inscriptionAdministrativeBack.getAnneeAcademique()).isEqualTo(anneeAcademique);

        anneeAcademique.removeInscriptionAdministratives(inscriptionAdministrativeBack);
        assertThat(anneeAcademique.getInscriptionAdministratives()).doesNotContain(inscriptionAdministrativeBack);
        assertThat(inscriptionAdministrativeBack.getAnneeAcademique()).isNull();

        anneeAcademique.inscriptionAdministratives(new HashSet<>(Set.of(inscriptionAdministrativeBack)));
        assertThat(anneeAcademique.getInscriptionAdministratives()).containsOnly(inscriptionAdministrativeBack);
        assertThat(inscriptionAdministrativeBack.getAnneeAcademique()).isEqualTo(anneeAcademique);

        anneeAcademique.setInscriptionAdministratives(new HashSet<>());
        assertThat(anneeAcademique.getInscriptionAdministratives()).doesNotContain(inscriptionAdministrativeBack);
        assertThat(inscriptionAdministrativeBack.getAnneeAcademique()).isNull();
    }

    @Test
    void formationInvalidesTest() throws Exception {
        AnneeAcademique anneeAcademique = getAnneeAcademiqueRandomSampleGenerator();
        FormationInvalide formationInvalideBack = getFormationInvalideRandomSampleGenerator();

        anneeAcademique.addFormationInvalides(formationInvalideBack);
        assertThat(anneeAcademique.getFormationInvalides()).containsOnly(formationInvalideBack);
        assertThat(formationInvalideBack.getAnneeAcademique()).isEqualTo(anneeAcademique);

        anneeAcademique.removeFormationInvalides(formationInvalideBack);
        assertThat(anneeAcademique.getFormationInvalides()).doesNotContain(formationInvalideBack);
        assertThat(formationInvalideBack.getAnneeAcademique()).isNull();

        anneeAcademique.formationInvalides(new HashSet<>(Set.of(formationInvalideBack)));
        assertThat(anneeAcademique.getFormationInvalides()).containsOnly(formationInvalideBack);
        assertThat(formationInvalideBack.getAnneeAcademique()).isEqualTo(anneeAcademique);

        anneeAcademique.setFormationInvalides(new HashSet<>());
        assertThat(anneeAcademique.getFormationInvalides()).doesNotContain(formationInvalideBack);
        assertThat(formationInvalideBack.getAnneeAcademique()).isNull();
    }

    @Test
    void programmationInscriptionsTest() throws Exception {
        AnneeAcademique anneeAcademique = getAnneeAcademiqueRandomSampleGenerator();
        ProgrammationInscription programmationInscriptionBack = getProgrammationInscriptionRandomSampleGenerator();

        anneeAcademique.addProgrammationInscriptions(programmationInscriptionBack);
        assertThat(anneeAcademique.getProgrammationInscriptions()).containsOnly(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getAnneeAcademique()).isEqualTo(anneeAcademique);

        anneeAcademique.removeProgrammationInscriptions(programmationInscriptionBack);
        assertThat(anneeAcademique.getProgrammationInscriptions()).doesNotContain(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getAnneeAcademique()).isNull();

        anneeAcademique.programmationInscriptions(new HashSet<>(Set.of(programmationInscriptionBack)));
        assertThat(anneeAcademique.getProgrammationInscriptions()).containsOnly(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getAnneeAcademique()).isEqualTo(anneeAcademique);

        anneeAcademique.setProgrammationInscriptions(new HashSet<>());
        assertThat(anneeAcademique.getProgrammationInscriptions()).doesNotContain(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getAnneeAcademique()).isNull();
    }
}
