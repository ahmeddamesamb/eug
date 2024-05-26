package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class LyceeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LyceeDTO.class);
        LyceeDTO lyceeDTO1 = new LyceeDTO();
        lyceeDTO1.setId(1L);
        LyceeDTO lyceeDTO2 = new LyceeDTO();
        assertThat(lyceeDTO1).isNotEqualTo(lyceeDTO2);
        lyceeDTO2.setId(lyceeDTO1.getId());
        assertThat(lyceeDTO1).isEqualTo(lyceeDTO2);
        lyceeDTO2.setId(2L);
        assertThat(lyceeDTO1).isNotEqualTo(lyceeDTO2);
        lyceeDTO1.setId(null);
        assertThat(lyceeDTO1).isNotEqualTo(lyceeDTO2);
    }
}
