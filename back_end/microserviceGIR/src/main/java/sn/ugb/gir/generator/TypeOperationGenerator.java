package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.TypeOperation;

public class TypeOperationGenerator {
    private static final Faker faker = new Faker();

    public static TypeOperation generateTypeOperation() {
        TypeOperation typeOperation = new TypeOperation();
        typeOperation.setLibelleTypeOperation(faker.lorem().word());
        return typeOperation;
    }
}
