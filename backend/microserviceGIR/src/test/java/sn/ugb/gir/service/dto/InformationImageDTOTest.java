package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InformationImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InformationImageDTO.class);
        InformationImageDTO informationImageDTO1 = new InformationImageDTO();
        informationImageDTO1.setId(1L);
        InformationImageDTO informationImageDTO2 = new InformationImageDTO();
        assertThat(informationImageDTO1).isNotEqualTo(informationImageDTO2);
        informationImageDTO2.setId(informationImageDTO1.getId());
        assertThat(informationImageDTO1).isEqualTo(informationImageDTO2);
        informationImageDTO2.setId(2L);
        assertThat(informationImageDTO1).isNotEqualTo(informationImageDTO2);
        informationImageDTO1.setId(null);
        assertThat(informationImageDTO1).isNotEqualTo(informationImageDTO2);
    }
}
