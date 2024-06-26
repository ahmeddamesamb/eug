package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FormationTestSamples.*;
import static sn.ugb.gir.domain.TypeFormationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeFormationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeFormation.class);
        TypeFormation typeFormation1 = getTypeFormationSample1();
        TypeFormation typeFormation2 = new TypeFormation();
        assertThat(typeFormation1).isNotEqualTo(typeFormation2);

        typeFormation2.setId(typeFormation1.getId());
        assertThat(typeFormation1).isEqualTo(typeFormation2);

        typeFormation2 = getTypeFormationSample2();
        assertThat(typeFormation1).isNotEqualTo(typeFormation2);
    }

    @Test
    void formationsTest() throws Exception {
        TypeFormation typeFormation = getTypeFormationRandomSampleGenerator();
        Formation formationBack = getFormationRandomSampleGenerator();

        typeFormation.addFormations(formationBack);
        assertThat(typeFormation.getFormations()).containsOnly(formationBack);
        assertThat(formationBack.getTypeFormation()).isEqualTo(typeFormation);

        typeFormation.removeFormations(formationBack);
        assertThat(typeFormation.getFormations()).doesNotContain(formationBack);
        assertThat(formationBack.getTypeFormation()).isNull();

        typeFormation.formations(new HashSet<>(Set.of(formationBack)));
        assertThat(typeFormation.getFormations()).containsOnly(formationBack);
        assertThat(formationBack.getTypeFormation()).isEqualTo(typeFormation);

        typeFormation.setFormations(new HashSet<>());
        assertThat(typeFormation.getFormations()).doesNotContain(formationBack);
        assertThat(formationBack.getTypeFormation()).isNull();
    }
}
