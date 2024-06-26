package sn.ugb.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class ServiceUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceUserDTO.class);
        ServiceUserDTO serviceUserDTO1 = new ServiceUserDTO();
        serviceUserDTO1.setId(1L);
        ServiceUserDTO serviceUserDTO2 = new ServiceUserDTO();
        assertThat(serviceUserDTO1).isNotEqualTo(serviceUserDTO2);
        serviceUserDTO2.setId(serviceUserDTO1.getId());
        assertThat(serviceUserDTO1).isEqualTo(serviceUserDTO2);
        serviceUserDTO2.setId(2L);
        assertThat(serviceUserDTO1).isNotEqualTo(serviceUserDTO2);
        serviceUserDTO1.setId(null);
        assertThat(serviceUserDTO1).isNotEqualTo(serviceUserDTO2);
    }
}
