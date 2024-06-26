package sn.ugb.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class HistoriqueConnexionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueConnexionDTO.class);
        HistoriqueConnexionDTO historiqueConnexionDTO1 = new HistoriqueConnexionDTO();
        historiqueConnexionDTO1.setId(1L);
        HistoriqueConnexionDTO historiqueConnexionDTO2 = new HistoriqueConnexionDTO();
        assertThat(historiqueConnexionDTO1).isNotEqualTo(historiqueConnexionDTO2);
        historiqueConnexionDTO2.setId(historiqueConnexionDTO1.getId());
        assertThat(historiqueConnexionDTO1).isEqualTo(historiqueConnexionDTO2);
        historiqueConnexionDTO2.setId(2L);
        assertThat(historiqueConnexionDTO1).isNotEqualTo(historiqueConnexionDTO2);
        historiqueConnexionDTO1.setId(null);
        assertThat(historiqueConnexionDTO1).isNotEqualTo(historiqueConnexionDTO2);
    }
}
