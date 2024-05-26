package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DomaineTestSamples.*;
import static sn.ugb.gir.domain.MentionTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class MentionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mention.class);
        Mention mention1 = getMentionSample1();
        Mention mention2 = new Mention();
        assertThat(mention1).isNotEqualTo(mention2);

        mention2.setId(mention1.getId());
        assertThat(mention1).isEqualTo(mention2);

        mention2 = getMentionSample2();
        assertThat(mention1).isNotEqualTo(mention2);
    }

    @Test
    void domaineTest() throws Exception {
        Mention mention = getMentionRandomSampleGenerator();
        Domaine domaineBack = getDomaineRandomSampleGenerator();

        mention.setDomaine(domaineBack);
        assertThat(mention.getDomaine()).isEqualTo(domaineBack);

        mention.domaine(null);
        assertThat(mention.getDomaine()).isNull();
    }
}
