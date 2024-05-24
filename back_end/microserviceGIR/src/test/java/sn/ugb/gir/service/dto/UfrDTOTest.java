package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class UfrDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UfrDTO.class);
        UfrDTO ufrDTO1 = new UfrDTO();
        ufrDTO1.setId(1L);
        UfrDTO ufrDTO2 = new UfrDTO();
        assertThat(ufrDTO1).isNotEqualTo(ufrDTO2);
        ufrDTO2.setId(ufrDTO1.getId());
        assertThat(ufrDTO1).isEqualTo(ufrDTO2);
        ufrDTO2.setId(2L);
        assertThat(ufrDTO1).isNotEqualTo(ufrDTO2);
        ufrDTO1.setId(null);
        assertThat(ufrDTO1).isNotEqualTo(ufrDTO2);
    }
}
