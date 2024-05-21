package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.EtudiantTestSamples.*;
import static sn.ugb.gir.domain.InformationPersonnelleTestSamples.*;
import static sn.ugb.gir.domain.TypeBourseTestSamples.*;
import static sn.ugb.gir.domain.TypeHandicapTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class InformationPersonnelleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InformationPersonnelle.class);
        InformationPersonnelle informationPersonnelle1 = getInformationPersonnelleSample1();
        InformationPersonnelle informationPersonnelle2 = new InformationPersonnelle();
        assertThat(informationPersonnelle1).isNotEqualTo(informationPersonnelle2);

        informationPersonnelle2.setId(informationPersonnelle1.getId());
        assertThat(informationPersonnelle1).isEqualTo(informationPersonnelle2);

        informationPersonnelle2 = getInformationPersonnelleSample2();
        assertThat(informationPersonnelle1).isNotEqualTo(informationPersonnelle2);
    }

    @Test
    void etudiantTest() throws Exception {
        InformationPersonnelle informationPersonnelle = getInformationPersonnelleRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        informationPersonnelle.setEtudiant(etudiantBack);
        assertThat(informationPersonnelle.getEtudiant()).isEqualTo(etudiantBack);

        informationPersonnelle.etudiant(null);
        assertThat(informationPersonnelle.getEtudiant()).isNull();
    }

    @Test
    void typeHadiqueTest() throws Exception {
        InformationPersonnelle informationPersonnelle = getInformationPersonnelleRandomSampleGenerator();
        TypeHandicap typeHandicapBack = getTypeHandicapRandomSampleGenerator();

        informationPersonnelle.setTypeHadique(typeHandicapBack);
        assertThat(informationPersonnelle.getTypeHadique()).isEqualTo(typeHandicapBack);

        informationPersonnelle.typeHadique(null);
        assertThat(informationPersonnelle.getTypeHadique()).isNull();
    }

    @Test
    void typeBourseTest() throws Exception {
        InformationPersonnelle informationPersonnelle = getInformationPersonnelleRandomSampleGenerator();
        TypeBourse typeBourseBack = getTypeBourseRandomSampleGenerator();

        informationPersonnelle.setTypeBourse(typeBourseBack);
        assertThat(informationPersonnelle.getTypeBourse()).isEqualTo(typeBourseBack);

        informationPersonnelle.typeBourse(null);
        assertThat(informationPersonnelle.getTypeBourse()).isNull();
    }
}
