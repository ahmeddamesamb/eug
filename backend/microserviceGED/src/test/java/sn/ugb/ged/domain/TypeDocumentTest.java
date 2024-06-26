package sn.ugb.ged.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.ged.domain.DocumentDelivreTestSamples.*;
import static sn.ugb.ged.domain.TypeDocumentTestSamples.*;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    void documentsDelivresTest() throws Exception {
        TypeDocument typeDocument = getTypeDocumentRandomSampleGenerator();
        DocumentDelivre documentDelivreBack = getDocumentDelivreRandomSampleGenerator();

        typeDocument.addDocumentsDelivres(documentDelivreBack);
        assertThat(typeDocument.getDocumentsDelivres()).containsOnly(documentDelivreBack);
        assertThat(documentDelivreBack.getTypeDocument()).isEqualTo(typeDocument);

        typeDocument.removeDocumentsDelivres(documentDelivreBack);
        assertThat(typeDocument.getDocumentsDelivres()).doesNotContain(documentDelivreBack);
        assertThat(documentDelivreBack.getTypeDocument()).isNull();

        typeDocument.documentsDelivres(new HashSet<>(Set.of(documentDelivreBack)));
        assertThat(typeDocument.getDocumentsDelivres()).containsOnly(documentDelivreBack);
        assertThat(documentDelivreBack.getTypeDocument()).isEqualTo(typeDocument);

        typeDocument.setDocumentsDelivres(new HashSet<>());
        assertThat(typeDocument.getDocumentsDelivres()).doesNotContain(documentDelivreBack);
        assertThat(documentDelivreBack.getTypeDocument()).isNull();
    }
}
