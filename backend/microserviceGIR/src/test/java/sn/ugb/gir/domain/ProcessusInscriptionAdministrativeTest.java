package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.InscriptionAdministrativeTestSamples.*;
import static sn.ugb.gir.domain.ProcessusInscriptionAdministrativeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class ProcessusInscriptionAdministrativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessusInscriptionAdministrative.class);
        ProcessusInscriptionAdministrative processusInscriptionAdministrative1 = getProcessusInscriptionAdministrativeSample1();
        ProcessusInscriptionAdministrative processusInscriptionAdministrative2 = new ProcessusInscriptionAdministrative();
        assertThat(processusInscriptionAdministrative1).isNotEqualTo(processusInscriptionAdministrative2);

        processusInscriptionAdministrative2.setId(processusInscriptionAdministrative1.getId());
        assertThat(processusInscriptionAdministrative1).isEqualTo(processusInscriptionAdministrative2);

        processusInscriptionAdministrative2 = getProcessusInscriptionAdministrativeSample2();
        assertThat(processusInscriptionAdministrative1).isNotEqualTo(processusInscriptionAdministrative2);
    }

    @Test
    void inscriptionAdministrativeTest() throws Exception {
        ProcessusInscriptionAdministrative processusInscriptionAdministrative =
            getProcessusInscriptionAdministrativeRandomSampleGenerator();
        InscriptionAdministrative inscriptionAdministrativeBack = getInscriptionAdministrativeRandomSampleGenerator();

        processusInscriptionAdministrative.setInscriptionAdministrative(inscriptionAdministrativeBack);
        assertThat(processusInscriptionAdministrative.getInscriptionAdministrative()).isEqualTo(inscriptionAdministrativeBack);

        processusInscriptionAdministrative.inscriptionAdministrative(null);
        assertThat(processusInscriptionAdministrative.getInscriptionAdministrative()).isNull();
    }
}
