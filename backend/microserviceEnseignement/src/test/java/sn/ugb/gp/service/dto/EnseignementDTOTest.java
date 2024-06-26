package sn.ugb.gp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gp.web.rest.TestUtil;

class EnseignementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnseignementDTO.class);
        EnseignementDTO enseignementDTO1 = new EnseignementDTO();
        enseignementDTO1.setId(1L);
        EnseignementDTO enseignementDTO2 = new EnseignementDTO();
        assertThat(enseignementDTO1).isNotEqualTo(enseignementDTO2);
        enseignementDTO2.setId(enseignementDTO1.getId());
        assertThat(enseignementDTO1).isEqualTo(enseignementDTO2);
        enseignementDTO2.setId(2L);
        assertThat(enseignementDTO1).isNotEqualTo(enseignementDTO2);
        enseignementDTO1.setId(null);
        assertThat(enseignementDTO1).isNotEqualTo(enseignementDTO2);
    }
}
