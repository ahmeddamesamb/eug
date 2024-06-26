package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeFormationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeFormationDTO.class);
        TypeFormationDTO typeFormationDTO1 = new TypeFormationDTO();
        typeFormationDTO1.setId(1L);
        TypeFormationDTO typeFormationDTO2 = new TypeFormationDTO();
        assertThat(typeFormationDTO1).isNotEqualTo(typeFormationDTO2);
        typeFormationDTO2.setId(typeFormationDTO1.getId());
        assertThat(typeFormationDTO1).isEqualTo(typeFormationDTO2);
        typeFormationDTO2.setId(2L);
        assertThat(typeFormationDTO1).isNotEqualTo(typeFormationDTO2);
        typeFormationDTO1.setId(null);
        assertThat(typeFormationDTO1).isNotEqualTo(typeFormationDTO2);
    }
}
