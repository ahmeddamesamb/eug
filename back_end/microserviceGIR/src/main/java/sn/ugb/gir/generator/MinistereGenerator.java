package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Ministere;

import java.time.LocalDate;

public class MinistereGenerator {
    static final Faker faker = new Faker();

    public static Ministere generateMinistere(){
        Ministere ministere = new Ministere();
        ministere.setNomMinistere(faker.company().name());
        ministere.setSigleMinistere(faker.letterify("????"));
        ministere.setDateDebut(LocalDate.now().minusYears(faker.number().numberBetween(1, 10)));
        ministere.setDateFin(LocalDate.now().plusYears(faker.number().numberBetween(1, 5)));
        ministere.setEnCoursYN(faker.random().nextInt(0, 2));

        return ministere;
    }
}
