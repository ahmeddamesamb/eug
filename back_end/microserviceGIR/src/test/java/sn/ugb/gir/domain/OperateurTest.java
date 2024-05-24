package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.OperateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class OperateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operateur.class);
        Operateur operateur1 = getOperateurSample1();
        Operateur operateur2 = new Operateur();
        assertThat(operateur1).isNotEqualTo(operateur2);

        operateur2.setId(operateur1.getId());
        assertThat(operateur1).isEqualTo(operateur2);

        operateur2 = getOperateurSample2();
        assertThat(operateur1).isNotEqualTo(operateur2);
    }
}
