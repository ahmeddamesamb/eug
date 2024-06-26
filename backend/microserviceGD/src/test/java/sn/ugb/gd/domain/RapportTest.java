package sn.ugb.gd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gd.domain.RapportTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gd.web.rest.TestUtil;

class RapportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rapport.class);
        Rapport rapport1 = getRapportSample1();
        Rapport rapport2 = new Rapport();
        assertThat(rapport1).isNotEqualTo(rapport2);

        rapport2.setId(rapport1.getId());
        assertThat(rapport1).isEqualTo(rapport2);

        rapport2 = getRapportSample2();
        assertThat(rapport1).isNotEqualTo(rapport2);
    }
}
