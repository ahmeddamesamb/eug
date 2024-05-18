package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InscriptionAdministrativeFormationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionAdministrativeFormationDTO.class);
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO1 = new InscriptionAdministrativeFormationDTO();
        inscriptionAdministrativeFormationDTO1.setId(1L);
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO2 = new InscriptionAdministrativeFormationDTO();
        assertThat(inscriptionAdministrativeFormationDTO1).isNotEqualTo(inscriptionAdministrativeFormationDTO2);
        inscriptionAdministrativeFormationDTO2.setId(inscriptionAdministrativeFormationDTO1.getId());
        assertThat(inscriptionAdministrativeFormationDTO1).isEqualTo(inscriptionAdministrativeFormationDTO2);
        inscriptionAdministrativeFormationDTO2.setId(2L);
        assertThat(inscriptionAdministrativeFormationDTO1).isNotEqualTo(inscriptionAdministrativeFormationDTO2);
        inscriptionAdministrativeFormationDTO1.setId(null);
        assertThat(inscriptionAdministrativeFormationDTO1).isNotEqualTo(inscriptionAdministrativeFormationDTO2);
    }
}
