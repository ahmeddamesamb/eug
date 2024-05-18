package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FormationValideDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationValideDTO.class);
        FormationValideDTO formationValideDTO1 = new FormationValideDTO();
        formationValideDTO1.setId(1L);
        FormationValideDTO formationValideDTO2 = new FormationValideDTO();
        assertThat(formationValideDTO1).isNotEqualTo(formationValideDTO2);
        formationValideDTO2.setId(formationValideDTO1.getId());
        assertThat(formationValideDTO1).isEqualTo(formationValideDTO2);
        formationValideDTO2.setId(2L);
        assertThat(formationValideDTO1).isNotEqualTo(formationValideDTO2);
        formationValideDTO1.setId(null);
        assertThat(formationValideDTO1).isNotEqualTo(formationValideDTO2);
    }
}
