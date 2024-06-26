package sn.ugb.gir.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.ugb.gir.domain.InscriptionAdministrativeTestSamples.*;
import static sn.ugb.gir.domain.TypeAdmissionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.ugb.gir.web.rest.TestUtil;

class TypeAdmissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeAdmission.class);
        TypeAdmission typeAdmission1 = getTypeAdmissionSample1();
        TypeAdmission typeAdmission2 = new TypeAdmission();
        assertThat(typeAdmission1).isNotEqualTo(typeAdmission2);

        typeAdmission2.setId(typeAdmission1.getId());
        assertThat(typeAdmission1).isEqualTo(typeAdmission2);

        typeAdmission2 = getTypeAdmissionSample2();
        assertThat(typeAdmission1).isNotEqualTo(typeAdmission2);
    }

    @Test
    void inscriptionAdministrativesTest() throws Exception {
        TypeAdmission typeAdmission = getTypeAdmissionRandomSampleGenerator();
        InscriptionAdministrative inscriptionAdministrativeBack = getInscriptionAdministrativeRandomSampleGenerator();

        typeAdmission.addInscriptionAdministratives(inscriptionAdministrativeBack);
        assertThat(typeAdmission.getInscriptionAdministratives()).containsOnly(inscriptionAdministrativeBack);
        assertThat(inscriptionAdministrativeBack.getTypeAdmission()).isEqualTo(typeAdmission);

        typeAdmission.removeInscriptionAdministratives(inscriptionAdministrativeBack);
        assertThat(typeAdmission.getInscriptionAdministratives()).doesNotContain(inscriptionAdministrativeBack);
        assertThat(inscriptionAdministrativeBack.getTypeAdmission()).isNull();

        typeAdmission.inscriptionAdministratives(new HashSet<>(Set.of(inscriptionAdministrativeBack)));
        assertThat(typeAdmission.getInscriptionAdministratives()).containsOnly(inscriptionAdministrativeBack);
        assertThat(inscriptionAdministrativeBack.getTypeAdmission()).isEqualTo(typeAdmission);

        typeAdmission.setInscriptionAdministratives(new HashSet<>());
        assertThat(typeAdmission.getInscriptionAdministratives()).doesNotContain(inscriptionAdministrativeBack);
        assertThat(inscriptionAdministrativeBack.getTypeAdmission()).isNull();
    }
}
