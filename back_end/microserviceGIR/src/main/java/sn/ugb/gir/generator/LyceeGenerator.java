package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Lycee;
import sn.ugb.gir.domain.Region;

import java.util.List;

public class LyceeGenerator {
    public static Faker getFaker() {
        return faker;
    }

    static final Faker faker = new Faker();

    public static Lycee generateLycee(List<Region> region) {
        Lycee lycee = new Lycee();
        lycee.setNomLycee(faker.educator().campus());
        lycee.setCodeLycee(faker.code().isbn10());
        lycee.setVilleLycee(faker.address().city());
        lycee.setAcademieLycee(faker.number().numberBetween(1, 10));
        lycee.setCentreExamen(faker.address().streetName());
        lycee.setRegion(region.get(faker.random().nextInt(region.size())));
        return lycee;
    }
}
