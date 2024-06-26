package sn.ugb.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gateway.domain.BlocFonctionnelTestSamples.*;
import static sn.ugb.gateway.domain.ServiceUserTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class ServiceUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceUser.class);
        ServiceUser serviceUser1 = getServiceUserSample1();
        ServiceUser serviceUser2 = new ServiceUser();
        assertThat(serviceUser1).isNotEqualTo(serviceUser2);

        serviceUser2.setId(serviceUser1.getId());
        assertThat(serviceUser1).isEqualTo(serviceUser2);

        serviceUser2 = getServiceUserSample2();
        assertThat(serviceUser1).isNotEqualTo(serviceUser2);
    }

    @Test
    void blocFonctionnelsTest() throws Exception {
        ServiceUser serviceUser = getServiceUserRandomSampleGenerator();
        BlocFonctionnel blocFonctionnelBack = getBlocFonctionnelRandomSampleGenerator();

        serviceUser.addBlocFonctionnels(blocFonctionnelBack);
        assertThat(serviceUser.getBlocFonctionnels()).containsOnly(blocFonctionnelBack);
        assertThat(blocFonctionnelBack.getService()).isEqualTo(serviceUser);

        serviceUser.removeBlocFonctionnels(blocFonctionnelBack);
        assertThat(serviceUser.getBlocFonctionnels()).doesNotContain(blocFonctionnelBack);
        assertThat(blocFonctionnelBack.getService()).isNull();

        serviceUser.blocFonctionnels(new HashSet<>(Set.of(blocFonctionnelBack)));
        assertThat(serviceUser.getBlocFonctionnels()).containsOnly(blocFonctionnelBack);
        assertThat(blocFonctionnelBack.getService()).isEqualTo(serviceUser);

        serviceUser.setBlocFonctionnels(new HashSet<>());
        assertThat(serviceUser.getBlocFonctionnels()).doesNotContain(blocFonctionnelBack);
        assertThat(blocFonctionnelBack.getService()).isNull();
    }
}
