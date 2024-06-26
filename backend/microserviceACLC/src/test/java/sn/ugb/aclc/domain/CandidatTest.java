package sn.ugb.aclc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.aclc.domain.CandidatTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.aclc.web.rest.TestUtil;

class CandidatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Candidat.class);
        Candidat candidat1 = getCandidatSample1();
        Candidat candidat2 = new Candidat();
        assertThat(candidat1).isNotEqualTo(candidat2);

        candidat2.setId(candidat1.getId());
        assertThat(candidat1).isEqualTo(candidat2);

        candidat2 = getCandidatSample2();
        assertThat(candidat1).isNotEqualTo(candidat2);
    }
}
