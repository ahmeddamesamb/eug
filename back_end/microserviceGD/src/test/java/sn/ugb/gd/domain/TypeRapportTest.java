package sn.ugb.gd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gd.domain.TypeRapportTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gd.web.rest.TestUtil;

class TypeRapportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeRapport.class);
        TypeRapport typeRapport1 = getTypeRapportSample1();
        TypeRapport typeRapport2 = new TypeRapport();
        assertThat(typeRapport1).isNotEqualTo(typeRapport2);

        typeRapport2.setId(typeRapport1.getId());
        assertThat(typeRapport1).isEqualTo(typeRapport2);

        typeRapport2 = getTypeRapportSample2();
        assertThat(typeRapport1).isNotEqualTo(typeRapport2);
    }
}
