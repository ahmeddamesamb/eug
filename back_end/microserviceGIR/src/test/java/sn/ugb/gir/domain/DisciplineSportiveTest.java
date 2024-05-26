package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DisciplineSportiveTestSamples.*;

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
}
