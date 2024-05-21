package sn.ugb.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.user.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.user.web.rest.TestUtil;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }
}
