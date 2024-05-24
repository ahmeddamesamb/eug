package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DisciplineSportiveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisciplineSportiveDTO.class);
        DisciplineSportiveDTO disciplineSportiveDTO1 = new DisciplineSportiveDTO();
        disciplineSportiveDTO1.setId(1L);
        DisciplineSportiveDTO disciplineSportiveDTO2 = new DisciplineSportiveDTO();
        assertThat(disciplineSportiveDTO1).isNotEqualTo(disciplineSportiveDTO2);
        disciplineSportiveDTO2.setId(disciplineSportiveDTO1.getId());
        assertThat(disciplineSportiveDTO1).isEqualTo(disciplineSportiveDTO2);
        disciplineSportiveDTO2.setId(2L);
        assertThat(disciplineSportiveDTO1).isNotEqualTo(disciplineSportiveDTO2);
        disciplineSportiveDTO1.setId(null);
        assertThat(disciplineSportiveDTO1).isNotEqualTo(disciplineSportiveDTO2);
    }
}
