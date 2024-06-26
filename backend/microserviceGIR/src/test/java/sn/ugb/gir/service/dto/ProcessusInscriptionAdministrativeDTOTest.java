package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class ProcessusInscriptionAdministrativeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessusInscriptionAdministrativeDTO.class);
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO1 = new ProcessusInscriptionAdministrativeDTO();
        processusInscriptionAdministrativeDTO1.setId(1L);
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO2 = new ProcessusInscriptionAdministrativeDTO();
        assertThat(processusInscriptionAdministrativeDTO1).isNotEqualTo(processusInscriptionAdministrativeDTO2);
        processusInscriptionAdministrativeDTO2.setId(processusInscriptionAdministrativeDTO1.getId());
        assertThat(processusInscriptionAdministrativeDTO1).isEqualTo(processusInscriptionAdministrativeDTO2);
        processusInscriptionAdministrativeDTO2.setId(2L);
        assertThat(processusInscriptionAdministrativeDTO1).isNotEqualTo(processusInscriptionAdministrativeDTO2);
        processusInscriptionAdministrativeDTO1.setId(null);
        assertThat(processusInscriptionAdministrativeDTO1).isNotEqualTo(processusInscriptionAdministrativeDTO2);
    }
}
