package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class MinistereDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MinistereDTO.class);
        MinistereDTO ministereDTO1 = new MinistereDTO();
        ministereDTO1.setId(1L);
        MinistereDTO ministereDTO2 = new MinistereDTO();
        assertThat(ministereDTO1).isNotEqualTo(ministereDTO2);
        ministereDTO2.setId(ministereDTO1.getId());
        assertThat(ministereDTO1).isEqualTo(ministereDTO2);
        ministereDTO2.setId(2L);
        assertThat(ministereDTO1).isNotEqualTo(ministereDTO2);
        ministereDTO1.setId(null);
        assertThat(ministereDTO1).isNotEqualTo(ministereDTO2);
    }
}
