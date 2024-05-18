package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class PaiementFormationPriveeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaiementFormationPriveeDTO.class);
        PaiementFormationPriveeDTO paiementFormationPriveeDTO1 = new PaiementFormationPriveeDTO();
        paiementFormationPriveeDTO1.setId(1L);
        PaiementFormationPriveeDTO paiementFormationPriveeDTO2 = new PaiementFormationPriveeDTO();
        assertThat(paiementFormationPriveeDTO1).isNotEqualTo(paiementFormationPriveeDTO2);
        paiementFormationPriveeDTO2.setId(paiementFormationPriveeDTO1.getId());
        assertThat(paiementFormationPriveeDTO1).isEqualTo(paiementFormationPriveeDTO2);
        paiementFormationPriveeDTO2.setId(2L);
        assertThat(paiementFormationPriveeDTO1).isNotEqualTo(paiementFormationPriveeDTO2);
        paiementFormationPriveeDTO1.setId(null);
        assertThat(paiementFormationPriveeDTO1).isNotEqualTo(paiementFormationPriveeDTO2);
    }
}
