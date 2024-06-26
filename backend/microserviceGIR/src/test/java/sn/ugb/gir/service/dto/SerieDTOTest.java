package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class SerieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SerieDTO.class);
        SerieDTO serieDTO1 = new SerieDTO();
        serieDTO1.setId(1L);
        SerieDTO serieDTO2 = new SerieDTO();
        assertThat(serieDTO1).isNotEqualTo(serieDTO2);
        serieDTO2.setId(serieDTO1.getId());
        assertThat(serieDTO1).isEqualTo(serieDTO2);
        serieDTO2.setId(2L);
        assertThat(serieDTO1).isNotEqualTo(serieDTO2);
        serieDTO1.setId(null);
        assertThat(serieDTO1).isNotEqualTo(serieDTO2);
    }
}
