package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class ProgrammationInscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgrammationInscriptionDTO.class);
        ProgrammationInscriptionDTO programmationInscriptionDTO1 = new ProgrammationInscriptionDTO();
        programmationInscriptionDTO1.setId(1L);
        ProgrammationInscriptionDTO programmationInscriptionDTO2 = new ProgrammationInscriptionDTO();
        assertThat(programmationInscriptionDTO1).isNotEqualTo(programmationInscriptionDTO2);
        programmationInscriptionDTO2.setId(programmationInscriptionDTO1.getId());
        assertThat(programmationInscriptionDTO1).isEqualTo(programmationInscriptionDTO2);
        programmationInscriptionDTO2.setId(2L);
        assertThat(programmationInscriptionDTO1).isNotEqualTo(programmationInscriptionDTO2);
        programmationInscriptionDTO1.setId(null);
        assertThat(programmationInscriptionDTO1).isNotEqualTo(programmationInscriptionDTO2);
    }
}
