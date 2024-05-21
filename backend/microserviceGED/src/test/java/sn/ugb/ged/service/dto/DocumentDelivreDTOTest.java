package sn.ugb.ged.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.ged.web.rest.TestUtil;

class DocumentDelivreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentDelivreDTO.class);
        DocumentDelivreDTO documentDelivreDTO1 = new DocumentDelivreDTO();
        documentDelivreDTO1.setId(1L);
        DocumentDelivreDTO documentDelivreDTO2 = new DocumentDelivreDTO();
        assertThat(documentDelivreDTO1).isNotEqualTo(documentDelivreDTO2);
        documentDelivreDTO2.setId(documentDelivreDTO1.getId());
        assertThat(documentDelivreDTO1).isEqualTo(documentDelivreDTO2);
        documentDelivreDTO2.setId(2L);
        assertThat(documentDelivreDTO1).isNotEqualTo(documentDelivreDTO2);
        documentDelivreDTO1.setId(null);
        assertThat(documentDelivreDTO1).isNotEqualTo(documentDelivreDTO2);
    }
}
