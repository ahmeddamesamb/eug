package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.DisciplineSportive;
import sn.ugb.gir.domain.DisciplineSportiveEtudiant;
import sn.ugb.gir.domain.Etudiant;

import java.util.List;

public class DisciplineSportiveEtudiantGenerator {
    public static Faker getFaker() {
        return faker;
    }

    static final Faker faker = new Faker();

    public static DisciplineSportiveEtudiant generateDisciplineSportiveEtudiant(List<DisciplineSportive> disciplineSportives) {
        DisciplineSportiveEtudiant disciplineSportiveEtudiant = new DisciplineSportiveEtudiant();
        disciplineSportiveEtudiant.setId(faker.number().randomNumber());
        disciplineSportiveEtudiant.setId(faker.number().randomNumber());
        disciplineSportiveEtudiant.setDisciplineSportive(disciplineSportives.get(faker.random().nextInt(disciplineSportives.size())));
        return disciplineSportiveEtudiant;
    }
}
