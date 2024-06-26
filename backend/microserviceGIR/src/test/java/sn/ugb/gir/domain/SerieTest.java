package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.BaccalaureatTestSamples.*;
import static sn.ugb.gir.domain.SerieTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class SerieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Serie.class);
        Serie serie1 = getSerieSample1();
        Serie serie2 = new Serie();
        assertThat(serie1).isNotEqualTo(serie2);

        serie2.setId(serie1.getId());
        assertThat(serie1).isEqualTo(serie2);

        serie2 = getSerieSample2();
        assertThat(serie1).isNotEqualTo(serie2);
    }

    @Test
    void baccalaureatsTest() throws Exception {
        Serie serie = getSerieRandomSampleGenerator();
        Baccalaureat baccalaureatBack = getBaccalaureatRandomSampleGenerator();

        serie.addBaccalaureats(baccalaureatBack);
        assertThat(serie.getBaccalaureats()).containsOnly(baccalaureatBack);
        assertThat(baccalaureatBack.getSerie()).isEqualTo(serie);

        serie.removeBaccalaureats(baccalaureatBack);
        assertThat(serie.getBaccalaureats()).doesNotContain(baccalaureatBack);
        assertThat(baccalaureatBack.getSerie()).isNull();

        serie.baccalaureats(new HashSet<>(Set.of(baccalaureatBack)));
        assertThat(serie.getBaccalaureats()).containsOnly(baccalaureatBack);
        assertThat(baccalaureatBack.getSerie()).isEqualTo(serie);

        serie.setBaccalaureats(new HashSet<>());
        assertThat(serie.getBaccalaureats()).doesNotContain(baccalaureatBack);
        assertThat(baccalaureatBack.getSerie()).isNull();
    }
}
