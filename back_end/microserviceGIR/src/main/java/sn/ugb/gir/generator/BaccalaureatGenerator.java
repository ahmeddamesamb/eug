package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Baccalaureat;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.Serie;

import java.time.LocalDate;
import java.util.List;

public class BaccalaureatGenerator {
    public static Faker getFaker() {
        return faker;
    }

    static final Faker faker = new Faker();

    public static Baccalaureat generateBaccalaureat(List<Etudiant> etudiants, List<Serie> series) {
        Baccalaureat baccalaureat = new Baccalaureat();
        baccalaureat.setOrigineScolaire(faker.educator().university());
        baccalaureat.setAnneeBac(LocalDate.parse(LocalDate.now().toString()));
        baccalaureat.setNumeroTable(faker.number().numberBetween(10000, 99999));
        baccalaureat.setNatureBac(faker.lorem().word());
        baccalaureat.setMentionBac(faker.options().option("Passable", "Assez Bien", "Bien", "Très Bien"));
        baccalaureat.setMoyenneSelectionBac((float) faker.number().randomDouble(2, 10, 20));
        baccalaureat.setMoyenneBac((float) faker.number().randomDouble(2, 10, 20));
        baccalaureat.setEtudiant(etudiants.get(faker.random().nextInt(etudiants.size())));
        baccalaureat.setSerie(series.get(faker.random().nextInt(series.size())));
        return baccalaureat;
    }
}
