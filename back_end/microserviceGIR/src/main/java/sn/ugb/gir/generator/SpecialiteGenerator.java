package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Mention;
import sn.ugb.gir.domain.Specialite;

import java.util.List;
import java.util.Set;

public class SpecialiteGenerator {
    private static final Faker faker = new Faker();

    public static Specialite generateSpecialite(List<Mention> mentions) {
        Specialite specialite = new Specialite();
        specialite.setNomSpecialites(faker.lorem().word());
        specialite.setSigleSpecialites(faker.lorem().word());
        specialite.setSpecialiteParticulierYN(faker.number().numberBetween(0, 1));
        specialite.setSpecialitesPayanteYN(faker.number().numberBetween(0, 1));
        specialite.setMention(mentions.get(faker.random().nextInt(mentions.size())));
        return specialite;
    }
}
