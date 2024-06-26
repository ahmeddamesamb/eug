package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.OperationTestSamples.*;
import static sn.ugb.gir.domain.TypeOperationTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class OperationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operation.class);
        Operation operation1 = getOperationSample1();
        Operation operation2 = new Operation();
        assertThat(operation1).isNotEqualTo(operation2);

        operation2.setId(operation1.getId());
        assertThat(operation1).isEqualTo(operation2);

        operation2 = getOperationSample2();
        assertThat(operation1).isNotEqualTo(operation2);
    }

    @Test
    void typeOperationTest() throws Exception {
        Operation operation = getOperationRandomSampleGenerator();
        TypeOperation typeOperationBack = getTypeOperationRandomSampleGenerator();

        operation.setTypeOperation(typeOperationBack);
        assertThat(operation.getTypeOperation()).isEqualTo(typeOperationBack);

        operation.typeOperation(null);
        assertThat(operation.getTypeOperation()).isNull();
    }
}
