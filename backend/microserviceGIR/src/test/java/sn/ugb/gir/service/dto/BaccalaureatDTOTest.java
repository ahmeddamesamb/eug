package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class BaccalaureatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaccalaureatDTO.class);
        BaccalaureatDTO baccalaureatDTO1 = new BaccalaureatDTO();
        baccalaureatDTO1.setId(1L);
        BaccalaureatDTO baccalaureatDTO2 = new BaccalaureatDTO();
        assertThat(baccalaureatDTO1).isNotEqualTo(baccalaureatDTO2);
        baccalaureatDTO2.setId(baccalaureatDTO1.getId());
        assertThat(baccalaureatDTO1).isEqualTo(baccalaureatDTO2);
        baccalaureatDTO2.setId(2L);
        assertThat(baccalaureatDTO1).isNotEqualTo(baccalaureatDTO2);
        baccalaureatDTO1.setId(null);
        assertThat(baccalaureatDTO1).isNotEqualTo(baccalaureatDTO2);
    }
}
