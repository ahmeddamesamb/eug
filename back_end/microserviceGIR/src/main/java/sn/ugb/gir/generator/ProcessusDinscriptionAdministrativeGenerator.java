package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.domain.ProcessusDinscriptionAdministrative;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class ProcessusDinscriptionAdministrativeGenerator {
    private static final Faker faker = new Faker();

    public static ProcessusDinscriptionAdministrative generateProcessusDinscriptionAdministrative(List<InscriptionAdministrative> inscriptionAdministratives) {
        ProcessusDinscriptionAdministrative processus = new ProcessusDinscriptionAdministrative();
        processus.setInscriptionDemarreeYN(faker.bool().bool() ? 1 : 0);
        processus.setDateDemarrageInscription(Instant.parse(LocalDate.now().toString()));
        processus.setInscriptionAnnuleeYN(faker.bool().bool() ? 1 : 0);
        processus.setDateAnnulationInscription(Instant.parse(LocalDate.now().toString()));
        processus.setApteYN(faker.bool().bool() ? 1 : 0);
        processus.setDateVisiteMedicale(Instant.parse(LocalDate.now().toString()));
        processus.setBeneficiaireCROUSYN(faker.bool().bool() ? 1 : 0);
        processus.setDateRemiseQuitusCROUS(Instant.parse(LocalDate.now().toString()));
        processus.setNouveauCodeBUYN(faker.bool().bool() ? 1 : 0);
        processus.setQuitusBUYN(faker.bool().bool() ? 1 : 0);
        processus.setDateRemiseQuitusBU(Instant.parse(LocalDate.now().toString()));
        processus.setExporterBUYN(faker.bool().bool() ? 1 : 0);
        processus.setFraisObligatoiresPayesYN(faker.bool().bool() ? 1 : 0);
        processus.setDatePaiementFraisObligatoires(Instant.parse(LocalDate.now().toString()));
        processus.setNumeroQuitusCROUS(faker.number().numberBetween(10000, 99999));
        processus.setCarteEturemiseYN(faker.bool().bool() ? 1 : 0);
        processus.setCarteDupremiseYN(faker.bool().bool() ? 1 : 0);
        processus.setDateRemiseCarteEtu(Instant.parse(LocalDate.now().toString()));
        processus.setDateRemiseCarteDup(Integer.valueOf(LocalDate.now().toString()));
        processus.setInscritAdministrativementYN(faker.bool().bool() ? 1 : 0);
        processus.setDateInscriptionAdministrative(Instant.parse(LocalDate.now().toString()));
        processus.setDerniereModification(Instant.parse(LocalDate.now().toString()));
        processus.setInscritOnlineYN(faker.bool().bool() ? 1 : 0);
        processus.setEmailUser(faker.internet().emailAddress());
        processus.setInscriptionAdministrative(inscriptionAdministratives.get(faker.random().nextInt(inscriptionAdministratives.size())));

        return processus;
    }
}
