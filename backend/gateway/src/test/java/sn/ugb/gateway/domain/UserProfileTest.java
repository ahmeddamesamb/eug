package sn.ugb.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gateway.domain.InfosUserTestSamples.*;
import static sn.ugb.gateway.domain.ProfilTestSamples.*;
import static sn.ugb.gateway.domain.UserProfileBlocFonctionnelTestSamples.*;
import static sn.ugb.gateway.domain.UserProfileTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void profilTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Profil profilBack = getProfilRandomSampleGenerator();

        userProfile.setProfil(profilBack);
        assertThat(userProfile.getProfil()).isEqualTo(profilBack);

        userProfile.profil(null);
        assertThat(userProfile.getProfil()).isNull();
    }

    @Test
    void infoUserTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        InfosUser infosUserBack = getInfosUserRandomSampleGenerator();

        userProfile.setInfoUser(infosUserBack);
        assertThat(userProfile.getInfoUser()).isEqualTo(infosUserBack);

        userProfile.infoUser(null);
        assertThat(userProfile.getInfoUser()).isNull();
    }

    @Test
    void userProfileBlocFonctionnelsTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        UserProfileBlocFonctionnel userProfileBlocFonctionnelBack = getUserProfileBlocFonctionnelRandomSampleGenerator();

        userProfile.addUserProfileBlocFonctionnels(userProfileBlocFonctionnelBack);
        assertThat(userProfile.getUserProfileBlocFonctionnels()).containsOnly(userProfileBlocFonctionnelBack);
        assertThat(userProfileBlocFonctionnelBack.getUserProfil()).isEqualTo(userProfile);

        userProfile.removeUserProfileBlocFonctionnels(userProfileBlocFonctionnelBack);
        assertThat(userProfile.getUserProfileBlocFonctionnels()).doesNotContain(userProfileBlocFonctionnelBack);
        assertThat(userProfileBlocFonctionnelBack.getUserProfil()).isNull();

        userProfile.userProfileBlocFonctionnels(new HashSet<>(Set.of(userProfileBlocFonctionnelBack)));
        assertThat(userProfile.getUserProfileBlocFonctionnels()).containsOnly(userProfileBlocFonctionnelBack);
        assertThat(userProfileBlocFonctionnelBack.getUserProfil()).isEqualTo(userProfile);

        userProfile.setUserProfileBlocFonctionnels(new HashSet<>());
        assertThat(userProfile.getUserProfileBlocFonctionnels()).doesNotContain(userProfileBlocFonctionnelBack);
        assertThat(userProfileBlocFonctionnelBack.getUserProfil()).isNull();
    }
}
