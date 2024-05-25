package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.domain.FormationAutorisee;
import sn.ugb.gir.domain.Mention;

import java.time.LocalDate;
import java.util.List;

public class MentionGenerator {
    public static Faker getFaker() {
        return faker;
    }

    static final Faker faker = new Faker();

    public static Mention generateMention(List<Domaine> domaine) {
        Mention mention = new Mention();
        mention.setLibelleMention(faker.lorem().word());
        mention.setDomaine(domaine.get(faker.random().nextInt(domaine.size())));
        return mention;
    }
}
