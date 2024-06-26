package sn.ugb.ged.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.ged.domain.DocumentDelivreTestSamples.*;
import static sn.ugb.ged.domain.TypeDocumentTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.ged.web.rest.TestUtil;

class DocumentDelivreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentDelivre.class);
        DocumentDelivre documentDelivre1 = getDocumentDelivreSample1();
        DocumentDelivre documentDelivre2 = new DocumentDelivre();
        assertThat(documentDelivre1).isNotEqualTo(documentDelivre2);

        documentDelivre2.setId(documentDelivre1.getId());
        assertThat(documentDelivre1).isEqualTo(documentDelivre2);

        documentDelivre2 = getDocumentDelivreSample2();
        assertThat(documentDelivre1).isNotEqualTo(documentDelivre2);
    }

    @Test
    void typeDocumentTest() throws Exception {
        DocumentDelivre documentDelivre = getDocumentDelivreRandomSampleGenerator();
        TypeDocument typeDocumentBack = getTypeDocumentRandomSampleGenerator();

        documentDelivre.setTypeDocument(typeDocumentBack);
        assertThat(documentDelivre.getTypeDocument()).isEqualTo(typeDocumentBack);

        documentDelivre.typeDocument(null);
        assertThat(documentDelivre.getTypeDocument()).isNull();
    }
}
