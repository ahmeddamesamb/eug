package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.FormationAutorisee;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FormationAutoriseeGenerator {
    private static final Faker faker = new Faker();

    public static FormationAutorisee generateFormationAutorisee() {
        FormationAutorisee formationAutorisee = new FormationAutorisee();
        formationAutorisee.setDateDebut(LocalDate.now().plusDays(faker.number().numberBetween(1, 30)));
        formationAutorisee.setDateFin(formationAutorisee.getDateDebut().plusMonths(faker.number().numberBetween(1, 12)));
        formationAutorisee.setEnCoursYN(faker.random().nextInt(0, 1));
        return formationAutorisee;
    }
}
