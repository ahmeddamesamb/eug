package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.PaysTestSamples.*;
import static sn.ugb.gir.domain.ZoneTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class ZoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Zone.class);
        Zone zone1 = getZoneSample1();
        Zone zone2 = new Zone();
        assertThat(zone1).isNotEqualTo(zone2);

        zone2.setId(zone1.getId());
        assertThat(zone1).isEqualTo(zone2);

        zone2 = getZoneSample2();
        assertThat(zone1).isNotEqualTo(zone2);
    }

    @Test
    void paysTest() throws Exception {
        Zone zone = getZoneRandomSampleGenerator();
        Pays paysBack = getPaysRandomSampleGenerator();

        zone.addPays(paysBack);
        assertThat(zone.getPays()).containsOnly(paysBack);
        assertThat(paysBack.getZones()).containsOnly(zone);

        zone.removePays(paysBack);
        assertThat(zone.getPays()).doesNotContain(paysBack);
        assertThat(paysBack.getZones()).doesNotContain(zone);

        zone.pays(new HashSet<>(Set.of(paysBack)));
        assertThat(zone.getPays()).containsOnly(paysBack);
        assertThat(paysBack.getZones()).containsOnly(zone);

        zone.setPays(new HashSet<>());
        assertThat(zone.getPays()).doesNotContain(paysBack);
        assertThat(paysBack.getZones()).doesNotContain(zone);
    }
}
