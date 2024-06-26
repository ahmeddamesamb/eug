package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DepartementTestSamples.*;
import static sn.ugb.gir.domain.UfrTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DepartementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departement.class);
        Departement departement1 = getDepartementSample1();
        Departement departement2 = new Departement();
        assertThat(departement1).isNotEqualTo(departement2);

        departement2.setId(departement1.getId());
        assertThat(departement1).isEqualTo(departement2);

        departement2 = getDepartementSample2();
        assertThat(departement1).isNotEqualTo(departement2);
    }

    @Test
    void ufrTest() throws Exception {
        Departement departement = getDepartementRandomSampleGenerator();
        Ufr ufrBack = getUfrRandomSampleGenerator();

        departement.setUfr(ufrBack);
        assertThat(departement.getUfr()).isEqualTo(ufrBack);

        departement.ufr(null);
        assertThat(departement.getUfr()).isNull();
    }
}
