package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FraisTestSamples.*;
import static sn.ugb.gir.domain.TypeFraisTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FraisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Frais.class);
        Frais frais1 = getFraisSample1();
        Frais frais2 = new Frais();
        assertThat(frais1).isNotEqualTo(frais2);

        frais2.setId(frais1.getId());
        assertThat(frais1).isEqualTo(frais2);

        frais2 = getFraisSample2();
        assertThat(frais1).isNotEqualTo(frais2);
    }

    @Test
    void typeFraisTest() throws Exception {
        Frais frais = getFraisRandomSampleGenerator();
        TypeFrais typeFraisBack = getTypeFraisRandomSampleGenerator();

        frais.setTypeFrais(typeFraisBack);
        assertThat(frais.getTypeFrais()).isEqualTo(typeFraisBack);

        frais.typeFrais(null);
        assertThat(frais.getTypeFrais()).isNull();
    }
}
