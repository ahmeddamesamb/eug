package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class NiveauDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NiveauDTO.class);
        NiveauDTO niveauDTO1 = new NiveauDTO();
        niveauDTO1.setId(1L);
        NiveauDTO niveauDTO2 = new NiveauDTO();
        assertThat(niveauDTO1).isNotEqualTo(niveauDTO2);
        niveauDTO2.setId(niveauDTO1.getId());
        assertThat(niveauDTO1).isEqualTo(niveauDTO2);
        niveauDTO2.setId(2L);
        assertThat(niveauDTO1).isNotEqualTo(niveauDTO2);
        niveauDTO1.setId(null);
        assertThat(niveauDTO1).isNotEqualTo(niveauDTO2);
    }
}
