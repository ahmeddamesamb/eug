package sn.ugb.ged.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.ged.domain.TypeDocumentTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.ged.web.rest.TestUtil;

class TypeDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDocument.class);
        TypeDocument typeDocument1 = getTypeDocumentSample1();
        TypeDocument typeDocument2 = new TypeDocument();
        assertThat(typeDocument1).isNotEqualTo(typeDocument2);

        typeDocument2.setId(typeDocument1.getId());
        assertThat(typeDocument1).isEqualTo(typeDocument2);

        typeDocument2 = getTypeDocumentSample2();
        assertThat(typeDocument1).isNotEqualTo(typeDocument2);
    }
}
