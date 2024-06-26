package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FraisTestSamples.*;
import static sn.ugb.gir.domain.TypeFraisTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeFraisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeFrais.class);
        TypeFrais typeFrais1 = getTypeFraisSample1();
        TypeFrais typeFrais2 = new TypeFrais();
        assertThat(typeFrais1).isNotEqualTo(typeFrais2);

        typeFrais2.setId(typeFrais1.getId());
        assertThat(typeFrais1).isEqualTo(typeFrais2);

        typeFrais2 = getTypeFraisSample2();
        assertThat(typeFrais1).isNotEqualTo(typeFrais2);
    }

    @Test
    void fraisTest() throws Exception {
        TypeFrais typeFrais = getTypeFraisRandomSampleGenerator();
        Frais fraisBack = getFraisRandomSampleGenerator();

        typeFrais.addFrais(fraisBack);
        assertThat(typeFrais.getFrais()).containsOnly(fraisBack);
        assertThat(fraisBack.getTypeFrais()).isEqualTo(typeFrais);

        typeFrais.removeFrais(fraisBack);
        assertThat(typeFrais.getFrais()).doesNotContain(fraisBack);
        assertThat(fraisBack.getTypeFrais()).isNull();

        typeFrais.frais(new HashSet<>(Set.of(fraisBack)));
        assertThat(typeFrais.getFrais()).containsOnly(fraisBack);
        assertThat(fraisBack.getTypeFrais()).isEqualTo(typeFrais);

        typeFrais.setFrais(new HashSet<>());
        assertThat(typeFrais.getFrais()).doesNotContain(fraisBack);
        assertThat(fraisBack.getTypeFrais()).isNull();
    }
}
