package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FormationInvalideDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationInvalideDTO.class);
        FormationInvalideDTO formationInvalideDTO1 = new FormationInvalideDTO();
        formationInvalideDTO1.setId(1L);
        FormationInvalideDTO formationInvalideDTO2 = new FormationInvalideDTO();
        assertThat(formationInvalideDTO1).isNotEqualTo(formationInvalideDTO2);
        formationInvalideDTO2.setId(formationInvalideDTO1.getId());
        assertThat(formationInvalideDTO1).isEqualTo(formationInvalideDTO2);
        formationInvalideDTO2.setId(2L);
        assertThat(formationInvalideDTO1).isNotEqualTo(formationInvalideDTO2);
        formationInvalideDTO1.setId(null);
        assertThat(formationInvalideDTO1).isNotEqualTo(formationInvalideDTO2);
    }
}
