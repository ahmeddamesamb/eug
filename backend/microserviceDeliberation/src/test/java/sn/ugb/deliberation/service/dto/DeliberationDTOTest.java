package sn.ugb.deliberation.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.deliberation.web.rest.TestUtil;

class DeliberationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliberationDTO.class);
        DeliberationDTO deliberationDTO1 = new DeliberationDTO();
        deliberationDTO1.setId(1L);
        DeliberationDTO deliberationDTO2 = new DeliberationDTO();
        assertThat(deliberationDTO1).isNotEqualTo(deliberationDTO2);
        deliberationDTO2.setId(deliberationDTO1.getId());
        assertThat(deliberationDTO1).isEqualTo(deliberationDTO2);
        deliberationDTO2.setId(2L);
        assertThat(deliberationDTO1).isNotEqualTo(deliberationDTO2);
        deliberationDTO1.setId(null);
        assertThat(deliberationDTO1).isNotEqualTo(deliberationDTO2);
    }
}
