package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.domain.Mention;

import java.util.Set;

public class DomaineGenerator {
    public static Faker getFaker() {
        return faker;
    }

    static final Faker faker = new Faker();

    public static Domaine generateDomaine() {
        Domaine domaine = new Domaine();
        domaine.setLibelleDomaine(faker.lorem().word());
        return domaine;
    }
}
