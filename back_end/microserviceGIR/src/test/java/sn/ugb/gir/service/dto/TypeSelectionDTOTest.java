package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeSelectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSelectionDTO.class);
        TypeSelectionDTO typeSelectionDTO1 = new TypeSelectionDTO();
        typeSelectionDTO1.setId(1L);
        TypeSelectionDTO typeSelectionDTO2 = new TypeSelectionDTO();
        assertThat(typeSelectionDTO1).isNotEqualTo(typeSelectionDTO2);
        typeSelectionDTO2.setId(typeSelectionDTO1.getId());
        assertThat(typeSelectionDTO1).isEqualTo(typeSelectionDTO2);
        typeSelectionDTO2.setId(2L);
        assertThat(typeSelectionDTO1).isNotEqualTo(typeSelectionDTO2);
        typeSelectionDTO1.setId(null);
        assertThat(typeSelectionDTO1).isNotEqualTo(typeSelectionDTO2);
    }
}
