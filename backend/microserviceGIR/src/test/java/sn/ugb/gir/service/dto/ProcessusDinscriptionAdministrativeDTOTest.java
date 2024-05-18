package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class ProcessusDinscriptionAdministrativeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessusDinscriptionAdministrativeDTO.class);
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO1 = new ProcessusDinscriptionAdministrativeDTO();
        processusDinscriptionAdministrativeDTO1.setId(1L);
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO2 = new ProcessusDinscriptionAdministrativeDTO();
        assertThat(processusDinscriptionAdministrativeDTO1).isNotEqualTo(processusDinscriptionAdministrativeDTO2);
        processusDinscriptionAdministrativeDTO2.setId(processusDinscriptionAdministrativeDTO1.getId());
        assertThat(processusDinscriptionAdministrativeDTO1).isEqualTo(processusDinscriptionAdministrativeDTO2);
        processusDinscriptionAdministrativeDTO2.setId(2L);
        assertThat(processusDinscriptionAdministrativeDTO1).isNotEqualTo(processusDinscriptionAdministrativeDTO2);
        processusDinscriptionAdministrativeDTO1.setId(null);
        assertThat(processusDinscriptionAdministrativeDTO1).isNotEqualTo(processusDinscriptionAdministrativeDTO2);
    }
}
