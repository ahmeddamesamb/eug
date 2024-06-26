package sn.ugb.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class BlocFonctionnelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlocFonctionnelDTO.class);
        BlocFonctionnelDTO blocFonctionnelDTO1 = new BlocFonctionnelDTO();
        blocFonctionnelDTO1.setId(1L);
        BlocFonctionnelDTO blocFonctionnelDTO2 = new BlocFonctionnelDTO();
        assertThat(blocFonctionnelDTO1).isNotEqualTo(blocFonctionnelDTO2);
        blocFonctionnelDTO2.setId(blocFonctionnelDTO1.getId());
        assertThat(blocFonctionnelDTO1).isEqualTo(blocFonctionnelDTO2);
        blocFonctionnelDTO2.setId(2L);
        assertThat(blocFonctionnelDTO1).isNotEqualTo(blocFonctionnelDTO2);
        blocFonctionnelDTO1.setId(null);
        assertThat(blocFonctionnelDTO1).isNotEqualTo(blocFonctionnelDTO2);
    }
}
