package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.InscriptionAdministrativeTestSamples.*;
import static sn.ugb.gir.domain.ProcessusDinscriptionAdministrativeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class ProcessusDinscriptionAdministrativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessusDinscriptionAdministrative.class);
        ProcessusDinscriptionAdministrative processusDinscriptionAdministrative1 = getProcessusDinscriptionAdministrativeSample1();
        ProcessusDinscriptionAdministrative processusDinscriptionAdministrative2 = new ProcessusDinscriptionAdministrative();
        assertThat(processusDinscriptionAdministrative1).isNotEqualTo(processusDinscriptionAdministrative2);

        processusDinscriptionAdministrative2.setId(processusDinscriptionAdministrative1.getId());
        assertThat(processusDinscriptionAdministrative1).isEqualTo(processusDinscriptionAdministrative2);

        processusDinscriptionAdministrative2 = getProcessusDinscriptionAdministrativeSample2();
        assertThat(processusDinscriptionAdministrative1).isNotEqualTo(processusDinscriptionAdministrative2);
    }

    @Test
    void inscriptionAdministrativeTest() throws Exception {
        ProcessusDinscriptionAdministrative processusDinscriptionAdministrative =
            getProcessusDinscriptionAdministrativeRandomSampleGenerator();
        InscriptionAdministrative inscriptionAdministrativeBack = getInscriptionAdministrativeRandomSampleGenerator();

        processusDinscriptionAdministrative.setInscriptionAdministrative(inscriptionAdministrativeBack);
        assertThat(processusDinscriptionAdministrative.getInscriptionAdministrative()).isEqualTo(inscriptionAdministrativeBack);

        processusDinscriptionAdministrative.inscriptionAdministrative(null);
        assertThat(processusDinscriptionAdministrative.getInscriptionAdministrative()).isNull();
    }
}
