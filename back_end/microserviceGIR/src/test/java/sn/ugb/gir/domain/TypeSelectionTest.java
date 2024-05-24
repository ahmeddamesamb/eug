package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.TypeSelectionTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeSelectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSelection.class);
        TypeSelection typeSelection1 = getTypeSelectionSample1();
        TypeSelection typeSelection2 = new TypeSelection();
        assertThat(typeSelection1).isNotEqualTo(typeSelection2);

        typeSelection2.setId(typeSelection1.getId());
        assertThat(typeSelection1).isEqualTo(typeSelection2);

        typeSelection2 = getTypeSelectionSample2();
        assertThat(typeSelection1).isNotEqualTo(typeSelection2);
    }
}
