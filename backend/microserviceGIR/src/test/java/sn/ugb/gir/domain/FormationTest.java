package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DepartementTestSamples.*;
import static sn.ugb.gir.domain.FormationAutoriseeTestSamples.*;
import static sn.ugb.gir.domain.FormationInvalideTestSamples.*;
import static sn.ugb.gir.domain.FormationPriveeTestSamples.*;
import static sn.ugb.gir.domain.FormationTestSamples.*;
import static sn.ugb.gir.domain.InscriptionAdministrativeFormationTestSamples.*;
import static sn.ugb.gir.domain.NiveauTestSamples.*;
import static sn.ugb.gir.domain.ProgrammationInscriptionTestSamples.*;
import static sn.ugb.gir.domain.SpecialiteTestSamples.*;
import static sn.ugb.gir.domain.TypeFormationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FormationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Formation.class);
        Formation formation1 = getFormationSample1();
        Formation formation2 = new Formation();
        assertThat(formation1).isNotEqualTo(formation2);

        formation2.setId(formation1.getId());
        assertThat(formation1).isEqualTo(formation2);

        formation2 = getFormationSample2();
        assertThat(formation1).isNotEqualTo(formation2);
    }

    @Test
    void typeFormationTest() throws Exception {
        Formation formation = getFormationRandomSampleGenerator();
        TypeFormation typeFormationBack = getTypeFormationRandomSampleGenerator();

        formation.setTypeFormation(typeFormationBack);
        assertThat(formation.getTypeFormation()).isEqualTo(typeFormationBack);

        formation.typeFormation(null);
        assertThat(formation.getTypeFormation()).isNull();
    }

    @Test
    void niveauTest() throws Exception {
        Formation formation = getFormationRandomSampleGenerator();
        Niveau niveauBack = getNiveauRandomSampleGenerator();

        formation.setNiveau(niveauBack);
        assertThat(formation.getNiveau()).isEqualTo(niveauBack);

        formation.niveau(null);
        assertThat(formation.getNiveau()).isNull();
    }

    @Test
    void specialiteTest() throws Exception {
        Formation formation = getFormationRandomSampleGenerator();
        Specialite specialiteBack = getSpecialiteRandomSampleGenerator();

        formation.setSpecialite(specialiteBack);
        assertThat(formation.getSpecialite()).isEqualTo(specialiteBack);

        formation.specialite(null);
        assertThat(formation.getSpecialite()).isNull();
    }

    @Test
    void departementTest() throws Exception {
        Formation formation = getFormationRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        formation.setDepartement(departementBack);
        assertThat(formation.getDepartement()).isEqualTo(departementBack);

        formation.departement(null);
        assertThat(formation.getDepartement()).isNull();
    }

    @Test
    void formationPriveeTest() throws Exception {
        Formation formation = getFormationRandomSampleGenerator();
        FormationPrivee formationPriveeBack = getFormationPriveeRandomSampleGenerator();

        formation.setFormationPrivee(formationPriveeBack);
        assertThat(formation.getFormationPrivee()).isEqualTo(formationPriveeBack);
        assertThat(formationPriveeBack.getFormation()).isEqualTo(formation);

        formation.formationPrivee(null);
        assertThat(formation.getFormationPrivee()).isNull();
        assertThat(formationPriveeBack.getFormation()).isNull();
    }

    @Test
    void formationInvalidesTest() throws Exception {
        Formation formation = getFormationRandomSampleGenerator();
        FormationInvalide formationInvalideBack = getFormationInvalideRandomSampleGenerator();

        formation.addFormationInvalides(formationInvalideBack);
        assertThat(formation.getFormationInvalides()).containsOnly(formationInvalideBack);
        assertThat(formationInvalideBack.getFormation()).isEqualTo(formation);

        formation.removeFormationInvalides(formationInvalideBack);
        assertThat(formation.getFormationInvalides()).doesNotContain(formationInvalideBack);
        assertThat(formationInvalideBack.getFormation()).isNull();

        formation.formationInvalides(new HashSet<>(Set.of(formationInvalideBack)));
        assertThat(formation.getFormationInvalides()).containsOnly(formationInvalideBack);
        assertThat(formationInvalideBack.getFormation()).isEqualTo(formation);

        formation.setFormationInvalides(new HashSet<>());
        assertThat(formation.getFormationInvalides()).doesNotContain(formationInvalideBack);
        assertThat(formationInvalideBack.getFormation()).isNull();
    }

    @Test
    void inscriptionAdministrativeFormationsTest() throws Exception {
        Formation formation = getFormationRandomSampleGenerator();
        InscriptionAdministrativeFormation inscriptionAdministrativeFormationBack =
            getInscriptionAdministrativeFormationRandomSampleGenerator();

        formation.addInscriptionAdministrativeFormations(inscriptionAdministrativeFormationBack);
        assertThat(formation.getInscriptionAdministrativeFormations()).containsOnly(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrativeFormationBack.getFormation()).isEqualTo(formation);

        formation.removeInscriptionAdministrativeFormations(inscriptionAdministrativeFormationBack);
        assertThat(formation.getInscriptionAdministrativeFormations()).doesNotContain(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrativeFormationBack.getFormation()).isNull();

        formation.inscriptionAdministrativeFormations(new HashSet<>(Set.of(inscriptionAdministrativeFormationBack)));
        assertThat(formation.getInscriptionAdministrativeFormations()).containsOnly(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrativeFormationBack.getFormation()).isEqualTo(formation);

        formation.setInscriptionAdministrativeFormations(new HashSet<>());
        assertThat(formation.getInscriptionAdministrativeFormations()).doesNotContain(inscriptionAdministrativeFormationBack);
        assertThat(inscriptionAdministrativeFormationBack.getFormation()).isNull();
    }

    @Test
    void programmationInscriptionsTest() throws Exception {
        Formation formation = getFormationRandomSampleGenerator();
        ProgrammationInscription programmationInscriptionBack = getProgrammationInscriptionRandomSampleGenerator();

        formation.addProgrammationInscriptions(programmationInscriptionBack);
        assertThat(formation.getProgrammationInscriptions()).containsOnly(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getFormation()).isEqualTo(formation);

        formation.removeProgrammationInscriptions(programmationInscriptionBack);
        assertThat(formation.getProgrammationInscriptions()).doesNotContain(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getFormation()).isNull();

        formation.programmationInscriptions(new HashSet<>(Set.of(programmationInscriptionBack)));
        assertThat(formation.getProgrammationInscriptions()).containsOnly(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getFormation()).isEqualTo(formation);

        formation.setProgrammationInscriptions(new HashSet<>());
        assertThat(formation.getProgrammationInscriptions()).doesNotContain(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getFormation()).isNull();
    }

    @Test
    void formationAutoriseeTest() throws Exception {
        Formation formation = getFormationRandomSampleGenerator();
        FormationAutorisee formationAutoriseeBack = getFormationAutoriseeRandomSampleGenerator();

        formation.addFormationAutorisee(formationAutoriseeBack);
        assertThat(formation.getFormationAutorisees()).containsOnly(formationAutoriseeBack);
        assertThat(formationAutoriseeBack.getFormations()).containsOnly(formation);

        formation.removeFormationAutorisee(formationAutoriseeBack);
        assertThat(formation.getFormationAutorisees()).doesNotContain(formationAutoriseeBack);
        assertThat(formationAutoriseeBack.getFormations()).doesNotContain(formation);

        formation.formationAutorisees(new HashSet<>(Set.of(formationAutoriseeBack)));
        assertThat(formation.getFormationAutorisees()).containsOnly(formationAutoriseeBack);
        assertThat(formationAutoriseeBack.getFormations()).containsOnly(formation);

        formation.setFormationAutorisees(new HashSet<>());
        assertThat(formation.getFormationAutorisees()).doesNotContain(formationAutoriseeBack);
        assertThat(formationAutoriseeBack.getFormations()).doesNotContain(formation);
    }
}
