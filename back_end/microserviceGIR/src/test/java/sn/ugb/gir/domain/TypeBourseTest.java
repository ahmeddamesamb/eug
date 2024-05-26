package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.TypeBourseTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeBourseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeBourse.class);
        TypeBourse typeBourse1 = getTypeBourseSample1();
        TypeBourse typeBourse2 = new TypeBourse();
        assertThat(typeBourse1).isNotEqualTo(typeBourse2);

        typeBourse2.setId(typeBourse1.getId());
        assertThat(typeBourse1).isEqualTo(typeBourse2);

        typeBourse2 = getTypeBourseSample2();
        assertThat(typeBourse1).isNotEqualTo(typeBourse2);
    }
}
