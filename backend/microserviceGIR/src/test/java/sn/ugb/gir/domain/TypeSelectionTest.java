package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.EtudiantTestSamples.*;
import static sn.ugb.gir.domain.TypeSelectionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    void etudiantsTest() throws Exception {
        TypeSelection typeSelection = getTypeSelectionRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        typeSelection.addEtudiants(etudiantBack);
        assertThat(typeSelection.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getTypeSelection()).isEqualTo(typeSelection);

        typeSelection.removeEtudiants(etudiantBack);
        assertThat(typeSelection.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getTypeSelection()).isNull();

        typeSelection.etudiants(new HashSet<>(Set.of(etudiantBack)));
        assertThat(typeSelection.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getTypeSelection()).isEqualTo(typeSelection);

        typeSelection.setEtudiants(new HashSet<>());
        assertThat(typeSelection.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getTypeSelection()).isNull();
    }
}
