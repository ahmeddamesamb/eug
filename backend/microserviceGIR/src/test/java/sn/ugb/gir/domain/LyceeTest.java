package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.EtudiantTestSamples.*;
import static sn.ugb.gir.domain.LyceeTestSamples.*;
import static sn.ugb.gir.domain.RegionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class LyceeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lycee.class);
        Lycee lycee1 = getLyceeSample1();
        Lycee lycee2 = new Lycee();
        assertThat(lycee1).isNotEqualTo(lycee2);

        lycee2.setId(lycee1.getId());
        assertThat(lycee1).isEqualTo(lycee2);

        lycee2 = getLyceeSample2();
        assertThat(lycee1).isNotEqualTo(lycee2);
    }

    @Test
    void regionTest() throws Exception {
        Lycee lycee = getLyceeRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        lycee.setRegion(regionBack);
        assertThat(lycee.getRegion()).isEqualTo(regionBack);

        lycee.region(null);
        assertThat(lycee.getRegion()).isNull();
    }

    @Test
    void etudiantsTest() throws Exception {
        Lycee lycee = getLyceeRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        lycee.addEtudiants(etudiantBack);
        assertThat(lycee.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getLycee()).isEqualTo(lycee);

        lycee.removeEtudiants(etudiantBack);
        assertThat(lycee.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getLycee()).isNull();

        lycee.etudiants(new HashSet<>(Set.of(etudiantBack)));
        assertThat(lycee.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getLycee()).isEqualTo(lycee);

        lycee.setEtudiants(new HashSet<>());
        assertThat(lycee.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getLycee()).isNull();
    }
}
