package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class UniversiteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniversiteDTO.class);
        UniversiteDTO universiteDTO1 = new UniversiteDTO();
        universiteDTO1.setId(1L);
        UniversiteDTO universiteDTO2 = new UniversiteDTO();
        assertThat(universiteDTO1).isNotEqualTo(universiteDTO2);
        universiteDTO2.setId(universiteDTO1.getId());
        assertThat(universiteDTO1).isEqualTo(universiteDTO2);
        universiteDTO2.setId(2L);
        assertThat(universiteDTO1).isNotEqualTo(universiteDTO2);
        universiteDTO1.setId(null);
        assertThat(universiteDTO1).isNotEqualTo(universiteDTO2);
    }
}
