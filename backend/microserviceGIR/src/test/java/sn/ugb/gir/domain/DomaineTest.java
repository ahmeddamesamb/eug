package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DomaineTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DomaineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Domaine.class);
        Domaine domaine1 = getDomaineSample1();
        Domaine domaine2 = new Domaine();
        assertThat(domaine1).isNotEqualTo(domaine2);

        domaine2.setId(domaine1.getId());
        assertThat(domaine1).isEqualTo(domaine2);

        domaine2 = getDomaineSample2();
        assertThat(domaine1).isNotEqualTo(domaine2);
    }
}
