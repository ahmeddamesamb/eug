package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.MinistereTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class MinistereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ministere.class);
        Ministere ministere1 = getMinistereSample1();
        Ministere ministere2 = new Ministere();
        assertThat(ministere1).isNotEqualTo(ministere2);

        ministere2.setId(ministere1.getId());
        assertThat(ministere1).isEqualTo(ministere2);

        ministere2 = getMinistereSample2();
        assertThat(ministere1).isNotEqualTo(ministere2);
    }
}
