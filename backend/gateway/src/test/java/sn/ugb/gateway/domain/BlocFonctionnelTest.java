package sn.ugb.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gateway.domain.BlocFonctionnelTestSamples.*;
import static sn.ugb.gateway.domain.ServiceUserTestSamples.*;
import static sn.ugb.gateway.domain.UserProfileBlocFonctionnelTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class BlocFonctionnelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlocFonctionnel.class);
        BlocFonctionnel blocFonctionnel1 = getBlocFonctionnelSample1();
        BlocFonctionnel blocFonctionnel2 = new BlocFonctionnel();
        assertThat(blocFonctionnel1).isNotEqualTo(blocFonctionnel2);

        blocFonctionnel2.setId(blocFonctionnel1.getId());
        assertThat(blocFonctionnel1).isEqualTo(blocFonctionnel2);

        blocFonctionnel2 = getBlocFonctionnelSample2();
        assertThat(blocFonctionnel1).isNotEqualTo(blocFonctionnel2);
    }

    @Test
    void serviceTest() throws Exception {
        BlocFonctionnel blocFonctionnel = getBlocFonctionnelRandomSampleGenerator();
        ServiceUser serviceUserBack = getServiceUserRandomSampleGenerator();

        blocFonctionnel.setService(serviceUserBack);
        assertThat(blocFonctionnel.getService()).isEqualTo(serviceUserBack);

        blocFonctionnel.service(null);
        assertThat(blocFonctionnel.getService()).isNull();
    }

    @Test
    void userProfileBlocFonctionnelsTest() throws Exception {
        BlocFonctionnel blocFonctionnel = getBlocFonctionnelRandomSampleGenerator();
        UserProfileBlocFonctionnel userProfileBlocFonctionnelBack = getUserProfileBlocFonctionnelRandomSampleGenerator();

        blocFonctionnel.addUserProfileBlocFonctionnels(userProfileBlocFonctionnelBack);
        assertThat(blocFonctionnel.getUserProfileBlocFonctionnels()).containsOnly(userProfileBlocFonctionnelBack);
        assertThat(userProfileBlocFonctionnelBack.getBlocFonctionnel()).isEqualTo(blocFonctionnel);

        blocFonctionnel.removeUserProfileBlocFonctionnels(userProfileBlocFonctionnelBack);
        assertThat(blocFonctionnel.getUserProfileBlocFonctionnels()).doesNotContain(userProfileBlocFonctionnelBack);
        assertThat(userProfileBlocFonctionnelBack.getBlocFonctionnel()).isNull();

        blocFonctionnel.userProfileBlocFonctionnels(new HashSet<>(Set.of(userProfileBlocFonctionnelBack)));
        assertThat(blocFonctionnel.getUserProfileBlocFonctionnels()).containsOnly(userProfileBlocFonctionnelBack);
        assertThat(userProfileBlocFonctionnelBack.getBlocFonctionnel()).isEqualTo(blocFonctionnel);

        blocFonctionnel.setUserProfileBlocFonctionnels(new HashSet<>());
        assertThat(blocFonctionnel.getUserProfileBlocFonctionnels()).doesNotContain(userProfileBlocFonctionnelBack);
        assertThat(userProfileBlocFonctionnelBack.getBlocFonctionnel()).isNull();
    }
}
