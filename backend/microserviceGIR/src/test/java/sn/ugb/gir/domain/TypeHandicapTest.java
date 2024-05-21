package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.TypeHandicapTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeHandicapTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeHandicap.class);
        TypeHandicap typeHandicap1 = getTypeHandicapSample1();
        TypeHandicap typeHandicap2 = new TypeHandicap();
        assertThat(typeHandicap1).isNotEqualTo(typeHandicap2);

        typeHandicap2.setId(typeHandicap1.getId());
        assertThat(typeHandicap1).isEqualTo(typeHandicap2);

        typeHandicap2 = getTypeHandicapSample2();
        assertThat(typeHandicap1).isNotEqualTo(typeHandicap2);
    }
}
