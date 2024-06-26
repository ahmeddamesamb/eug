package sn.ugb.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gateway.domain.BlocFonctionnelTestSamples.*;
import static sn.ugb.gateway.domain.UserProfileBlocFonctionnelTestSamples.*;
import static sn.ugb.gateway.domain.UserProfileTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class UserProfileBlocFonctionnelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfileBlocFonctionnel.class);
        UserProfileBlocFonctionnel userProfileBlocFonctionnel1 = getUserProfileBlocFonctionnelSample1();
        UserProfileBlocFonctionnel userProfileBlocFonctionnel2 = new UserProfileBlocFonctionnel();
        assertThat(userProfileBlocFonctionnel1).isNotEqualTo(userProfileBlocFonctionnel2);

        userProfileBlocFonctionnel2.setId(userProfileBlocFonctionnel1.getId());
        assertThat(userProfileBlocFonctionnel1).isEqualTo(userProfileBlocFonctionnel2);

        userProfileBlocFonctionnel2 = getUserProfileBlocFonctionnelSample2();
        assertThat(userProfileBlocFonctionnel1).isNotEqualTo(userProfileBlocFonctionnel2);
    }

    @Test
    void userProfilTest() throws Exception {
        UserProfileBlocFonctionnel userProfileBlocFonctionnel = getUserProfileBlocFonctionnelRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userProfileBlocFonctionnel.setUserProfil(userProfileBack);
        assertThat(userProfileBlocFonctionnel.getUserProfil()).isEqualTo(userProfileBack);

        userProfileBlocFonctionnel.userProfil(null);
        assertThat(userProfileBlocFonctionnel.getUserProfil()).isNull();
    }

    @Test
    void blocFonctionnelTest() throws Exception {
        UserProfileBlocFonctionnel userProfileBlocFonctionnel = getUserProfileBlocFonctionnelRandomSampleGenerator();
        BlocFonctionnel blocFonctionnelBack = getBlocFonctionnelRandomSampleGenerator();

        userProfileBlocFonctionnel.setBlocFonctionnel(blocFonctionnelBack);
        assertThat(userProfileBlocFonctionnel.getBlocFonctionnel()).isEqualTo(blocFonctionnelBack);

        userProfileBlocFonctionnel.blocFonctionnel(null);
        assertThat(userProfileBlocFonctionnel.getBlocFonctionnel()).isNull();
    }
}
