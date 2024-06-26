package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DomaineTestSamples.*;
import static sn.ugb.gir.domain.UfrTestSamples.*;
import static sn.ugb.gir.domain.UniversiteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class UfrTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ufr.class);
        Ufr ufr1 = getUfrSample1();
        Ufr ufr2 = new Ufr();
        assertThat(ufr1).isNotEqualTo(ufr2);

        ufr2.setId(ufr1.getId());
        assertThat(ufr1).isEqualTo(ufr2);

        ufr2 = getUfrSample2();
        assertThat(ufr1).isNotEqualTo(ufr2);
    }

    @Test
    void universiteTest() throws Exception {
        Ufr ufr = getUfrRandomSampleGenerator();
        Universite universiteBack = getUniversiteRandomSampleGenerator();

        ufr.setUniversite(universiteBack);
        assertThat(ufr.getUniversite()).isEqualTo(universiteBack);

        ufr.universite(null);
        assertThat(ufr.getUniversite()).isNull();
    }

    @Test
    void domaineTest() throws Exception {
        Ufr ufr = getUfrRandomSampleGenerator();
        Domaine domaineBack = getDomaineRandomSampleGenerator();

        ufr.addDomaine(domaineBack);
        assertThat(ufr.getDomaines()).containsOnly(domaineBack);
        assertThat(domaineBack.getUfrs()).containsOnly(ufr);

        ufr.removeDomaine(domaineBack);
        assertThat(ufr.getDomaines()).doesNotContain(domaineBack);
        assertThat(domaineBack.getUfrs()).doesNotContain(ufr);

        ufr.domaines(new HashSet<>(Set.of(domaineBack)));
        assertThat(ufr.getDomaines()).containsOnly(domaineBack);
        assertThat(domaineBack.getUfrs()).containsOnly(ufr);

        ufr.setDomaines(new HashSet<>());
        assertThat(ufr.getDomaines()).doesNotContain(domaineBack);
        assertThat(domaineBack.getUfrs()).doesNotContain(ufr);
    }
}
