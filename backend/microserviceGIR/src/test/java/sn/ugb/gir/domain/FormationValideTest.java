package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FormationTestSamples.*;
import static sn.ugb.gir.domain.FormationValideTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FormationValideTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationValide.class);
        FormationValide formationValide1 = getFormationValideSample1();
        FormationValide formationValide2 = new FormationValide();
        assertThat(formationValide1).isNotEqualTo(formationValide2);

        formationValide2.setId(formationValide1.getId());
        assertThat(formationValide1).isEqualTo(formationValide2);

        formationValide2 = getFormationValideSample2();
        assertThat(formationValide1).isNotEqualTo(formationValide2);
    }

    @Test
    void formationTest() throws Exception {
        FormationValide formationValide = getFormationValideRandomSampleGenerator();
        Formation formationBack = getFormationRandomSampleGenerator();

        formationValide.setFormation(formationBack);
        assertThat(formationValide.getFormation()).isEqualTo(formationBack);

        formationValide.formation(null);
        assertThat(formationValide.getFormation()).isNull();
    }
}
