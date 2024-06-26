package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.CycleTestSamples.*;
import static sn.ugb.gir.domain.FormationTestSamples.*;
import static sn.ugb.gir.domain.NiveauTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class NiveauTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Niveau.class);
        Niveau niveau1 = getNiveauSample1();
        Niveau niveau2 = new Niveau();
        assertThat(niveau1).isNotEqualTo(niveau2);

        niveau2.setId(niveau1.getId());
        assertThat(niveau1).isEqualTo(niveau2);

        niveau2 = getNiveauSample2();
        assertThat(niveau1).isNotEqualTo(niveau2);
    }

    @Test
    void typeCycleTest() throws Exception {
        Niveau niveau = getNiveauRandomSampleGenerator();
        Cycle cycleBack = getCycleRandomSampleGenerator();

        niveau.setTypeCycle(cycleBack);
        assertThat(niveau.getTypeCycle()).isEqualTo(cycleBack);

        niveau.typeCycle(null);
        assertThat(niveau.getTypeCycle()).isNull();
    }

    @Test
    void formationsTest() throws Exception {
        Niveau niveau = getNiveauRandomSampleGenerator();
        Formation formationBack = getFormationRandomSampleGenerator();

        niveau.addFormations(formationBack);
        assertThat(niveau.getFormations()).containsOnly(formationBack);
        assertThat(formationBack.getNiveau()).isEqualTo(niveau);

        niveau.removeFormations(formationBack);
        assertThat(niveau.getFormations()).doesNotContain(formationBack);
        assertThat(formationBack.getNiveau()).isNull();

        niveau.formations(new HashSet<>(Set.of(formationBack)));
        assertThat(niveau.getFormations()).containsOnly(formationBack);
        assertThat(formationBack.getNiveau()).isEqualTo(niveau);

        niveau.setFormations(new HashSet<>());
        assertThat(niveau.getFormations()).doesNotContain(formationBack);
        assertThat(formationBack.getNiveau()).isNull();
    }
}
