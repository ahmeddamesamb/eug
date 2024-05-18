package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class UFRDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UFRDTO.class);
        UFRDTO uFRDTO1 = new UFRDTO();
        uFRDTO1.setId(1L);
        UFRDTO uFRDTO2 = new UFRDTO();
        assertThat(uFRDTO1).isNotEqualTo(uFRDTO2);
        uFRDTO2.setId(uFRDTO1.getId());
        assertThat(uFRDTO1).isEqualTo(uFRDTO2);
        uFRDTO2.setId(2L);
        assertThat(uFRDTO1).isNotEqualTo(uFRDTO2);
        uFRDTO1.setId(null);
        assertThat(uFRDTO1).isNotEqualTo(uFRDTO2);
    }
}
