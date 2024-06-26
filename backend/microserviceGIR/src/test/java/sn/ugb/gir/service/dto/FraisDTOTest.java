package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class FraisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FraisDTO.class);
        FraisDTO fraisDTO1 = new FraisDTO();
        fraisDTO1.setId(1L);
        FraisDTO fraisDTO2 = new FraisDTO();
        assertThat(fraisDTO1).isNotEqualTo(fraisDTO2);
        fraisDTO2.setId(fraisDTO1.getId());
        assertThat(fraisDTO1).isEqualTo(fraisDTO2);
        fraisDTO2.setId(2L);
        assertThat(fraisDTO1).isNotEqualTo(fraisDTO2);
        fraisDTO1.setId(null);
        assertThat(fraisDTO1).isNotEqualTo(fraisDTO2);
    }
}
