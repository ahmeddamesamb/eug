package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class SpecialiteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialiteDTO.class);
        SpecialiteDTO specialiteDTO1 = new SpecialiteDTO();
        specialiteDTO1.setId(1L);
        SpecialiteDTO specialiteDTO2 = new SpecialiteDTO();
        assertThat(specialiteDTO1).isNotEqualTo(specialiteDTO2);
        specialiteDTO2.setId(specialiteDTO1.getId());
        assertThat(specialiteDTO1).isEqualTo(specialiteDTO2);
        specialiteDTO2.setId(2L);
        assertThat(specialiteDTO1).isNotEqualTo(specialiteDTO2);
        specialiteDTO1.setId(null);
        assertThat(specialiteDTO1).isNotEqualTo(specialiteDTO2);
    }
}
