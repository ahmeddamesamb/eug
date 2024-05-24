package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DisciplineSportiveEtudiantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisciplineSportiveEtudiantDTO.class);
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO1 = new DisciplineSportiveEtudiantDTO();
        disciplineSportiveEtudiantDTO1.setId(1L);
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO2 = new DisciplineSportiveEtudiantDTO();
        assertThat(disciplineSportiveEtudiantDTO1).isNotEqualTo(disciplineSportiveEtudiantDTO2);
        disciplineSportiveEtudiantDTO2.setId(disciplineSportiveEtudiantDTO1.getId());
        assertThat(disciplineSportiveEtudiantDTO1).isEqualTo(disciplineSportiveEtudiantDTO2);
        disciplineSportiveEtudiantDTO2.setId(2L);
        assertThat(disciplineSportiveEtudiantDTO1).isNotEqualTo(disciplineSportiveEtudiantDTO2);
        disciplineSportiveEtudiantDTO1.setId(null);
        assertThat(disciplineSportiveEtudiantDTO1).isNotEqualTo(disciplineSportiveEtudiantDTO2);
    }
}
