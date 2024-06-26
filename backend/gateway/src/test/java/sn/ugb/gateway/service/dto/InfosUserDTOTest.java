package sn.ugb.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class InfosUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfosUserDTO.class);
        InfosUserDTO infosUserDTO1 = new InfosUserDTO();
        infosUserDTO1.setId(1L);
        InfosUserDTO infosUserDTO2 = new InfosUserDTO();
        assertThat(infosUserDTO1).isNotEqualTo(infosUserDTO2);
        infosUserDTO2.setId(infosUserDTO1.getId());
        assertThat(infosUserDTO1).isEqualTo(infosUserDTO2);
        infosUserDTO2.setId(2L);
        assertThat(infosUserDTO1).isNotEqualTo(infosUserDTO2);
        infosUserDTO1.setId(null);
        assertThat(infosUserDTO1).isNotEqualTo(infosUserDTO2);
    }
}
