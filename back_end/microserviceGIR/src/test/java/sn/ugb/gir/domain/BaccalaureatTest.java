package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.BaccalaureatTestSamples.*;
import static sn.ugb.gir.domain.EtudiantTestSamples.*;
import static sn.ugb.gir.domain.SerieTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class BaccalaureatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Baccalaureat.class);
        Baccalaureat baccalaureat1 = getBaccalaureatSample1();
        Baccalaureat baccalaureat2 = new Baccalaureat();
        assertThat(baccalaureat1).isNotEqualTo(baccalaureat2);

        baccalaureat2.setId(baccalaureat1.getId());
        assertThat(baccalaureat1).isEqualTo(baccalaureat2);

        baccalaureat2 = getBaccalaureatSample2();
        assertThat(baccalaureat1).isNotEqualTo(baccalaureat2);
    }

    @Test
    void etudiantTest() throws Exception {
        Baccalaureat baccalaureat = getBaccalaureatRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        baccalaureat.setEtudiant(etudiantBack);
        assertThat(baccalaureat.getEtudiant()).isEqualTo(etudiantBack);

        baccalaureat.etudiant(null);
        assertThat(baccalaureat.getEtudiant()).isNull();
    }

    @Test
    void serieTest() throws Exception {
        Baccalaureat baccalaureat = getBaccalaureatRandomSampleGenerator();
        Serie serieBack = getSerieRandomSampleGenerator();

        baccalaureat.setSerie(serieBack);
        assertThat(baccalaureat.getSerie()).isEqualTo(serieBack);

        baccalaureat.serie(null);
        assertThat(baccalaureat.getSerie()).isNull();
    }
}
