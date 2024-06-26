package sn.ugb.aide.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.aide.domain.RessourceAideTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.aide.web.rest.TestUtil;

class RessourceAideTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RessourceAide.class);
        RessourceAide ressourceAide1 = getRessourceAideSample1();
        RessourceAide ressourceAide2 = new RessourceAide();
        assertThat(ressourceAide1).isNotEqualTo(ressourceAide2);

        ressourceAide2.setId(ressourceAide1.getId());
        assertThat(ressourceAide1).isEqualTo(ressourceAide2);

        ressourceAide2 = getRessourceAideSample2();
        assertThat(ressourceAide1).isNotEqualTo(ressourceAide2);
    }
}
