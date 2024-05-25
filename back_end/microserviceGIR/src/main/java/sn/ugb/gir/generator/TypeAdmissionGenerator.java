package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.TypeAdmission;

public class TypeAdmissionGenerator {
    private static final Faker faker = new Faker();

    public static TypeAdmission generateTypeAdmission() {
        TypeAdmission typeAdmission = new TypeAdmission();
        typeAdmission.setLibelleTypeAdmission(faker.lorem().word());
        return typeAdmission;
    }
}
