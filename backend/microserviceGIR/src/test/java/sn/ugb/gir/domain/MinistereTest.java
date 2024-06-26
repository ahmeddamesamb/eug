package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.MinistereTestSamples.*;
import static sn.ugb.gir.domain.UniversiteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    void universitesTest() throws Exception {
        Ministere ministere = getMinistereRandomSampleGenerator();
        Universite universiteBack = getUniversiteRandomSampleGenerator();

        ministere.addUniversites(universiteBack);
        assertThat(ministere.getUniversites()).containsOnly(universiteBack);
        assertThat(universiteBack.getMinistere()).isEqualTo(ministere);

        ministere.removeUniversites(universiteBack);
        assertThat(ministere.getUniversites()).doesNotContain(universiteBack);
        assertThat(universiteBack.getMinistere()).isNull();

        ministere.universites(new HashSet<>(Set.of(universiteBack)));
        assertThat(ministere.getUniversites()).containsOnly(universiteBack);
        assertThat(universiteBack.getMinistere()).isEqualTo(ministere);

        ministere.setUniversites(new HashSet<>());
        assertThat(ministere.getUniversites()).doesNotContain(universiteBack);
        assertThat(universiteBack.getMinistere()).isNull();
    }
}
