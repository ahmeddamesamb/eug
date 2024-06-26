package sn.ugb.aide.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.aide.web.rest.TestUtil;

class RessourceAideDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RessourceAideDTO.class);
        RessourceAideDTO ressourceAideDTO1 = new RessourceAideDTO();
        ressourceAideDTO1.setId(1L);
        RessourceAideDTO ressourceAideDTO2 = new RessourceAideDTO();
        assertThat(ressourceAideDTO1).isNotEqualTo(ressourceAideDTO2);
        ressourceAideDTO2.setId(ressourceAideDTO1.getId());
        assertThat(ressourceAideDTO1).isEqualTo(ressourceAideDTO2);
        ressourceAideDTO2.setId(2L);
        assertThat(ressourceAideDTO1).isNotEqualTo(ressourceAideDTO2);
        ressourceAideDTO1.setId(null);
        assertThat(ressourceAideDTO1).isNotEqualTo(ressourceAideDTO2);
    }
}
