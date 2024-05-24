package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.AnneeAcademiqueTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class AnneeAcademiqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnneeAcademique.class);
        AnneeAcademique anneeAcademique1 = getAnneeAcademiqueSample1();
        AnneeAcademique anneeAcademique2 = new AnneeAcademique();
        assertThat(anneeAcademique1).isNotEqualTo(anneeAcademique2);

        anneeAcademique2.setId(anneeAcademique1.getId());
        assertThat(anneeAcademique1).isEqualTo(anneeAcademique2);

        anneeAcademique2 = getAnneeAcademiqueSample2();
        assertThat(anneeAcademique1).isNotEqualTo(anneeAcademique2);
    }
}
