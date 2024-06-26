package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.CycleTestSamples.*;
import static sn.ugb.gir.domain.FraisTestSamples.*;
import static sn.ugb.gir.domain.PaiementFraisTestSamples.*;
import static sn.ugb.gir.domain.TypeFraisTestSamples.*;
import static sn.ugb.gir.domain.UniversiteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FraisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Frais.class);
        Frais frais1 = getFraisSample1();
        Frais frais2 = new Frais();
        assertThat(frais1).isNotEqualTo(frais2);

        frais2.setId(frais1.getId());
        assertThat(frais1).isEqualTo(frais2);

        frais2 = getFraisSample2();
        assertThat(frais1).isNotEqualTo(frais2);
    }

    @Test
    void typeFraisTest() throws Exception {
        Frais frais = getFraisRandomSampleGenerator();
        TypeFrais typeFraisBack = getTypeFraisRandomSampleGenerator();

        frais.setTypeFrais(typeFraisBack);
        assertThat(frais.getTypeFrais()).isEqualTo(typeFraisBack);

        frais.typeFrais(null);
        assertThat(frais.getTypeFrais()).isNull();
    }

    @Test
    void typeCycleTest() throws Exception {
        Frais frais = getFraisRandomSampleGenerator();
        Cycle cycleBack = getCycleRandomSampleGenerator();

        frais.setTypeCycle(cycleBack);
        assertThat(frais.getTypeCycle()).isEqualTo(cycleBack);

        frais.typeCycle(null);
        assertThat(frais.getTypeCycle()).isNull();
    }

    @Test
    void universiteTest() throws Exception {
        Frais frais = getFraisRandomSampleGenerator();
        Universite universiteBack = getUniversiteRandomSampleGenerator();

        frais.addUniversite(universiteBack);
        assertThat(frais.getUniversites()).containsOnly(universiteBack);

        frais.removeUniversite(universiteBack);
        assertThat(frais.getUniversites()).doesNotContain(universiteBack);

        frais.universites(new HashSet<>(Set.of(universiteBack)));
        assertThat(frais.getUniversites()).containsOnly(universiteBack);

        frais.setUniversites(new HashSet<>());
        assertThat(frais.getUniversites()).doesNotContain(universiteBack);
    }

    @Test
    void paiementFraisTest() throws Exception {
        Frais frais = getFraisRandomSampleGenerator();
        PaiementFrais paiementFraisBack = getPaiementFraisRandomSampleGenerator();

        frais.addPaiementFrais(paiementFraisBack);
        assertThat(frais.getPaiementFrais()).containsOnly(paiementFraisBack);
        assertThat(paiementFraisBack.getFrais()).isEqualTo(frais);

        frais.removePaiementFrais(paiementFraisBack);
        assertThat(frais.getPaiementFrais()).doesNotContain(paiementFraisBack);
        assertThat(paiementFraisBack.getFrais()).isNull();

        frais.paiementFrais(new HashSet<>(Set.of(paiementFraisBack)));
        assertThat(frais.getPaiementFrais()).containsOnly(paiementFraisBack);
        assertThat(paiementFraisBack.getFrais()).isEqualTo(frais);

        frais.setPaiementFrais(new HashSet<>());
        assertThat(frais.getPaiementFrais()).doesNotContain(paiementFraisBack);
        assertThat(paiementFraisBack.getFrais()).isNull();
    }
}
