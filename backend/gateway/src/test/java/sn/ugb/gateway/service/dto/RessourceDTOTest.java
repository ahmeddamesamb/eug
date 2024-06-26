package sn.ugb.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class RessourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RessourceDTO.class);
        RessourceDTO ressourceDTO1 = new RessourceDTO();
        ressourceDTO1.setId(1L);
        RessourceDTO ressourceDTO2 = new RessourceDTO();
        assertThat(ressourceDTO1).isNotEqualTo(ressourceDTO2);
        ressourceDTO2.setId(ressourceDTO1.getId());
        assertThat(ressourceDTO1).isEqualTo(ressourceDTO2);
        ressourceDTO2.setId(2L);
        assertThat(ressourceDTO1).isNotEqualTo(ressourceDTO2);
        ressourceDTO1.setId(null);
        assertThat(ressourceDTO1).isNotEqualTo(ressourceDTO2);
    }
}
