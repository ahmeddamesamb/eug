package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FormationPriveeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationPriveeDTO.class);
        FormationPriveeDTO formationPriveeDTO1 = new FormationPriveeDTO();
        formationPriveeDTO1.setId(1L);
        FormationPriveeDTO formationPriveeDTO2 = new FormationPriveeDTO();
        assertThat(formationPriveeDTO1).isNotEqualTo(formationPriveeDTO2);
        formationPriveeDTO2.setId(formationPriveeDTO1.getId());
        assertThat(formationPriveeDTO1).isEqualTo(formationPriveeDTO2);
        formationPriveeDTO2.setId(2L);
        assertThat(formationPriveeDTO1).isNotEqualTo(formationPriveeDTO2);
        formationPriveeDTO1.setId(null);
        assertThat(formationPriveeDTO1).isNotEqualTo(formationPriveeDTO2);
    }
}
