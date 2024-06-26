package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.CampagneTestSamples.*;
import static sn.ugb.gir.domain.ProgrammationInscriptionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class CampagneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campagne.class);
        Campagne campagne1 = getCampagneSample1();
        Campagne campagne2 = new Campagne();
        assertThat(campagne1).isNotEqualTo(campagne2);

        campagne2.setId(campagne1.getId());
        assertThat(campagne1).isEqualTo(campagne2);

        campagne2 = getCampagneSample2();
        assertThat(campagne1).isNotEqualTo(campagne2);
    }

    @Test
    void programmationInscriptionsTest() throws Exception {
        Campagne campagne = getCampagneRandomSampleGenerator();
        ProgrammationInscription programmationInscriptionBack = getProgrammationInscriptionRandomSampleGenerator();

        campagne.addProgrammationInscriptions(programmationInscriptionBack);
        assertThat(campagne.getProgrammationInscriptions()).containsOnly(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getCampagne()).isEqualTo(campagne);

        campagne.removeProgrammationInscriptions(programmationInscriptionBack);
        assertThat(campagne.getProgrammationInscriptions()).doesNotContain(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getCampagne()).isNull();

        campagne.programmationInscriptions(new HashSet<>(Set.of(programmationInscriptionBack)));
        assertThat(campagne.getProgrammationInscriptions()).containsOnly(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getCampagne()).isEqualTo(campagne);

        campagne.setProgrammationInscriptions(new HashSet<>());
        assertThat(campagne.getProgrammationInscriptions()).doesNotContain(programmationInscriptionBack);
        assertThat(programmationInscriptionBack.getCampagne()).isNull();
    }
}
