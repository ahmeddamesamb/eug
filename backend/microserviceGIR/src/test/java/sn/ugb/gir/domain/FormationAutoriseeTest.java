package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FormationAutoriseeTestSamples.*;
import static sn.ugb.gir.domain.FormationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FormationAutoriseeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationAutorisee.class);
        FormationAutorisee formationAutorisee1 = getFormationAutoriseeSample1();
        FormationAutorisee formationAutorisee2 = new FormationAutorisee();
        assertThat(formationAutorisee1).isNotEqualTo(formationAutorisee2);

        formationAutorisee2.setId(formationAutorisee1.getId());
        assertThat(formationAutorisee1).isEqualTo(formationAutorisee2);

        formationAutorisee2 = getFormationAutoriseeSample2();
        assertThat(formationAutorisee1).isNotEqualTo(formationAutorisee2);
    }

    @Test
    void formationTest() throws Exception {
        FormationAutorisee formationAutorisee = getFormationAutoriseeRandomSampleGenerator();
        Formation formationBack = getFormationRandomSampleGenerator();

        formationAutorisee.addFormation(formationBack);
        assertThat(formationAutorisee.getFormations()).containsOnly(formationBack);

        formationAutorisee.removeFormation(formationBack);
        assertThat(formationAutorisee.getFormations()).doesNotContain(formationBack);

        formationAutorisee.formations(new HashSet<>(Set.of(formationBack)));
        assertThat(formationAutorisee.getFormations()).containsOnly(formationBack);

        formationAutorisee.setFormations(new HashSet<>());
        assertThat(formationAutorisee.getFormations()).doesNotContain(formationBack);
    }
}
