package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.BaccalaureatTestSamples.*;
import static sn.ugb.gir.domain.EtudiantTestSamples.*;
import static sn.ugb.gir.domain.InformationPersonnelleTestSamples.*;
import static sn.ugb.gir.domain.LyceeTestSamples.*;
import static sn.ugb.gir.domain.RegionTestSamples.*;
import static sn.ugb.gir.domain.TypeSelectionTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class EtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etudiant.class);
        Etudiant etudiant1 = getEtudiantSample1();
        Etudiant etudiant2 = new Etudiant();
        assertThat(etudiant1).isNotEqualTo(etudiant2);

        etudiant2.setId(etudiant1.getId());
        assertThat(etudiant1).isEqualTo(etudiant2);

        etudiant2 = getEtudiantSample2();
        assertThat(etudiant1).isNotEqualTo(etudiant2);
    }

    @Test
    void regionTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Region regionBack = getRegionRandomSampleGenerator();

        etudiant.setRegion(regionBack);
        assertThat(etudiant.getRegion()).isEqualTo(regionBack);

        etudiant.region(null);
        assertThat(etudiant.getRegion()).isNull();
    }

    @Test
    void typeSelectionTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        TypeSelection typeSelectionBack = getTypeSelectionRandomSampleGenerator();

        etudiant.setTypeSelection(typeSelectionBack);
        assertThat(etudiant.getTypeSelection()).isEqualTo(typeSelectionBack);

        etudiant.typeSelection(null);
        assertThat(etudiant.getTypeSelection()).isNull();
    }

    @Test
    void lyceeTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Lycee lyceeBack = getLyceeRandomSampleGenerator();

        etudiant.setLycee(lyceeBack);
        assertThat(etudiant.getLycee()).isEqualTo(lyceeBack);

        etudiant.lycee(null);
        assertThat(etudiant.getLycee()).isNull();
    }

    @Test
    void informationPersonnelleTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        InformationPersonnelle informationPersonnelleBack = getInformationPersonnelleRandomSampleGenerator();

        etudiant.setInformationPersonnelle(informationPersonnelleBack);
        assertThat(etudiant.getInformationPersonnelle()).isEqualTo(informationPersonnelleBack);
        assertThat(informationPersonnelleBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.informationPersonnelle(null);
        assertThat(etudiant.getInformationPersonnelle()).isNull();
        assertThat(informationPersonnelleBack.getEtudiant()).isNull();
    }

    @Test
    void baccalaureatTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Baccalaureat baccalaureatBack = getBaccalaureatRandomSampleGenerator();

        etudiant.setBaccalaureat(baccalaureatBack);
        assertThat(etudiant.getBaccalaureat()).isEqualTo(baccalaureatBack);
        assertThat(baccalaureatBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.baccalaureat(null);
        assertThat(etudiant.getBaccalaureat()).isNull();
        assertThat(baccalaureatBack.getEtudiant()).isNull();
    }
}
