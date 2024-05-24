package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeBourseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeBourseDTO.class);
        TypeBourseDTO typeBourseDTO1 = new TypeBourseDTO();
        typeBourseDTO1.setId(1L);
        TypeBourseDTO typeBourseDTO2 = new TypeBourseDTO();
        assertThat(typeBourseDTO1).isNotEqualTo(typeBourseDTO2);
        typeBourseDTO2.setId(typeBourseDTO1.getId());
        assertThat(typeBourseDTO1).isEqualTo(typeBourseDTO2);
        typeBourseDTO2.setId(2L);
        assertThat(typeBourseDTO1).isNotEqualTo(typeBourseDTO2);
        typeBourseDTO1.setId(null);
        assertThat(typeBourseDTO1).isNotEqualTo(typeBourseDTO2);
    }
}
