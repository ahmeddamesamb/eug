package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.FormationTestSamples.*;
import static sn.ugb.gir.domain.MentionTestSamples.*;
import static sn.ugb.gir.domain.SpecialiteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class SpecialiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specialite.class);
        Specialite specialite1 = getSpecialiteSample1();
        Specialite specialite2 = new Specialite();
        assertThat(specialite1).isNotEqualTo(specialite2);

        specialite2.setId(specialite1.getId());
        assertThat(specialite1).isEqualTo(specialite2);

        specialite2 = getSpecialiteSample2();
        assertThat(specialite1).isNotEqualTo(specialite2);
    }

    @Test
    void mentionTest() throws Exception {
        Specialite specialite = getSpecialiteRandomSampleGenerator();
        Mention mentionBack = getMentionRandomSampleGenerator();

        specialite.setMention(mentionBack);
        assertThat(specialite.getMention()).isEqualTo(mentionBack);

        specialite.mention(null);
        assertThat(specialite.getMention()).isNull();
    }

    @Test
    void formationsTest() throws Exception {
        Specialite specialite = getSpecialiteRandomSampleGenerator();
        Formation formationBack = getFormationRandomSampleGenerator();

        specialite.addFormations(formationBack);
        assertThat(specialite.getFormations()).containsOnly(formationBack);
        assertThat(formationBack.getSpecialite()).isEqualTo(specialite);

        specialite.removeFormations(formationBack);
        assertThat(specialite.getFormations()).doesNotContain(formationBack);
        assertThat(formationBack.getSpecialite()).isNull();

        specialite.formations(new HashSet<>(Set.of(formationBack)));
        assertThat(specialite.getFormations()).containsOnly(formationBack);
        assertThat(formationBack.getSpecialite()).isEqualTo(specialite);

        specialite.setFormations(new HashSet<>());
        assertThat(specialite.getFormations()).doesNotContain(formationBack);
        assertThat(formationBack.getSpecialite()).isNull();
    }
}
