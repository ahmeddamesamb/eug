package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.NiveauTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class NiveauTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Niveau.class);
        Niveau niveau1 = getNiveauSample1();
        Niveau niveau2 = new Niveau();
        assertThat(niveau1).isNotEqualTo(niveau2);

        niveau2.setId(niveau1.getId());
        assertThat(niveau1).isEqualTo(niveau2);

        niveau2 = getNiveauSample2();
        assertThat(niveau1).isNotEqualTo(niveau2);
    }
}
