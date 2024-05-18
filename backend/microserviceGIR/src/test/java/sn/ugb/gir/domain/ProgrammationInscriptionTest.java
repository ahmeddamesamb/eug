package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.AnneeAcademiqueTestSamples.*;
import static sn.ugb.gir.domain.CampagneTestSamples.*;
import static sn.ugb.gir.domain.FormationTestSamples.*;
import static sn.ugb.gir.domain.ProgrammationInscriptionTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class ProgrammationInscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgrammationInscription.class);
        ProgrammationInscription programmationInscription1 = getProgrammationInscriptionSample1();
        ProgrammationInscription programmationInscription2 = new ProgrammationInscription();
        assertThat(programmationInscription1).isNotEqualTo(programmationInscription2);

        programmationInscription2.setId(programmationInscription1.getId());
        assertThat(programmationInscription1).isEqualTo(programmationInscription2);

        programmationInscription2 = getProgrammationInscriptionSample2();
        assertThat(programmationInscription1).isNotEqualTo(programmationInscription2);
    }

    @Test
    void anneeAcademiqueTest() throws Exception {
        ProgrammationInscription programmationInscription = getProgrammationInscriptionRandomSampleGenerator();
        AnneeAcademique anneeAcademiqueBack = getAnneeAcademiqueRandomSampleGenerator();

        programmationInscription.setAnneeAcademique(anneeAcademiqueBack);
        assertThat(programmationInscription.getAnneeAcademique()).isEqualTo(anneeAcademiqueBack);

        programmationInscription.anneeAcademique(null);
        assertThat(programmationInscription.getAnneeAcademique()).isNull();
    }

    @Test
    void formationTest() throws Exception {
        ProgrammationInscription programmationInscription = getProgrammationInscriptionRandomSampleGenerator();
        Formation formationBack = getFormationRandomSampleGenerator();

        programmationInscription.setFormation(formationBack);
        assertThat(programmationInscription.getFormation()).isEqualTo(formationBack);

        programmationInscription.formation(null);
        assertThat(programmationInscription.getFormation()).isNull();
    }

    @Test
    void campagneTest() throws Exception {
        ProgrammationInscription programmationInscription = getProgrammationInscriptionRandomSampleGenerator();
        Campagne campagneBack = getCampagneRandomSampleGenerator();

        programmationInscription.setCampagne(campagneBack);
        assertThat(programmationInscription.getCampagne()).isEqualTo(campagneBack);

        programmationInscription.campagne(null);
        assertThat(programmationInscription.getCampagne()).isNull();
    }
}
