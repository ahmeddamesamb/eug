package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FormationPriveeTestSamples.*;
import static sn.ugb.gir.domain.FormationTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FormationPriveeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationPrivee.class);
        FormationPrivee formationPrivee1 = getFormationPriveeSample1();
        FormationPrivee formationPrivee2 = new FormationPrivee();
        assertThat(formationPrivee1).isNotEqualTo(formationPrivee2);

        formationPrivee2.setId(formationPrivee1.getId());
        assertThat(formationPrivee1).isEqualTo(formationPrivee2);

        formationPrivee2 = getFormationPriveeSample2();
        assertThat(formationPrivee1).isNotEqualTo(formationPrivee2);
    }

    @Test
    void formationTest() throws Exception {
        FormationPrivee formationPrivee = getFormationPriveeRandomSampleGenerator();
        Formation formationBack = getFormationRandomSampleGenerator();

        formationPrivee.setFormation(formationBack);
        assertThat(formationPrivee.getFormation()).isEqualTo(formationBack);

        formationPrivee.formation(null);
        assertThat(formationPrivee.getFormation()).isNull();
    }
}
