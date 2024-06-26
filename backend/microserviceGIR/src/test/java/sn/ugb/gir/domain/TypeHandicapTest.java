package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.InformationPersonnelleTestSamples.*;
import static sn.ugb.gir.domain.TypeHandicapTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    void informationPersonnellesTest() throws Exception {
        TypeHandicap typeHandicap = getTypeHandicapRandomSampleGenerator();
        InformationPersonnelle informationPersonnelleBack = getInformationPersonnelleRandomSampleGenerator();

        typeHandicap.addInformationPersonnelles(informationPersonnelleBack);
        assertThat(typeHandicap.getInformationPersonnelles()).containsOnly(informationPersonnelleBack);
        assertThat(informationPersonnelleBack.getTypeHandicap()).isEqualTo(typeHandicap);

        typeHandicap.removeInformationPersonnelles(informationPersonnelleBack);
        assertThat(typeHandicap.getInformationPersonnelles()).doesNotContain(informationPersonnelleBack);
        assertThat(informationPersonnelleBack.getTypeHandicap()).isNull();

        typeHandicap.informationPersonnelles(new HashSet<>(Set.of(informationPersonnelleBack)));
        assertThat(typeHandicap.getInformationPersonnelles()).containsOnly(informationPersonnelleBack);
        assertThat(informationPersonnelleBack.getTypeHandicap()).isEqualTo(typeHandicap);

        typeHandicap.setInformationPersonnelles(new HashSet<>());
        assertThat(typeHandicap.getInformationPersonnelles()).doesNotContain(informationPersonnelleBack);
        assertThat(informationPersonnelleBack.getTypeHandicap()).isNull();
    }
}
