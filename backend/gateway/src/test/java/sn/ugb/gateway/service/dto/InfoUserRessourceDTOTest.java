package sn.ugb.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class InfoUserRessourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfoUserRessourceDTO.class);
        InfoUserRessourceDTO infoUserRessourceDTO1 = new InfoUserRessourceDTO();
        infoUserRessourceDTO1.setId(1L);
        InfoUserRessourceDTO infoUserRessourceDTO2 = new InfoUserRessourceDTO();
        assertThat(infoUserRessourceDTO1).isNotEqualTo(infoUserRessourceDTO2);
        infoUserRessourceDTO2.setId(infoUserRessourceDTO1.getId());
        assertThat(infoUserRessourceDTO1).isEqualTo(infoUserRessourceDTO2);
        infoUserRessourceDTO2.setId(2L);
        assertThat(infoUserRessourceDTO1).isNotEqualTo(infoUserRessourceDTO2);
        infoUserRessourceDTO1.setId(null);
        assertThat(infoUserRessourceDTO1).isNotEqualTo(infoUserRessourceDTO2);
    }
}
