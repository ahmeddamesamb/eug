
package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.*;
import sn.ugb.gir.domain.enumeration.TypeFormation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FormationGenerator {
    public static Faker getFaker() {
        return faker;
    }

    static final Faker faker = new Faker();

    public static Formation generateFormation(List<Specialite> specialite, List<Niveau> niveau,List<FormationPrivee> formationPrivees) {
        Formation formation = new Formation();
        formation.setFraisDossierYN(faker.random().nextInt(0, 1));
        formation.setClasseDiplomanteYN(faker.random().nextInt(0, 1));
        formation.setLibelleDiplome(faker.educator().course());
        formation.setCodeFormation(faker.lorem().word());
        formation.setNbreCreditsMin(faker.number().numberBetween(0, 300));
        formation.setEstParcoursYN(faker.random().nextInt(0, 1));
        formation.setLmdYN(faker.random().nextInt(0, 1));
        formation.setSpecialite(specialite.get(faker.random().nextInt(specialite.size())));
        formation.setNiveau(niveau.get(faker.random().nextInt(niveau.size())));
        formation.setFormationPrivee(formationPrivees.get(faker.random().nextInt(formationPrivees.size())));
        formation.setTypeFormation(TypeFormation.PUBLIC);

        return formation;
    }
}
