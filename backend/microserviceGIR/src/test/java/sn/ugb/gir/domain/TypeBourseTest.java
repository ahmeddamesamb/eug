package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.InformationPersonnelleTestSamples.*;
import static sn.ugb.gir.domain.TypeBourseTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    void informationPersonnellesTest() throws Exception {
        TypeBourse typeBourse = getTypeBourseRandomSampleGenerator();
        InformationPersonnelle informationPersonnelleBack = getInformationPersonnelleRandomSampleGenerator();

        typeBourse.addInformationPersonnelles(informationPersonnelleBack);
        assertThat(typeBourse.getInformationPersonnelles()).containsOnly(informationPersonnelleBack);
        assertThat(informationPersonnelleBack.getTypeBourse()).isEqualTo(typeBourse);

        typeBourse.removeInformationPersonnelles(informationPersonnelleBack);
        assertThat(typeBourse.getInformationPersonnelles()).doesNotContain(informationPersonnelleBack);
        assertThat(informationPersonnelleBack.getTypeBourse()).isNull();

        typeBourse.informationPersonnelles(new HashSet<>(Set.of(informationPersonnelleBack)));
        assertThat(typeBourse.getInformationPersonnelles()).containsOnly(informationPersonnelleBack);
        assertThat(informationPersonnelleBack.getTypeBourse()).isEqualTo(typeBourse);

        typeBourse.setInformationPersonnelles(new HashSet<>());
        assertThat(typeBourse.getInformationPersonnelles()).doesNotContain(informationPersonnelleBack);
        assertThat(informationPersonnelleBack.getTypeBourse()).isNull();
    }
}
