package sn.ugb.gir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;


import sn.ugb.gir.domain.Zone;
import sn.ugb.gir.repository.ZoneRepository;

@Component
public class InitData implements ApplicationRunner{

    @Autowired
    FakerDataGenerator dataGen;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        dataGen.generateZoneData(10);
//        dataGen.generatePaysData(200);
//        dataGen.generateRegionData(14);
//        dataGen.generateLyceeData(350);
//        dataGen.generateSerieData(15);
//
//        dataGen.generateTypeSelectionData(5);
//        dataGen.generateEtudiantData(1000);
//
//        dataGen.generateBaccalaureatData(1000);
//
//        dataGen.generateTypeHandicapData(5);
//        dataGen.generateTypeBourseData(5);
//        dataGen.generateInformationPersonnelleData(1000);

//        dataGen.generateDisciplineSportiveData(5);
//        dataGen.generateDisciplineSportiveEtudiantData(1000);
//
//
//        dataGen.generateAnneeAcademiqueData(24);
//        dataGen.generateTypeAdmissionData(5);
//        dataGen.generateInscriptionAdministrativeData(1000);
//        dataGen.generateProcessusDinscriptionAdministrativeData(1000);


//        dataGen.generateNiveauData(8);
//        dataGen.generateDomaineData(8);
//        dataGen.generateMentionData(20);

//        dataGen.generateSpecialiteData(100);
//        dataGen.generateFormationAutoriseeData(200);
//        dataGen.generateFormationData(200);
//        dataGen.generateFormationValideData(200);

//        dataGen.generateMinistereData(2);
//        dataGen.generateUniversiteData(5);
//        dataGen.generateUfrData(30);

//        dataGen.generateInscriptionAdministrativeFormationData(1000);
    }
}
