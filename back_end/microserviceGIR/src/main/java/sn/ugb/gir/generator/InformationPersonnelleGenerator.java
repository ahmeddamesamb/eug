package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.domain.TypeBourse;
import sn.ugb.gir.domain.TypeHandicap;

import java.time.LocalDate;
import java.util.List;

public class  InformationPersonnelleGenerator {
    public Faker getFaker() {
        return faker;
    }

    final Faker faker;

    public InformationPersonnelleGenerator() {
        this.faker = new Faker();
    }

    public InformationPersonnelle generateInformationPersonnelle(List<Etudiant> etudiant, List<TypeHandicap> typeHandicap, List<TypeBourse> typeBourse) {
        InformationPersonnelle informationPersonnelle = new InformationPersonnelle();

        informationPersonnelle.setNomEtu(faker.name().lastName());
        informationPersonnelle.setNomJeuneFilleEtu(faker.name().lastName());
        informationPersonnelle.setPrenomEtu(faker.name().firstName());
        informationPersonnelle.setStatutMarital(faker.random().nextBoolean() ? "Célibataire" : "Marié");
        informationPersonnelle.setRegime(faker.random().nextInt(1, 5)); // Exemple de génération aléatoire pour le régime
        informationPersonnelle.setProfession(faker.company().profession());
        informationPersonnelle.setAdresseEtu(faker.address().fullAddress());
        informationPersonnelle.setTelEtu(faker.phoneNumber().cellPhone());
        informationPersonnelle.setEmailEtu(faker.internet().emailAddress());
        informationPersonnelle.setAdresseParent(faker.address().fullAddress());
        informationPersonnelle.setTelParent(faker.phoneNumber().cellPhone());
        informationPersonnelle.setEmailParent(faker.internet().emailAddress());
        informationPersonnelle.setNomParent(faker.name().fullName());
        informationPersonnelle.setPrenomParent(faker.name().fullName());
        informationPersonnelle.setHandicapYN(faker.random().nextInt(0, 1)); // Exemple de génération aléatoire pour le handicap
        informationPersonnelle.setOrdiPersoYN(faker.random().nextInt(0, 1)); // Exemple de génération aléatoire pour l'ordinateur personnel
        informationPersonnelle.setDerniereModification(LocalDate.now());
        informationPersonnelle.setEmailUser(faker.internet().emailAddress());
        informationPersonnelle.setEtudiant(etudiant.get(faker.random().nextInt(etudiant.size())));
        informationPersonnelle.setTypeHandique(typeHandicap.get(faker.random().nextInt(typeHandicap.size())));
        informationPersonnelle.setTypeBourse(typeBourse.get(faker.random().nextInt(typeBourse.size())));

        return informationPersonnelle;
    }
}
