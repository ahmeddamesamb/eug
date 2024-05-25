package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.repository.UniversiteRepository;

import java.util.List;

public class UfrGenerator {
    private static final Faker faker = new Faker();;

    public static Ufr generateUFR(List<Universite> universites) {
        Ufr ufr = new Ufr();
        ufr.setLibeleUfr(faker.university().name());
        ufr.setSigleUfr(faker.letterify("UFR-???"));
        ufr.setSystemeLMDYN(faker.random().nextInt(0, 1)); // Génère 0 ou 1 pour systemeLMDYN
        ufr.setOrdreStat(faker.number().numberBetween(1, 100));
        ufr.setUniversite(universites.get(faker.random().nextInt(universites.size())));

        return ufr;
    }
}
