package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.TypeBourse;

public class TypeBourseGenerator {
    private static final Faker faker = new Faker();

    public static TypeBourse generateTypeBourse() {
        TypeBourse typeBourse = new TypeBourse();
        typeBourse.setLibelleTypeBourse(faker.lorem().word());
        return typeBourse;
    }
}
