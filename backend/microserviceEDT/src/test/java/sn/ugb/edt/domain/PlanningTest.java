package sn.ugb.edt.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.edt.domain.PlanningTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.edt.web.rest.TestUtil;

class PlanningTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Planning.class);
        Planning planning1 = getPlanningSample1();
        Planning planning2 = new Planning();
        assertThat(planning1).isNotEqualTo(planning2);

        planning2.setId(planning1.getId());
        assertThat(planning1).isEqualTo(planning2);

        planning2 = getPlanningSample2();
        assertThat(planning1).isNotEqualTo(planning2);
    }
}
