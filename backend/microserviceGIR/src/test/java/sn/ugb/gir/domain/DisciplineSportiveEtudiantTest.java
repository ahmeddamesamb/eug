package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DisciplineSportiveEtudiantTestSamples.*;
import static sn.ugb.gir.domain.DisciplineSportiveTestSamples.*;
import static sn.ugb.gir.domain.EtudiantTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DisciplineSportiveEtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisciplineSportiveEtudiant.class);
        DisciplineSportiveEtudiant disciplineSportiveEtudiant1 = getDisciplineSportiveEtudiantSample1();
        DisciplineSportiveEtudiant disciplineSportiveEtudiant2 = new DisciplineSportiveEtudiant();
        assertThat(disciplineSportiveEtudiant1).isNotEqualTo(disciplineSportiveEtudiant2);

        disciplineSportiveEtudiant2.setId(disciplineSportiveEtudiant1.getId());
        assertThat(disciplineSportiveEtudiant1).isEqualTo(disciplineSportiveEtudiant2);

        disciplineSportiveEtudiant2 = getDisciplineSportiveEtudiantSample2();
        assertThat(disciplineSportiveEtudiant1).isNotEqualTo(disciplineSportiveEtudiant2);
    }

    @Test
    void disciplineSportiveTest() throws Exception {
        DisciplineSportiveEtudiant disciplineSportiveEtudiant = getDisciplineSportiveEtudiantRandomSampleGenerator();
        DisciplineSportive disciplineSportiveBack = getDisciplineSportiveRandomSampleGenerator();

        disciplineSportiveEtudiant.setDisciplineSportive(disciplineSportiveBack);
        assertThat(disciplineSportiveEtudiant.getDisciplineSportive()).isEqualTo(disciplineSportiveBack);

        disciplineSportiveEtudiant.disciplineSportive(null);
        assertThat(disciplineSportiveEtudiant.getDisciplineSportive()).isNull();
    }

    @Test
    void etudiantTest() throws Exception {
        DisciplineSportiveEtudiant disciplineSportiveEtudiant = getDisciplineSportiveEtudiantRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        disciplineSportiveEtudiant.setEtudiant(etudiantBack);
        assertThat(disciplineSportiveEtudiant.getEtudiant()).isEqualTo(etudiantBack);

        disciplineSportiveEtudiant.etudiant(null);
        assertThat(disciplineSportiveEtudiant.getEtudiant()).isNull();
    }
}
