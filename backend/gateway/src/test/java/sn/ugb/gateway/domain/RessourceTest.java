package sn.ugb.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gateway.domain.InfoUserRessourceTestSamples.*;
import static sn.ugb.gateway.domain.RessourceTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gateway.web.rest.TestUtil;

class RessourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ressource.class);
        Ressource ressource1 = getRessourceSample1();
        Ressource ressource2 = new Ressource();
        assertThat(ressource1).isNotEqualTo(ressource2);

        ressource2.setId(ressource1.getId());
        assertThat(ressource1).isEqualTo(ressource2);

        ressource2 = getRessourceSample2();
        assertThat(ressource1).isNotEqualTo(ressource2);
    }

    @Test
    void infoUserRessourcesTest() throws Exception {
        Ressource ressource = getRessourceRandomSampleGenerator();
        InfoUserRessource infoUserRessourceBack = getInfoUserRessourceRandomSampleGenerator();

        ressource.addInfoUserRessources(infoUserRessourceBack);
        assertThat(ressource.getInfoUserRessources()).containsOnly(infoUserRessourceBack);
        assertThat(infoUserRessourceBack.getRessource()).isEqualTo(ressource);

        ressource.removeInfoUserRessources(infoUserRessourceBack);
        assertThat(ressource.getInfoUserRessources()).doesNotContain(infoUserRessourceBack);
        assertThat(infoUserRessourceBack.getRessource()).isNull();

        ressource.infoUserRessources(new HashSet<>(Set.of(infoUserRessourceBack)));
        assertThat(ressource.getInfoUserRessources()).containsOnly(infoUserRessourceBack);
        assertThat(infoUserRessourceBack.getRessource()).isEqualTo(ressource);

        ressource.setInfoUserRessources(new HashSet<>());
        assertThat(ressource.getInfoUserRessources()).doesNotContain(infoUserRessourceBack);
        assertThat(infoUserRessourceBack.getRessource()).isNull();
    }
}
