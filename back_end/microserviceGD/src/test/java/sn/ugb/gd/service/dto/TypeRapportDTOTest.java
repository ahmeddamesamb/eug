package sn.ugb.gd.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gd.web.rest.TestUtil;

class TypeRapportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeRapportDTO.class);
        TypeRapportDTO typeRapportDTO1 = new TypeRapportDTO();
        typeRapportDTO1.setId(1L);
        TypeRapportDTO typeRapportDTO2 = new TypeRapportDTO();
        assertThat(typeRapportDTO1).isNotEqualTo(typeRapportDTO2);
        typeRapportDTO2.setId(typeRapportDTO1.getId());
        assertThat(typeRapportDTO1).isEqualTo(typeRapportDTO2);
        typeRapportDTO2.setId(2L);
        assertThat(typeRapportDTO1).isNotEqualTo(typeRapportDTO2);
        typeRapportDTO1.setId(null);
        assertThat(typeRapportDTO1).isNotEqualTo(typeRapportDTO2);
    }
}
