package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class PaiementFraisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementFraisDTO.class);
        PaiementFraisDTO paiementFraisDTO1 = new PaiementFraisDTO();
        paiementFraisDTO1.setId(1L);
        PaiementFraisDTO paiementFraisDTO2 = new PaiementFraisDTO();
        assertThat(paiementFraisDTO1).isNotEqualTo(paiementFraisDTO2);
        paiementFraisDTO2.setId(paiementFraisDTO1.getId());
        assertThat(paiementFraisDTO1).isEqualTo(paiementFraisDTO2);
        paiementFraisDTO2.setId(2L);
        assertThat(paiementFraisDTO1).isNotEqualTo(paiementFraisDTO2);
        paiementFraisDTO1.setId(null);
        assertThat(paiementFraisDTO1).isNotEqualTo(paiementFraisDTO2);
    }
}
