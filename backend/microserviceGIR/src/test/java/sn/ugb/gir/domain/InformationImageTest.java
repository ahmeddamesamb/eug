package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.InformationImageTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InformationImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InformationImage.class);
        InformationImage informationImage1 = getInformationImageSample1();
        InformationImage informationImage2 = new InformationImage();
        assertThat(informationImage1).isNotEqualTo(informationImage2);

        informationImage2.setId(informationImage1.getId());
        assertThat(informationImage1).isEqualTo(informationImage2);

        informationImage2 = getInformationImageSample2();
        assertThat(informationImage1).isNotEqualTo(informationImage2);
    }
}
