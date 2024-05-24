package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.PaysTestSamples.*;
import static sn.ugb.gir.domain.RegionTestSamples.*;

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
}
