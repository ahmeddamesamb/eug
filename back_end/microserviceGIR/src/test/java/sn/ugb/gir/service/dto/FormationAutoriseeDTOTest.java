package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FormationAutoriseeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationAutoriseeDTO.class);
        FormationAutoriseeDTO formationAutoriseeDTO1 = new FormationAutoriseeDTO();
        formationAutoriseeDTO1.setId(1L);
        FormationAutoriseeDTO formationAutoriseeDTO2 = new FormationAutoriseeDTO();
        assertThat(formationAutoriseeDTO1).isNotEqualTo(formationAutoriseeDTO2);
        formationAutoriseeDTO2.setId(formationAutoriseeDTO1.getId());
        assertThat(formationAutoriseeDTO1).isEqualTo(formationAutoriseeDTO2);
        formationAutoriseeDTO2.setId(2L);
        assertThat(formationAutoriseeDTO1).isNotEqualTo(formationAutoriseeDTO2);
        formationAutoriseeDTO1.setId(null);
        assertThat(formationAutoriseeDTO1).isNotEqualTo(formationAutoriseeDTO2);
    }
}
