package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DisciplineSportiveEtudiantTestSamples.*;
import static sn.ugb.gir.domain.DisciplineSportiveTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DisciplineSportiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisciplineSportive.class);
        DisciplineSportive disciplineSportive1 = getDisciplineSportiveSample1();
        DisciplineSportive disciplineSportive2 = new DisciplineSportive();
        assertThat(disciplineSportive1).isNotEqualTo(disciplineSportive2);

        disciplineSportive2.setId(disciplineSportive1.getId());
        assertThat(disciplineSportive1).isEqualTo(disciplineSportive2);

        disciplineSportive2 = getDisciplineSportiveSample2();
        assertThat(disciplineSportive1).isNotEqualTo(disciplineSportive2);
    }

    @Test
    void disciplineSportiveEtudiantsTest() throws Exception {
        DisciplineSportive disciplineSportive = getDisciplineSportiveRandomSampleGenerator();
        DisciplineSportiveEtudiant disciplineSportiveEtudiantBack = getDisciplineSportiveEtudiantRandomSampleGenerator();

        disciplineSportive.addDisciplineSportiveEtudiants(disciplineSportiveEtudiantBack);
        assertThat(disciplineSportive.getDisciplineSportiveEtudiants()).containsOnly(disciplineSportiveEtudiantBack);
        assertThat(disciplineSportiveEtudiantBack.getDisciplineSportive()).isEqualTo(disciplineSportive);

        disciplineSportive.removeDisciplineSportiveEtudiants(disciplineSportiveEtudiantBack);
        assertThat(disciplineSportive.getDisciplineSportiveEtudiants()).doesNotContain(disciplineSportiveEtudiantBack);
        assertThat(disciplineSportiveEtudiantBack.getDisciplineSportive()).isNull();

        disciplineSportive.disciplineSportiveEtudiants(new HashSet<>(Set.of(disciplineSportiveEtudiantBack)));
        assertThat(disciplineSportive.getDisciplineSportiveEtudiants()).containsOnly(disciplineSportiveEtudiantBack);
        assertThat(disciplineSportiveEtudiantBack.getDisciplineSportive()).isEqualTo(disciplineSportive);

        disciplineSportive.setDisciplineSportiveEtudiants(new HashSet<>());
        assertThat(disciplineSportive.getDisciplineSportiveEtudiants()).doesNotContain(disciplineSportiveEtudiantBack);
        assertThat(disciplineSportiveEtudiantBack.getDisciplineSportive()).isNull();
    }
}
