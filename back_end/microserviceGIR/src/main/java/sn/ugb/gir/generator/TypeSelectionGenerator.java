package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.TypeSelection;

public class TypeSelectionGenerator {
    private static final Faker faker = new Faker();

    public static TypeSelection generateTypeSelection() {
        TypeSelection typeSelection = new TypeSelection();
        typeSelection.setId(faker.number().randomNumber());
        typeSelection.setLibelleTypeSelection(faker.lorem().word());
        return typeSelection;
    }
}
