package sn.ugb.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.user.web.rest.TestUtil;

class UtilisateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilisateurDTO.class);
        UtilisateurDTO utilisateurDTO1 = new UtilisateurDTO();
        utilisateurDTO1.setId(1L);
        UtilisateurDTO utilisateurDTO2 = new UtilisateurDTO();
        assertThat(utilisateurDTO1).isNotEqualTo(utilisateurDTO2);
        utilisateurDTO2.setId(utilisateurDTO1.getId());
        assertThat(utilisateurDTO1).isEqualTo(utilisateurDTO2);
        utilisateurDTO2.setId(2L);
        assertThat(utilisateurDTO1).isNotEqualTo(utilisateurDTO2);
        utilisateurDTO1.setId(null);
        assertThat(utilisateurDTO1).isNotEqualTo(utilisateurDTO2);
    }
}
