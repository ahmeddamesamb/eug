package sn.ugb.aide.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.aide.domain.RessourceTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.aide.web.rest.TestUtil;

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
}
