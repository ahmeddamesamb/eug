package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.UFRTestSamples.*;
import static sn.ugb.gir.domain.UniversiteTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class UFRTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UFR.class);
        UFR uFR1 = getUFRSample1();
        UFR uFR2 = new UFR();
        assertThat(uFR1).isNotEqualTo(uFR2);

        uFR2.setId(uFR1.getId());
        assertThat(uFR1).isEqualTo(uFR2);

        uFR2 = getUFRSample2();
        assertThat(uFR1).isNotEqualTo(uFR2);
    }

    @Test
    void universiteTest() throws Exception {
        UFR uFR = getUFRRandomSampleGenerator();
        Universite universiteBack = getUniversiteRandomSampleGenerator();

        uFR.setUniversite(universiteBack);
        assertThat(uFR.getUniversite()).isEqualTo(universiteBack);

        uFR.universite(null);
        assertThat(uFR.getUniversite()).isNull();
    }
}
