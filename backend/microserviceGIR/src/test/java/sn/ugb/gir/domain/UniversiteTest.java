package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FraisTestSamples.*;
import static sn.ugb.gir.domain.MinistereTestSamples.*;
import static sn.ugb.gir.domain.UfrTestSamples.*;
import static sn.ugb.gir.domain.UniversiteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    void ufrsTest() throws Exception {
        Universite universite = getUniversiteRandomSampleGenerator();
        Ufr ufrBack = getUfrRandomSampleGenerator();

        universite.addUfrs(ufrBack);
        assertThat(universite.getUfrs()).containsOnly(ufrBack);
        assertThat(ufrBack.getUniversite()).isEqualTo(universite);

        universite.removeUfrs(ufrBack);
        assertThat(universite.getUfrs()).doesNotContain(ufrBack);
        assertThat(ufrBack.getUniversite()).isNull();

        universite.ufrs(new HashSet<>(Set.of(ufrBack)));
        assertThat(universite.getUfrs()).containsOnly(ufrBack);
        assertThat(ufrBack.getUniversite()).isEqualTo(universite);

        universite.setUfrs(new HashSet<>());
        assertThat(universite.getUfrs()).doesNotContain(ufrBack);
        assertThat(ufrBack.getUniversite()).isNull();
    }

    @Test
    void fraisTest() throws Exception {
        Universite universite = getUniversiteRandomSampleGenerator();
        Frais fraisBack = getFraisRandomSampleGenerator();

        universite.addFrais(fraisBack);
        assertThat(universite.getFrais()).containsOnly(fraisBack);
        assertThat(fraisBack.getUniversites()).containsOnly(universite);

        universite.removeFrais(fraisBack);
        assertThat(universite.getFrais()).doesNotContain(fraisBack);
        assertThat(fraisBack.getUniversites()).doesNotContain(universite);

        universite.frais(new HashSet<>(Set.of(fraisBack)));
        assertThat(universite.getFrais()).containsOnly(fraisBack);
        assertThat(fraisBack.getUniversites()).containsOnly(universite);

        universite.setFrais(new HashSet<>());
        assertThat(universite.getFrais()).doesNotContain(fraisBack);
        assertThat(fraisBack.getUniversites()).doesNotContain(universite);
    }
}
