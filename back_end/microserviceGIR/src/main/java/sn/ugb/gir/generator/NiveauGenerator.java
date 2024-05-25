package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;
import sn.ugb.gir.domain.Niveau;
import sn.ugb.gir.domain.enumeration.Cycle;

@Component
public class NiveauGenerator {
    private static final Faker faker = new Faker();

    public static Niveau generateNiveau() {
        Niveau niveau = new Niveau();
        niveau.setLibelleNiveau(faker.educator().course());
        niveau.setCycleNiveau(Cycle.LICENCE);
        niveau.setCodeNiveau(faker.code().isbn10());
        niveau.setAnneeEtude(faker.number().digit());
        return niveau;
    }
}
