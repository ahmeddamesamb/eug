package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.TypeAdmissionTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeAdmissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeAdmission.class);
        TypeAdmission typeAdmission1 = getTypeAdmissionSample1();
        TypeAdmission typeAdmission2 = new TypeAdmission();
        assertThat(typeAdmission1).isNotEqualTo(typeAdmission2);

        typeAdmission2.setId(typeAdmission1.getId());
        assertThat(typeAdmission1).isEqualTo(typeAdmission2);

        typeAdmission2 = getTypeAdmissionSample2();
        assertThat(typeAdmission1).isNotEqualTo(typeAdmission2);
    }
}
