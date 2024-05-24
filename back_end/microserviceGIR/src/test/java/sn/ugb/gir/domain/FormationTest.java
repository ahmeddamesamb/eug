package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FormationAutoriseeTestSamples.*;
import static sn.ugb.gir.domain.FormationPriveeTestSamples.*;
import static sn.ugb.gir.domain.FormationTestSamples.*;
import static sn.ugb.gir.domain.NiveauTestSamples.*;
import static sn.ugb.gir.domain.SpecialiteTestSamples.*;

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
}
