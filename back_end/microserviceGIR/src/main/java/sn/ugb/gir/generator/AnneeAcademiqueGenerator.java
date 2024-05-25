package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.AnneeAcademique;

public class AnneeAcademiqueGenerator {
    private static final Faker faker = new Faker();

    public static AnneeAcademique generateAnneeAcademique() {
        AnneeAcademique anneeAcademique = new AnneeAcademique();

        String libelleAnnee = faker.number().numberBetween(2000, 2025) + "-" + faker.number().numberBetween(2000, 2025);
        anneeAcademique.setLibelleAnneeAcademique(libelleAnnee);
        anneeAcademique.setAnneeCouranteYN(faker.number().numberBetween(2000, 2025));

        return anneeAcademique;
    }
}
