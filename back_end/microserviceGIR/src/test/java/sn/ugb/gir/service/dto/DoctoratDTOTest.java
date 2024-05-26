package sn.ugb.gir.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class DoctoratDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctoratDTO.class);
        DoctoratDTO doctoratDTO1 = new DoctoratDTO();
        doctoratDTO1.setId(1L);
        DoctoratDTO doctoratDTO2 = new DoctoratDTO();
        assertThat(doctoratDTO1).isNotEqualTo(doctoratDTO2);
        doctoratDTO2.setId(doctoratDTO1.getId());
        assertThat(doctoratDTO1).isEqualTo(doctoratDTO2);
        doctoratDTO2.setId(2L);
        assertThat(doctoratDTO1).isNotEqualTo(doctoratDTO2);
        doctoratDTO1.setId(null);
        assertThat(doctoratDTO1).isNotEqualTo(doctoratDTO2);
    }
}
