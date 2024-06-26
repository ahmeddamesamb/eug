package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.CycleTestSamples.*;
import static sn.ugb.gir.domain.FraisTestSamples.*;
import static sn.ugb.gir.domain.NiveauTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class CycleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cycle.class);
        Cycle cycle1 = getCycleSample1();
        Cycle cycle2 = new Cycle();
        assertThat(cycle1).isNotEqualTo(cycle2);

        cycle2.setId(cycle1.getId());
        assertThat(cycle1).isEqualTo(cycle2);

        cycle2 = getCycleSample2();
        assertThat(cycle1).isNotEqualTo(cycle2);
    }

    @Test
    void niveauxTest() throws Exception {
        Cycle cycle = getCycleRandomSampleGenerator();
        Niveau niveauBack = getNiveauRandomSampleGenerator();

        cycle.addNiveaux(niveauBack);
        assertThat(cycle.getNiveaux()).containsOnly(niveauBack);
        assertThat(niveauBack.getTypeCycle()).isEqualTo(cycle);

        cycle.removeNiveaux(niveauBack);
        assertThat(cycle.getNiveaux()).doesNotContain(niveauBack);
        assertThat(niveauBack.getTypeCycle()).isNull();

        cycle.niveaux(new HashSet<>(Set.of(niveauBack)));
        assertThat(cycle.getNiveaux()).containsOnly(niveauBack);
        assertThat(niveauBack.getTypeCycle()).isEqualTo(cycle);

        cycle.setNiveaux(new HashSet<>());
        assertThat(cycle.getNiveaux()).doesNotContain(niveauBack);
        assertThat(niveauBack.getTypeCycle()).isNull();
    }

    @Test
    void fraisTest() throws Exception {
        Cycle cycle = getCycleRandomSampleGenerator();
        Frais fraisBack = getFraisRandomSampleGenerator();

        cycle.addFrais(fraisBack);
        assertThat(cycle.getFrais()).containsOnly(fraisBack);
        assertThat(fraisBack.getTypeCycle()).isEqualTo(cycle);

        cycle.removeFrais(fraisBack);
        assertThat(cycle.getFrais()).doesNotContain(fraisBack);
        assertThat(fraisBack.getTypeCycle()).isNull();

        cycle.frais(new HashSet<>(Set.of(fraisBack)));
        assertThat(cycle.getFrais()).containsOnly(fraisBack);
        assertThat(fraisBack.getTypeCycle()).isEqualTo(cycle);

        cycle.setFrais(new HashSet<>());
        assertThat(cycle.getFrais()).doesNotContain(fraisBack);
        assertThat(fraisBack.getTypeCycle()).isNull();
    }
}
