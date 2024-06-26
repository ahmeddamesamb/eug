package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.OperateurTestSamples.*;
import static sn.ugb.gir.domain.PaiementFormationPriveeTestSamples.*;
import static sn.ugb.gir.domain.PaiementFraisTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class OperateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operateur.class);
        Operateur operateur1 = getOperateurSample1();
        Operateur operateur2 = new Operateur();
        assertThat(operateur1).isNotEqualTo(operateur2);

        operateur2.setId(operateur1.getId());
        assertThat(operateur1).isEqualTo(operateur2);

        operateur2 = getOperateurSample2();
        assertThat(operateur1).isNotEqualTo(operateur2);
    }

    @Test
    void paiementFraisTest() throws Exception {
        Operateur operateur = getOperateurRandomSampleGenerator();
        PaiementFrais paiementFraisBack = getPaiementFraisRandomSampleGenerator();

        operateur.addPaiementFrais(paiementFraisBack);
        assertThat(operateur.getPaiementFrais()).containsOnly(paiementFraisBack);
        assertThat(paiementFraisBack.getOperateur()).isEqualTo(operateur);

        operateur.removePaiementFrais(paiementFraisBack);
        assertThat(operateur.getPaiementFrais()).doesNotContain(paiementFraisBack);
        assertThat(paiementFraisBack.getOperateur()).isNull();

        operateur.paiementFrais(new HashSet<>(Set.of(paiementFraisBack)));
        assertThat(operateur.getPaiementFrais()).containsOnly(paiementFraisBack);
        assertThat(paiementFraisBack.getOperateur()).isEqualTo(operateur);

        operateur.setPaiementFrais(new HashSet<>());
        assertThat(operateur.getPaiementFrais()).doesNotContain(paiementFraisBack);
        assertThat(paiementFraisBack.getOperateur()).isNull();
    }

    @Test
    void paiementFormationPriveesTest() throws Exception {
        Operateur operateur = getOperateurRandomSampleGenerator();
        PaiementFormationPrivee paiementFormationPriveeBack = getPaiementFormationPriveeRandomSampleGenerator();

        operateur.addPaiementFormationPrivees(paiementFormationPriveeBack);
        assertThat(operateur.getPaiementFormationPrivees()).containsOnly(paiementFormationPriveeBack);
        assertThat(paiementFormationPriveeBack.getOperateur()).isEqualTo(operateur);

        operateur.removePaiementFormationPrivees(paiementFormationPriveeBack);
        assertThat(operateur.getPaiementFormationPrivees()).doesNotContain(paiementFormationPriveeBack);
        assertThat(paiementFormationPriveeBack.getOperateur()).isNull();

        operateur.paiementFormationPrivees(new HashSet<>(Set.of(paiementFormationPriveeBack)));
        assertThat(operateur.getPaiementFormationPrivees()).containsOnly(paiementFormationPriveeBack);
        assertThat(paiementFormationPriveeBack.getOperateur()).isEqualTo(operateur);

        operateur.setPaiementFormationPrivees(new HashSet<>());
        assertThat(operateur.getPaiementFormationPrivees()).doesNotContain(paiementFormationPriveeBack);
        assertThat(paiementFormationPriveeBack.getOperateur()).isNull();
    }
}
