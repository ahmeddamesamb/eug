package sn.ugb.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gateway.domain.ProfilTestSamples.*;
import static sn.ugb.gateway.domain.UserProfileTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class ProfilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profil.class);
        Profil profil1 = getProfilSample1();
        Profil profil2 = new Profil();
        assertThat(profil1).isNotEqualTo(profil2);

        profil2.setId(profil1.getId());
        assertThat(profil1).isEqualTo(profil2);

        profil2 = getProfilSample2();
        assertThat(profil1).isNotEqualTo(profil2);
    }

    @Test
    void userProfilesTest() throws Exception {
        Profil profil = getProfilRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        profil.addUserProfiles(userProfileBack);
        assertThat(profil.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getProfil()).isEqualTo(profil);

        profil.removeUserProfiles(userProfileBack);
        assertThat(profil.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getProfil()).isNull();

        profil.userProfiles(new HashSet<>(Set.of(userProfileBack)));
        assertThat(profil.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getProfil()).isEqualTo(profil);

        profil.setUserProfiles(new HashSet<>());
        assertThat(profil.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getProfil()).isNull();
    }
}
