package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.DisciplineSportive;

public class DisciplineSportiveGenerator {
    private static final Faker faker = new Faker();

    public static DisciplineSportive generateDisciplineSportive() {
        DisciplineSportive disciplineSportive = new DisciplineSportive();
        disciplineSportive.setLibelleDisciplineSportive(faker.esports().game());
        return disciplineSportive;
    }
}
