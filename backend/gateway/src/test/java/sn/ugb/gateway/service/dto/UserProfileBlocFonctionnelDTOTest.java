package sn.ugb.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class UserProfileBlocFonctionnelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfileBlocFonctionnelDTO.class);
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO1 = new UserProfileBlocFonctionnelDTO();
        userProfileBlocFonctionnelDTO1.setId(1L);
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO2 = new UserProfileBlocFonctionnelDTO();
        assertThat(userProfileBlocFonctionnelDTO1).isNotEqualTo(userProfileBlocFonctionnelDTO2);
        userProfileBlocFonctionnelDTO2.setId(userProfileBlocFonctionnelDTO1.getId());
        assertThat(userProfileBlocFonctionnelDTO1).isEqualTo(userProfileBlocFonctionnelDTO2);
        userProfileBlocFonctionnelDTO2.setId(2L);
        assertThat(userProfileBlocFonctionnelDTO1).isNotEqualTo(userProfileBlocFonctionnelDTO2);
        userProfileBlocFonctionnelDTO1.setId(null);
        assertThat(userProfileBlocFonctionnelDTO1).isNotEqualTo(userProfileBlocFonctionnelDTO2);
    }
}
