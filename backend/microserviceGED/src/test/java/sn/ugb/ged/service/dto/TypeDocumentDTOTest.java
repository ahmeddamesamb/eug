package sn.ugb.ged.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.ged.web.rest.TestUtil;

class TypeDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDocumentDTO.class);
        TypeDocumentDTO typeDocumentDTO1 = new TypeDocumentDTO();
        typeDocumentDTO1.setId(1L);
        TypeDocumentDTO typeDocumentDTO2 = new TypeDocumentDTO();
        assertThat(typeDocumentDTO1).isNotEqualTo(typeDocumentDTO2);
        typeDocumentDTO2.setId(typeDocumentDTO1.getId());
        assertThat(typeDocumentDTO1).isEqualTo(typeDocumentDTO2);
        typeDocumentDTO2.setId(2L);
        assertThat(typeDocumentDTO1).isNotEqualTo(typeDocumentDTO2);
        typeDocumentDTO1.setId(null);
        assertThat(typeDocumentDTO1).isNotEqualTo(typeDocumentDTO2);
    }
}
