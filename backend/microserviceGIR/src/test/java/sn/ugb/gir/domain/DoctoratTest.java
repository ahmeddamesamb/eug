package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DoctoratTestSamples.*;
import static sn.ugb.gir.domain.InscriptionDoctoratTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DoctoratTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doctorat.class);
        Doctorat doctorat1 = getDoctoratSample1();
        Doctorat doctorat2 = new Doctorat();
        assertThat(doctorat1).isNotEqualTo(doctorat2);

        doctorat2.setId(doctorat1.getId());
        assertThat(doctorat1).isEqualTo(doctorat2);

        doctorat2 = getDoctoratSample2();
        assertThat(doctorat1).isNotEqualTo(doctorat2);
    }

    @Test
    void inscriptionDoctoratsTest() throws Exception {
        Doctorat doctorat = getDoctoratRandomSampleGenerator();
        InscriptionDoctorat inscriptionDoctoratBack = getInscriptionDoctoratRandomSampleGenerator();

        doctorat.addInscriptionDoctorats(inscriptionDoctoratBack);
        assertThat(doctorat.getInscriptionDoctorats()).containsOnly(inscriptionDoctoratBack);
        assertThat(inscriptionDoctoratBack.getDoctorat()).isEqualTo(doctorat);

        doctorat.removeInscriptionDoctorats(inscriptionDoctoratBack);
        assertThat(doctorat.getInscriptionDoctorats()).doesNotContain(inscriptionDoctoratBack);
        assertThat(inscriptionDoctoratBack.getDoctorat()).isNull();

        doctorat.inscriptionDoctorats(new HashSet<>(Set.of(inscriptionDoctoratBack)));
        assertThat(doctorat.getInscriptionDoctorats()).containsOnly(inscriptionDoctoratBack);
        assertThat(inscriptionDoctoratBack.getDoctorat()).isEqualTo(doctorat);

        doctorat.setInscriptionDoctorats(new HashSet<>());
        assertThat(doctorat.getInscriptionDoctorats()).doesNotContain(inscriptionDoctoratBack);
        assertThat(inscriptionDoctoratBack.getDoctorat()).isNull();
    }
}
