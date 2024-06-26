package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeHandicapDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeHandicapDTO.class);
        TypeHandicapDTO typeHandicapDTO1 = new TypeHandicapDTO();
        typeHandicapDTO1.setId(1L);
        TypeHandicapDTO typeHandicapDTO2 = new TypeHandicapDTO();
        assertThat(typeHandicapDTO1).isNotEqualTo(typeHandicapDTO2);
        typeHandicapDTO2.setId(typeHandicapDTO1.getId());
        assertThat(typeHandicapDTO1).isEqualTo(typeHandicapDTO2);
        typeHandicapDTO2.setId(2L);
        assertThat(typeHandicapDTO1).isNotEqualTo(typeHandicapDTO2);
        typeHandicapDTO1.setId(null);
        assertThat(typeHandicapDTO1).isNotEqualTo(typeHandicapDTO2);
    }
}
