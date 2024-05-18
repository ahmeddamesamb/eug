package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeOperationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOperationDTO.class);
        TypeOperationDTO typeOperationDTO1 = new TypeOperationDTO();
        typeOperationDTO1.setId(1L);
        TypeOperationDTO typeOperationDTO2 = new TypeOperationDTO();
        assertThat(typeOperationDTO1).isNotEqualTo(typeOperationDTO2);
        typeOperationDTO2.setId(typeOperationDTO1.getId());
        assertThat(typeOperationDTO1).isEqualTo(typeOperationDTO2);
        typeOperationDTO2.setId(2L);
        assertThat(typeOperationDTO1).isNotEqualTo(typeOperationDTO2);
        typeOperationDTO1.setId(null);
        assertThat(typeOperationDTO1).isNotEqualTo(typeOperationDTO2);
    }
}
