package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DoctoratTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DoctoratTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doctorat.class);
        Doctorat doctorat1 = getDoctoratSample1();
        Doctorat doctorat2 = new Doctorat();
        assertThat(doctorat1).isNotEqualTo(doctorat2);

        doctorat2.setId(doctorat1.getId());
        assertThat(doctorat1).isEqualTo(doctorat2);

        doctorat2 = getDoctoratSample2();
        assertThat(doctorat1).isNotEqualTo(doctorat2);
    }
}
