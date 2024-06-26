package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.EtudiantTestSamples.*;
import static sn.ugb.gir.domain.LyceeTestSamples.*;
import static sn.ugb.gir.domain.PaysTestSamples.*;
import static sn.ugb.gir.domain.RegionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class RegionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Region.class);
        Region region1 = getRegionSample1();
        Region region2 = new Region();
        assertThat(region1).isNotEqualTo(region2);

        region2.setId(region1.getId());
        assertThat(region1).isEqualTo(region2);

        region2 = getRegionSample2();
        assertThat(region1).isNotEqualTo(region2);
    }

    @Test
    void paysTest() throws Exception {
        Region region = getRegionRandomSampleGenerator();
        Pays paysBack = getPaysRandomSampleGenerator();

        region.setPays(paysBack);
        assertThat(region.getPays()).isEqualTo(paysBack);

        region.pays(null);
        assertThat(region.getPays()).isNull();
    }

    @Test
    void etudiantsTest() throws Exception {
        Region region = getRegionRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        region.addEtudiants(etudiantBack);
        assertThat(region.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getRegion()).isEqualTo(region);

        region.removeEtudiants(etudiantBack);
        assertThat(region.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getRegion()).isNull();

        region.etudiants(new HashSet<>(Set.of(etudiantBack)));
        assertThat(region.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getRegion()).isEqualTo(region);

        region.setEtudiants(new HashSet<>());
        assertThat(region.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getRegion()).isNull();
    }

    @Test
    void lyceesTest() throws Exception {
        Region region = getRegionRandomSampleGenerator();
        Lycee lyceeBack = getLyceeRandomSampleGenerator();

        region.addLycees(lyceeBack);
        assertThat(region.getLycees()).containsOnly(lyceeBack);
        assertThat(lyceeBack.getRegion()).isEqualTo(region);

        region.removeLycees(lyceeBack);
        assertThat(region.getLycees()).doesNotContain(lyceeBack);
        assertThat(lyceeBack.getRegion()).isNull();

        region.lycees(new HashSet<>(Set.of(lyceeBack)));
        assertThat(region.getLycees()).containsOnly(lyceeBack);
        assertThat(lyceeBack.getRegion()).isEqualTo(region);

        region.setLycees(new HashSet<>());
        assertThat(region.getLycees()).doesNotContain(lyceeBack);
        assertThat(lyceeBack.getRegion()).isNull();
    }
}
