package sn.ugb.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gateway.domain.HistoriqueConnexionTestSamples.*;
import static sn.ugb.gateway.domain.InfosUserTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class HistoriqueConnexionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueConnexion.class);
        HistoriqueConnexion historiqueConnexion1 = getHistoriqueConnexionSample1();
        HistoriqueConnexion historiqueConnexion2 = new HistoriqueConnexion();
        assertThat(historiqueConnexion1).isNotEqualTo(historiqueConnexion2);

        historiqueConnexion2.setId(historiqueConnexion1.getId());
        assertThat(historiqueConnexion1).isEqualTo(historiqueConnexion2);

        historiqueConnexion2 = getHistoriqueConnexionSample2();
        assertThat(historiqueConnexion1).isNotEqualTo(historiqueConnexion2);
    }

    @Test
    void infoUserTest() throws Exception {
        HistoriqueConnexion historiqueConnexion = getHistoriqueConnexionRandomSampleGenerator();
        InfosUser infosUserBack = getInfosUserRandomSampleGenerator();

        historiqueConnexion.setInfoUser(infosUserBack);
        assertThat(historiqueConnexion.getInfoUser()).isEqualTo(infosUserBack);

        historiqueConnexion.infoUser(null);
        assertThat(historiqueConnexion.getInfoUser()).isNull();
    }
}
