package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.DomaineTestSamples.*;
import static sn.ugb.gir.domain.MentionTestSamples.*;
import static sn.ugb.gir.domain.UfrTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DomaineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Domaine.class);
        Domaine domaine1 = getDomaineSample1();
        Domaine domaine2 = new Domaine();
        assertThat(domaine1).isNotEqualTo(domaine2);

        domaine2.setId(domaine1.getId());
        assertThat(domaine1).isEqualTo(domaine2);

        domaine2 = getDomaineSample2();
        assertThat(domaine1).isNotEqualTo(domaine2);
    }

    @Test
    void ufrTest() throws Exception {
        Domaine domaine = getDomaineRandomSampleGenerator();
        Ufr ufrBack = getUfrRandomSampleGenerator();

        domaine.addUfr(ufrBack);
        assertThat(domaine.getUfrs()).containsOnly(ufrBack);

        domaine.removeUfr(ufrBack);
        assertThat(domaine.getUfrs()).doesNotContain(ufrBack);

        domaine.ufrs(new HashSet<>(Set.of(ufrBack)));
        assertThat(domaine.getUfrs()).containsOnly(ufrBack);

        domaine.setUfrs(new HashSet<>());
        assertThat(domaine.getUfrs()).doesNotContain(ufrBack);
    }

    @Test
    void mentionsTest() throws Exception {
        Domaine domaine = getDomaineRandomSampleGenerator();
        Mention mentionBack = getMentionRandomSampleGenerator();

        domaine.addMentions(mentionBack);
        assertThat(domaine.getMentions()).containsOnly(mentionBack);
        assertThat(mentionBack.getDomaine()).isEqualTo(domaine);

        domaine.removeMentions(mentionBack);
        assertThat(domaine.getMentions()).doesNotContain(mentionBack);
        assertThat(mentionBack.getDomaine()).isNull();

        domaine.mentions(new HashSet<>(Set.of(mentionBack)));
        assertThat(domaine.getMentions()).containsOnly(mentionBack);
        assertThat(mentionBack.getDomaine()).isEqualTo(domaine);

        domaine.setMentions(new HashSet<>());
        assertThat(domaine.getMentions()).doesNotContain(mentionBack);
        assertThat(mentionBack.getDomaine()).isNull();
    }
}
