package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.AnneeAcademiqueTestSamples.*;
import static sn.ugb.gir.domain.FormationInvalideTestSamples.*;
import static sn.ugb.gir.domain.FormationTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FormationInvalideTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationInvalide.class);
        FormationInvalide formationInvalide1 = getFormationInvalideSample1();
        FormationInvalide formationInvalide2 = new FormationInvalide();
        assertThat(formationInvalide1).isNotEqualTo(formationInvalide2);

        formationInvalide2.setId(formationInvalide1.getId());
        assertThat(formationInvalide1).isEqualTo(formationInvalide2);

        formationInvalide2 = getFormationInvalideSample2();
        assertThat(formationInvalide1).isNotEqualTo(formationInvalide2);
    }

    @Test
    void formationTest() throws Exception {
        FormationInvalide formationInvalide = getFormationInvalideRandomSampleGenerator();
        Formation formationBack = getFormationRandomSampleGenerator();

        formationInvalide.setFormation(formationBack);
        assertThat(formationInvalide.getFormation()).isEqualTo(formationBack);

        formationInvalide.formation(null);
        assertThat(formationInvalide.getFormation()).isNull();
    }

    @Test
    void anneeAcademiqueTest() throws Exception {
        FormationInvalide formationInvalide = getFormationInvalideRandomSampleGenerator();
        AnneeAcademique anneeAcademiqueBack = getAnneeAcademiqueRandomSampleGenerator();

        formationInvalide.setAnneeAcademique(anneeAcademiqueBack);
        assertThat(formationInvalide.getAnneeAcademique()).isEqualTo(anneeAcademiqueBack);

        formationInvalide.anneeAcademique(null);
        assertThat(formationInvalide.getAnneeAcademique()).isNull();
    }
}
