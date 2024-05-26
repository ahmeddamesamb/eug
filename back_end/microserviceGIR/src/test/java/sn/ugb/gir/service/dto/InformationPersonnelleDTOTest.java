package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InformationPersonnelleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InformationPersonnelleDTO.class);
        InformationPersonnelleDTO informationPersonnelleDTO1 = new InformationPersonnelleDTO();
        informationPersonnelleDTO1.setId(1L);
        InformationPersonnelleDTO informationPersonnelleDTO2 = new InformationPersonnelleDTO();
        assertThat(informationPersonnelleDTO1).isNotEqualTo(informationPersonnelleDTO2);
        informationPersonnelleDTO2.setId(informationPersonnelleDTO1.getId());
        assertThat(informationPersonnelleDTO1).isEqualTo(informationPersonnelleDTO2);
        informationPersonnelleDTO2.setId(2L);
        assertThat(informationPersonnelleDTO1).isNotEqualTo(informationPersonnelleDTO2);
        informationPersonnelleDTO1.setId(null);
        assertThat(informationPersonnelleDTO1).isNotEqualTo(informationPersonnelleDTO2);
    }
}
