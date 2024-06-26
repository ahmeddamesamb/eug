package sn.ugb.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gateway.domain.InfoUserRessourceTestSamples.*;
import static sn.ugb.gateway.domain.InfosUserTestSamples.*;
import static sn.ugb.gateway.domain.RessourceTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class InfoUserRessourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfoUserRessource.class);
        InfoUserRessource infoUserRessource1 = getInfoUserRessourceSample1();
        InfoUserRessource infoUserRessource2 = new InfoUserRessource();
        assertThat(infoUserRessource1).isNotEqualTo(infoUserRessource2);

        infoUserRessource2.setId(infoUserRessource1.getId());
        assertThat(infoUserRessource1).isEqualTo(infoUserRessource2);

        infoUserRessource2 = getInfoUserRessourceSample2();
        assertThat(infoUserRessource1).isNotEqualTo(infoUserRessource2);
    }

    @Test
    void infosUserTest() throws Exception {
        InfoUserRessource infoUserRessource = getInfoUserRessourceRandomSampleGenerator();
        InfosUser infosUserBack = getInfosUserRandomSampleGenerator();

        infoUserRessource.setInfosUser(infosUserBack);
        assertThat(infoUserRessource.getInfosUser()).isEqualTo(infosUserBack);

        infoUserRessource.infosUser(null);
        assertThat(infoUserRessource.getInfosUser()).isNull();
    }

    @Test
    void ressourceTest() throws Exception {
        InfoUserRessource infoUserRessource = getInfoUserRessourceRandomSampleGenerator();
        Ressource ressourceBack = getRessourceRandomSampleGenerator();

        infoUserRessource.setRessource(ressourceBack);
        assertThat(infoUserRessource.getRessource()).isEqualTo(ressourceBack);

        infoUserRessource.ressource(null);
        assertThat(infoUserRessource.getRessource()).isNull();
    }
}
