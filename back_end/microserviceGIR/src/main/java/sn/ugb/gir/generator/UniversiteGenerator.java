package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Ministere;
import sn.ugb.gir.domain.Universite;

import java.util.List;

public class UniversiteGenerator {

    static final Faker faker = new Faker();

    public static Universite generateUniversite(List<Ministere> ministeres) {
        Universite universite = new Universite();
        universite.setNomUniversite(faker.university().name());
        universite.setSigleUniversite(faker.letterify("????"));
        universite.setMinistere(ministeres.get(faker.random().nextInt(ministeres.size())));

        return universite;
    }
}
