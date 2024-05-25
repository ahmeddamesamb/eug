package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.TypeHandicap;

public class TypeHandicapGenerator {
    private static final Faker faker = new Faker();

    public static TypeHandicap generateTypeHandicap() {
        TypeHandicap typeHandicap = new TypeHandicap();
        typeHandicap.setLibelleTypeHandicap(faker.lorem().word());
        return typeHandicap;
    }
}
