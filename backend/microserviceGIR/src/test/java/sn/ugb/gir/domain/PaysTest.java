package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.PaysTestSamples.*;
import static sn.ugb.gir.domain.RegionTestSamples.*;
import static sn.ugb.gir.domain.ZoneTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class PaysTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pays.class);
        Pays pays1 = getPaysSample1();
        Pays pays2 = new Pays();
        assertThat(pays1).isNotEqualTo(pays2);

        pays2.setId(pays1.getId());
        assertThat(pays1).isEqualTo(pays2);

        pays2 = getPaysSample2();
        assertThat(pays1).isNotEqualTo(pays2);
    }

    @Test
    void zoneTest() throws Exception {
        Pays pays = getPaysRandomSampleGenerator();
        Zone zoneBack = getZoneRandomSampleGenerator();

        pays.addZone(zoneBack);
        assertThat(pays.getZones()).containsOnly(zoneBack);

        pays.removeZone(zoneBack);
        assertThat(pays.getZones()).doesNotContain(zoneBack);

        pays.zones(new HashSet<>(Set.of(zoneBack)));
        assertThat(pays.getZones()).containsOnly(zoneBack);

        pays.setZones(new HashSet<>());
        assertThat(pays.getZones()).doesNotContain(zoneBack);
    }

    @Test
    void regionsTest() throws Exception {
        Pays pays = getPaysRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        pays.addRegions(regionBack);
        assertThat(pays.getRegions()).containsOnly(regionBack);
        assertThat(regionBack.getPays()).isEqualTo(pays);

        pays.removeRegions(regionBack);
        assertThat(pays.getRegions()).doesNotContain(regionBack);
        assertThat(regionBack.getPays()).isNull();

        pays.regions(new HashSet<>(Set.of(regionBack)));
        assertThat(pays.getRegions()).containsOnly(regionBack);
        assertThat(regionBack.getPays()).isEqualTo(pays);

        pays.setRegions(new HashSet<>());
        assertThat(pays.getRegions()).doesNotContain(regionBack);
        assertThat(regionBack.getPays()).isNull();
    }
}
