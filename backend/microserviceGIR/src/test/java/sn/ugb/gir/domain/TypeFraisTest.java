package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.TypeFraisTestSamples.*;

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
}
