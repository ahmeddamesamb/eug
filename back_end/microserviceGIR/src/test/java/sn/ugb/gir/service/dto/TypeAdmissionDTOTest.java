package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeAdmissionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeAdmissionDTO.class);
        TypeAdmissionDTO typeAdmissionDTO1 = new TypeAdmissionDTO();
        typeAdmissionDTO1.setId(1L);
        TypeAdmissionDTO typeAdmissionDTO2 = new TypeAdmissionDTO();
        assertThat(typeAdmissionDTO1).isNotEqualTo(typeAdmissionDTO2);
        typeAdmissionDTO2.setId(typeAdmissionDTO1.getId());
        assertThat(typeAdmissionDTO1).isEqualTo(typeAdmissionDTO2);
        typeAdmissionDTO2.setId(2L);
        assertThat(typeAdmissionDTO1).isNotEqualTo(typeAdmissionDTO2);
        typeAdmissionDTO1.setId(null);
        assertThat(typeAdmissionDTO1).isNotEqualTo(typeAdmissionDTO2);
    }
}
