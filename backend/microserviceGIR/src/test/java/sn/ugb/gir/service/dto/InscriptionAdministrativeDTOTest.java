package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InscriptionAdministrativeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionAdministrativeDTO.class);
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO1 = new InscriptionAdministrativeDTO();
        inscriptionAdministrativeDTO1.setId(1L);
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO2 = new InscriptionAdministrativeDTO();
        assertThat(inscriptionAdministrativeDTO1).isNotEqualTo(inscriptionAdministrativeDTO2);
        inscriptionAdministrativeDTO2.setId(inscriptionAdministrativeDTO1.getId());
        assertThat(inscriptionAdministrativeDTO1).isEqualTo(inscriptionAdministrativeDTO2);
        inscriptionAdministrativeDTO2.setId(2L);
        assertThat(inscriptionAdministrativeDTO1).isNotEqualTo(inscriptionAdministrativeDTO2);
        inscriptionAdministrativeDTO1.setId(null);
        assertThat(inscriptionAdministrativeDTO1).isNotEqualTo(inscriptionAdministrativeDTO2);
    }
}
