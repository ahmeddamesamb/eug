package sn.ugb.gir;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.ugb.gir.domain.*;
import sn.ugb.gir.generator.*;
//import sn.ugb.gir.generator.InformationPersonnelleGenerator;
import sn.ugb.gir.repository.*;

import java.util.List;

@Service
public class FakerDataGenerator {

    @Autowired
    InformationPersonnelleRepository informationPersonnelleRepository;
    @Autowired
    TypeBourseRepository typeBourseRepository;
    @Autowired
    TypeHandicapRepository typeHandicapRepository;
    @Autowired
    EtudiantRepository etudiantRepository;
    @Autowired
    TypeSelectionRepository typeSelectionRepository;
    @Autowired
    DisciplineSportiveRepository disciplineSportiveRepository;
    @Autowired
    DisciplineSportiveEtudiantRepository disciplineSportiveEtudiantRepository;
    @Autowired
    BaccalaureatRepository baccalaureatRepository;
    @Autowired
    SerieRepository serieRepository;
    @Autowired
    LyceeRepository lyceeRepository;
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    PaysRepository paysRepository;
    @Autowired
    ZoneRepository zoneRepository;
    @Autowired
    ProcessusDinscriptionAdministrativeRepository processusDinscriptionAdministrativeRepository;
    @Autowired
    TypeAdmissionRepository typeAdmissionRepository;
    @Autowired
    InscriptionAdministrativeRepository inscriptionAdministrativeRepository;
    @Autowired
    AnneeAcademiqueRepository anneeAcademiqueRepository;
    @Autowired
    FormationRepository formationRepository;
    @Autowired
    FormationAutoriseeRepository formationAutoriseeRepository;
    @Autowired
    DomaineRepository domaineRepository;
    @Autowired
    SpecialiteRepository specialiteRepository;
    @Autowired
    private MentionRepository mentionRepository;
    @Autowired
    private NiveauRepository niveauRepository;
    @Autowired
    private UfrRepository ufrRepository;
    @Autowired
    private FormationInvalideRepository formationValideRepository;
    @Autowired
    private UniversiteRepository universiteRepository;
    @Autowired
    private MinistereRepository ministereRepository;
    @Autowired
    private InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository;
    @Autowired
    private FormationPriveeRepository formationPriveeRepository;


    public void generateData(int count) {
        generateEtudiantData(count);
        generateInformationPersonnelleData(count);
        generateTypeBourseData(count);
        generateZoneData(count);
    }

    public void generateInformationPersonnelleData(int count) {
        InformationPersonnelleGenerator informationPersonnelleGenerator = new InformationPersonnelleGenerator();
        List<TypeHandicap> typeHandicaps = typeHandicapRepository.findAll();
        List<TypeBourse> typeBourses = typeBourseRepository.findAll();
        List<Etudiant> etudiants = etudiantRepository.findAll();
        for (int i = 0; i < count; i++) {
            InformationPersonnelle informationPersonnelle = informationPersonnelleGenerator.generateInformationPersonnelle(etudiants, typeHandicaps, typeBourses);
            informationPersonnelleRepository.save(informationPersonnelle);
        }
    }

    public void generateTypeBourseData(int count) {
        TypeBourseGenerator typeBourseGenerator = new TypeBourseGenerator();
        for (int i = 0; i < count; i++) {
            TypeBourse typeBourse = typeBourseGenerator.generateTypeBourse();
            typeBourseRepository.save(typeBourse);
        }
    }

    public void generateTypeHandicapData(int count) {
        TypeHandicapGenerator typeHandicapGenerator = new TypeHandicapGenerator();
        for (int i = 0; i < count; i++) {
            TypeHandicap typeBourse = typeHandicapGenerator.generateTypeHandicap();
            typeHandicapRepository.save(typeBourse);
        }
    }

    public void generateEtudiantData(int count) {
        EtudiantGenerator etudiantGenerator = new EtudiantGenerator();
        List<Region> regions = regionRepository.findAll();
        List<Lycee> lycees = lyceeRepository.findAll();
        List<TypeSelection> typeSelections = typeSelectionRepository.findAll();
        for (int i = 0; i < count; i++) {
            Etudiant etudiant = etudiantGenerator.generateEtudiant(regions, lycees, typeSelections);
            etudiantRepository.save(etudiant);
        }
    }

    public void generateTypeSelectionData(int count) {
        for (int i = 0; i < count; i++) {
            TypeSelection typeSelection = TypeSelectionGenerator.generateTypeSelection();
            typeSelectionRepository.save(typeSelection);
        }
    }

    public void generateDisciplineSportiveData(int count) {
        for (int i = 0; i < count; i++) {
            DisciplineSportive disciplineSportive = DisciplineSportiveGenerator.generateDisciplineSportive();
            disciplineSportiveRepository.save(disciplineSportive);
        }
    }

    public void generateDisciplineSportiveEtudiantData(int count) {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        List<DisciplineSportive> disciplineSportives = disciplineSportiveRepository.findAll();
        for (int i = 0; i < count; i++) {
            DisciplineSportiveEtudiant disciplineSportiveEtudiant = DisciplineSportiveEtudiantGenerator.generateDisciplineSportiveEtudiant(disciplineSportives);
            disciplineSportiveEtudiantRepository.save(disciplineSportiveEtudiant);
        }
    }

    public void generateSerieData(int count) {
        for (int i = 0; i < count; i++) {
            Serie serie = SerieGenerator.generateSerie();
            serieRepository.save(serie);
        }
    }

    public void generateLyceeData(int count) {
        List<Region> regions = regionRepository.findAll();
        for (int i = 0; i < count; i++) {
            Lycee lycee = LyceeGenerator.generateLycee(regions);
            lyceeRepository.save(lycee);
        }
    }

    public void generateRegionData(int count) {
        List<Pays> pays = paysRepository.findAll();
        for (int i = 0; i < count; i++) {
            Region region = RegionGenerator.generateRegion(pays);
            regionRepository.save(region);
        }
    }

    public void generateZoneData(int count) {
        ZoneGenerator zoneGenerator = new ZoneGenerator();
        for (int i = 0; i < count; i++) {
            Zone zone = zoneGenerator.generateZone();
            zoneRepository.save(zone);
        }
    }

    public void generatePaysData(int count) {
        PaysGenerator paysGenerator = new PaysGenerator();
        for (int i = 0; i < count; i++) {
            Pays pays = paysGenerator.generatePays();
            paysRepository.save(pays);
        }
    }

    public void generateBaccalaureatData(int count) {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        List<Serie> series = serieRepository.findAll();
        for (int i = 0; i < count; i++) {
            Baccalaureat baccalaureat = BaccalaureatGenerator.generateBaccalaureat(etudiants, series);
            baccalaureatRepository.save(baccalaureat);
        }
    }

    public void generateProcessusDinscriptionAdministrativeData(int count) {
        List<InscriptionAdministrative> inscriptionAdministratives = inscriptionAdministrativeRepository.findAll();
        for (int i = 0; i < count; i++) {
            ProcessusDinscriptionAdministrative processusDinscriptionAdministrative = ProcessusDinscriptionAdministrativeGenerator.generateProcessusDinscriptionAdministrative(inscriptionAdministratives);
            processusDinscriptionAdministrativeRepository.save(processusDinscriptionAdministrative);
        }
    }

    public void generateTypeAdmissionData(int count) {
        for (int i = 0; i < count; i++) {
            TypeAdmission admission = TypeAdmissionGenerator.generateTypeAdmission();
            typeAdmissionRepository.save(admission);
        }
    }

    public void generateAnneeAcademiqueData(int count) {
        for (int i = 0; i < count; i++) {
            AnneeAcademique anneeAcademique = AnneeAcademiqueGenerator.generateAnneeAcademique();
            anneeAcademiqueRepository.save(anneeAcademique);
        }
    }

    public void generateInscriptionAdministrativeData(int count) {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        List<AnneeAcademique> anneeAcademiques = anneeAcademiqueRepository.findAll();
        List<TypeAdmission> typeAdmissions = typeAdmissionRepository.findAll();
        for (int i = 0; i < count; i++) {
            InscriptionAdministrative inscriptionAdministrative = InscriptionAdministrativeGenerator.generateInscriptionAdministrative(etudiants, anneeAcademiques, typeAdmissions);
            inscriptionAdministrativeRepository.save(inscriptionAdministrative);
        }
    }

    public void generateFormationData(int count) {
        List<Specialite> specialites = specialiteRepository.findAll();
        List<Niveau> niveaus = niveauRepository.findAll();
        List<FormationPrivee> formationPrivees = formationPriveeRepository.findAll();
        for (int i = 0; i < count; i++) {
            Formation formation = FormationGenerator.generateFormation(specialites, niveaus,formationPrivees);
            formationRepository.save(formation);
        }
    }

    public void generateMentionData(int count) {
        List<Domaine> domaines = domaineRepository.findAll();
        for (int i = 0; i < count; i++) {
            Mention mention = MentionGenerator.generateMention(domaines);
            mentionRepository.save(mention);
        }
    }

    public void generateFormationAutoriseeData(int count) {
        for (int i = 0; i < count; i++) {
            FormationAutorisee formationAutorisee = FormationAutoriseeGenerator.generateFormationAutorisee();
            formationAutoriseeRepository.save(formationAutorisee);
        }
    }

    public void generateDomaineData(int count) {
        List<Mention> mentions = mentionRepository.findAll();
        for (int i = 0; i < count; i++) {
            Domaine domaine = DomaineGenerator.generateDomaine();
            domaineRepository.save(domaine);
        }
    }

    public void generateSpecialiteData(int count) {
        List<Mention> mentions = mentionRepository.findAll();
        for (int i = 0; i < count; i++) {
            Specialite specialite = SpecialiteGenerator.generateSpecialite(mentions);
            specialiteRepository.save(specialite);
        }
    }

    public void generateNiveauData(int count) {
        for (int i = 0; i < count; i++) {
            Niveau niveau = NiveauGenerator.generateNiveau();
            niveauRepository.save(niveau);
        }
    }

    public void generateMinistereData(int count) {
        for (int i = 0; i < count; i++) {
            Ministere ministere = MinistereGenerator.generateMinistere();
            ministereRepository.save(ministere);
        }
    }

    public void generateUniversiteData(int count) {
        List<Ministere> ministeres = ministereRepository.findAll();
        for (int i = 0; i < count; i++) {
            Universite universites = UniversiteGenerator.generateUniversite(ministeres);
            universiteRepository.save(universites);
        }
    }

    public void generateUfrData(int count) {
        List<Universite> universites = universiteRepository.findAll();
        for (int i = 0; i < count; i++) {
            Ufr ufr = UfrGenerator.generateUFR(universites);
            ufrRepository.save(ufr);
        }
    }

    public void generateFormationValideData(int count) {
        List<Formation> formations = formationRepository.findAll();
        List<AnneeAcademique> anneeAcademiques = anneeAcademiqueRepository.findAll();
        for (int i = 0; i < count; i++) {
            FormationInvalide formationValide = FormationValideGenerator.generateFormationValide(formations,anneeAcademiques);
            formationValideRepository.save(formationValide);
        }
    }

    public void generateInscriptionAdministrativeFormationData(int count) {
        List<InscriptionAdministrative> inscriptionAdministratives = inscriptionAdministrativeRepository.findAll();
        List<Formation> formations = formationRepository.findAll();
        for (int i = 0; i < count; i++) {
            InscriptionAdministrativeFormation inscriptionAdministrativeFormation = InscriptionAdministrativeFormationGenerator.generateInscriptionAdministrativeFormation(formations, inscriptionAdministratives);
            inscriptionAdministrativeFormationRepository.save(inscriptionAdministrativeFormation);
        }
    }
}
