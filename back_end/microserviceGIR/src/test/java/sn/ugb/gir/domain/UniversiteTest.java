package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.MinistereTestSamples.*;
import static sn.ugb.gir.domain.UniversiteTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class UniversiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Universite.class);
        Universite universite1 = getUniversiteSample1();
        Universite universite2 = new Universite();
        assertThat(universite1).isNotEqualTo(universite2);

        universite2.setId(universite1.getId());
        assertThat(universite1).isEqualTo(universite2);

        universite2 = getUniversiteSample2();
        assertThat(universite1).isNotEqualTo(universite2);
    }

    @Test
    void ministereTest() throws Exception {
        Universite universite = getUniversiteRandomSampleGenerator();
        Ministere ministereBack = getMinistereRandomSampleGenerator();

        universite.setMinistere(ministereBack);
        assertThat(universite.getMinistere()).isEqualTo(ministereBack);

        universite.ministere(null);
        assertThat(universite.getMinistere()).isNull();
    }
}
