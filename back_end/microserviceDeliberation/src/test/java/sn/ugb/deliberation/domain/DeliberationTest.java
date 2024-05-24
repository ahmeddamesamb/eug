package sn.ugb.deliberation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.deliberation.domain.DeliberationTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.deliberation.web.rest.TestUtil;

class DeliberationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deliberation.class);
        Deliberation deliberation1 = getDeliberationSample1();
        Deliberation deliberation2 = new Deliberation();
        assertThat(deliberation1).isNotEqualTo(deliberation2);

        deliberation2.setId(deliberation1.getId());
        assertThat(deliberation1).isEqualTo(deliberation2);

        deliberation2 = getDeliberationSample2();
        assertThat(deliberation1).isNotEqualTo(deliberation2);
    }
}
