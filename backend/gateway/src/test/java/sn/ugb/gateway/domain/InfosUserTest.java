package sn.ugb.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gateway.domain.HistoriqueConnexionTestSamples.*;
import static sn.ugb.gateway.domain.InfoUserRessourceTestSamples.*;
import static sn.ugb.gateway.domain.InfosUserTestSamples.*;
import static sn.ugb.gateway.domain.UserProfileTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class InfosUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfosUser.class);
        InfosUser infosUser1 = getInfosUserSample1();
        InfosUser infosUser2 = new InfosUser();
        assertThat(infosUser1).isNotEqualTo(infosUser2);

        infosUser2.setId(infosUser1.getId());
        assertThat(infosUser1).isEqualTo(infosUser2);

        infosUser2 = getInfosUserSample2();
        assertThat(infosUser1).isNotEqualTo(infosUser2);
    }

    @Test
    void historiqueConnexionsTest() throws Exception {
        InfosUser infosUser = getInfosUserRandomSampleGenerator();
        HistoriqueConnexion historiqueConnexionBack = getHistoriqueConnexionRandomSampleGenerator();

        infosUser.addHistoriqueConnexions(historiqueConnexionBack);
        assertThat(infosUser.getHistoriqueConnexions()).containsOnly(historiqueConnexionBack);
        assertThat(historiqueConnexionBack.getInfoUser()).isEqualTo(infosUser);

        infosUser.removeHistoriqueConnexions(historiqueConnexionBack);
        assertThat(infosUser.getHistoriqueConnexions()).doesNotContain(historiqueConnexionBack);
        assertThat(historiqueConnexionBack.getInfoUser()).isNull();

        infosUser.historiqueConnexions(new HashSet<>(Set.of(historiqueConnexionBack)));
        assertThat(infosUser.getHistoriqueConnexions()).containsOnly(historiqueConnexionBack);
        assertThat(historiqueConnexionBack.getInfoUser()).isEqualTo(infosUser);

        infosUser.setHistoriqueConnexions(new HashSet<>());
        assertThat(infosUser.getHistoriqueConnexions()).doesNotContain(historiqueConnexionBack);
        assertThat(historiqueConnexionBack.getInfoUser()).isNull();
    }

    @Test
    void userProfilesTest() throws Exception {
        InfosUser infosUser = getInfosUserRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        infosUser.addUserProfiles(userProfileBack);
        assertThat(infosUser.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getInfoUser()).isEqualTo(infosUser);

        infosUser.removeUserProfiles(userProfileBack);
        assertThat(infosUser.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getInfoUser()).isNull();

        infosUser.userProfiles(new HashSet<>(Set.of(userProfileBack)));
        assertThat(infosUser.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getInfoUser()).isEqualTo(infosUser);

        infosUser.setUserProfiles(new HashSet<>());
        assertThat(infosUser.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getInfoUser()).isNull();
    }

    @Test
    void infoUserRessourcesTest() throws Exception {
        InfosUser infosUser = getInfosUserRandomSampleGenerator();
        InfoUserRessource infoUserRessourceBack = getInfoUserRessourceRandomSampleGenerator();

        infosUser.addInfoUserRessources(infoUserRessourceBack);
        assertThat(infosUser.getInfoUserRessources()).containsOnly(infoUserRessourceBack);
        assertThat(infoUserRessourceBack.getInfosUser()).isEqualTo(infosUser);

        infosUser.removeInfoUserRessources(infoUserRessourceBack);
        assertThat(infosUser.getInfoUserRessources()).doesNotContain(infoUserRessourceBack);
        assertThat(infoUserRessourceBack.getInfosUser()).isNull();

        infosUser.infoUserRessources(new HashSet<>(Set.of(infoUserRessourceBack)));
        assertThat(infosUser.getInfoUserRessources()).containsOnly(infoUserRessourceBack);
        assertThat(infoUserRessourceBack.getInfosUser()).isEqualTo(infosUser);

        infosUser.setInfoUserRessources(new HashSet<>());
        assertThat(infosUser.getInfoUserRessources()).doesNotContain(infoUserRessourceBack);
        assertThat(infoUserRessourceBack.getInfosUser()).isNull();
    }
}
