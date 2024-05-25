package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.FormationInvalide;

import java.time.LocalDate;
import java.util.List;

public class FormationValideGenerator {
    private static final Faker faker = new Faker();

    public static FormationInvalide generateFormationValide(List<Formation> formation, List<AnneeAcademique> anneeAcademiques) {
        FormationInvalide formationValide = new FormationInvalide();
        formationValide.setActifYN(faker.random().nextInt(0, 1));
        formationValide.setFormation(formation.get(faker.random().nextInt(formation.size())));
        formationValide.setAnneAcademique(anneeAcademiques.get(faker.random().nextInt(anneeAcademiques.size())));
        return formationValide;
    }
}
