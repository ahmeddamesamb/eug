package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.*;

import java.time.LocalDate;
import java.util.List;

public class EtudiantGenerator {

    static final Faker faker = new Faker();

    public static Etudiant generateEtudiant(List<Region> region, List<Lycee> lycee, List<TypeSelection> typeSelection) {
        Etudiant etudiant = new Etudiant();
        etudiant.setCodeEtu(faker.idNumber().valid());
        etudiant.setIne(faker.idNumber().valid());
        etudiant.setCodeBU(faker.number().numberBetween(100000, 999999));
        etudiant.setEmailUGB(faker.internet().emailAddress());
        etudiant.setDateNaissEtu(LocalDate.parse(LocalDate.now().toString()));
        etudiant.setLieuNaissEtu(faker.address().city());
        etudiant.setSexe(faker.options().option("M", "F"));
        etudiant.setNumDocIdentite(faker.idNumber().valid());
        etudiant.setAssimileYN(faker.bool().bool() ? 1 : 0);
        etudiant.setExonereYN(faker.bool().bool() ? 1 : 0);
        etudiant.setRegion(region.get(faker.random().nextInt(region.size())));
        etudiant.setLycee(lycee.get(faker.random().nextInt(lycee.size())));
        etudiant.setTypeSelection(typeSelection.get(faker.random().nextInt(typeSelection.size())));
        return etudiant;
    }
}
