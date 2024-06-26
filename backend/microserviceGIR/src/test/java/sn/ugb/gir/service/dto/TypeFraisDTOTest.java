package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeFraisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeFraisDTO.class);
        TypeFraisDTO typeFraisDTO1 = new TypeFraisDTO();
        typeFraisDTO1.setId(1L);
        TypeFraisDTO typeFraisDTO2 = new TypeFraisDTO();
        assertThat(typeFraisDTO1).isNotEqualTo(typeFraisDTO2);
        typeFraisDTO2.setId(typeFraisDTO1.getId());
        assertThat(typeFraisDTO1).isEqualTo(typeFraisDTO2);
        typeFraisDTO2.setId(2L);
        assertThat(typeFraisDTO1).isNotEqualTo(typeFraisDTO2);
        typeFraisDTO1.setId(null);
        assertThat(typeFraisDTO1).isNotEqualTo(typeFraisDTO2);
    }
}
