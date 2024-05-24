package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InscriptionDoctoratDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionDoctoratDTO.class);
        InscriptionDoctoratDTO inscriptionDoctoratDTO1 = new InscriptionDoctoratDTO();
        inscriptionDoctoratDTO1.setId(1L);
        InscriptionDoctoratDTO inscriptionDoctoratDTO2 = new InscriptionDoctoratDTO();
        assertThat(inscriptionDoctoratDTO1).isNotEqualTo(inscriptionDoctoratDTO2);
        inscriptionDoctoratDTO2.setId(inscriptionDoctoratDTO1.getId());
        assertThat(inscriptionDoctoratDTO1).isEqualTo(inscriptionDoctoratDTO2);
        inscriptionDoctoratDTO2.setId(2L);
        assertThat(inscriptionDoctoratDTO1).isNotEqualTo(inscriptionDoctoratDTO2);
        inscriptionDoctoratDTO1.setId(null);
        assertThat(inscriptionDoctoratDTO1).isNotEqualTo(inscriptionDoctoratDTO2);
    }
}
