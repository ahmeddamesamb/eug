package sn.ugb.gp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gp.domain.EnseignementTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gp.web.rest.TestUtil;

class EnseignementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enseignement.class);
        Enseignement enseignement1 = getEnseignementSample1();
        Enseignement enseignement2 = new Enseignement();
        assertThat(enseignement1).isNotEqualTo(enseignement2);

        enseignement2.setId(enseignement1.getId());
        assertThat(enseignement1).isEqualTo(enseignement2);

        enseignement2 = getEnseignementSample2();
        assertThat(enseignement1).isNotEqualTo(enseignement2);
    }
}
