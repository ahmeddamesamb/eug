package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.ProcessusDinscriptionAdministrative;
import sn.ugb.gir.repository.ProcessusDinscriptionAdministrativeRepository;
import sn.ugb.gir.service.dto.ProcessusDinscriptionAdministrativeDTO;
import sn.ugb.gir.service.mapper.ProcessusDinscriptionAdministrativeMapper;

/**
 * Integration tests for the {@link ProcessusDinscriptionAdministrativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessusDinscriptionAdministrativeResourceIT {

    private static final Integer DEFAULT_INSCRIPTION_DEMARREE_YN = 1;
    private static final Integer UPDATED_INSCRIPTION_DEMARREE_YN = 2;

    private static final Instant DEFAULT_DATE_DEMARRAGE_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEMARRAGE_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_INSCRIPTION_ANNULEE_YN = 1;
    private static final Integer UPDATED_INSCRIPTION_ANNULEE_YN = 2;

    private static final Instant DEFAULT_DATE_ANNULATION_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ANNULATION_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_APTE_YN = 1;
    private static final Integer UPDATED_APTE_YN = 2;

    private static final Instant DEFAULT_DATE_VISITE_MEDICALE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_VISITE_MEDICALE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_BENEFICIAIRE_CROUSYN = 1;
    private static final Integer UPDATED_BENEFICIAIRE_CROUSYN = 2;

    private static final Instant DEFAULT_DATE_REMISE_QUITUS_CROUS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REMISE_QUITUS_CROUS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NOUVEAU_CODE_BUYN = 1;
    private static final Integer UPDATED_NOUVEAU_CODE_BUYN = 2;

    private static final Integer DEFAULT_QUITUS_BUYN = 1;
    private static final Integer UPDATED_QUITUS_BUYN = 2;

    private static final Instant DEFAULT_DATE_REMISE_QUITUS_BU = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REMISE_QUITUS_BU = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_EXPORTER_BUYN = 1;
    private static final Integer UPDATED_EXPORTER_BUYN = 2;

    private static final Integer DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN = 1;
    private static final Integer UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN = 2;

    private static final Instant DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NUMERO_QUITUS_CROUS = 1;
    private static final Integer UPDATED_NUMERO_QUITUS_CROUS = 2;

    private static final Integer DEFAULT_CARTE_ETUREMISE_YN = 1;
    private static final Integer UPDATED_CARTE_ETUREMISE_YN = 2;

    private static final Integer DEFAULT_CARTE_DUPREMISE_YN = 1;
    private static final Integer UPDATED_CARTE_DUPREMISE_YN = 2;

    private static final Instant DEFAULT_DATE_REMISE_CARTE_ETU = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REMISE_CARTE_ETU = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DATE_REMISE_CARTE_DUP = 1;
    private static final Integer UPDATED_DATE_REMISE_CARTE_DUP = 2;

    private static final Integer DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN = 1;
    private static final Integer UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN = 2;

    private static final Instant DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DERNIERE_MODIFICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DERNIERE_MODIFICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_INSCRIT_ONLINE_YN = 1;
    private static final Integer UPDATED_INSCRIT_ONLINE_YN = 2;

    private static final String DEFAULT_EMAIL_USER = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/processus-dinscription-administratives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessusDinscriptionAdministrativeRepository processusDinscriptionAdministrativeRepository;

    @Autowired
    private ProcessusDinscriptionAdministrativeMapper processusDinscriptionAdministrativeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessusDinscriptionAdministrativeMockMvc;

    private ProcessusDinscriptionAdministrative processusDinscriptionAdministrative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessusDinscriptionAdministrative createEntity(EntityManager em) {
        ProcessusDinscriptionAdministrative processusDinscriptionAdministrative = new ProcessusDinscriptionAdministrative()
            .inscriptionDemarreeYN(DEFAULT_INSCRIPTION_DEMARREE_YN)
            .dateDemarrageInscription(DEFAULT_DATE_DEMARRAGE_INSCRIPTION)
            .inscriptionAnnuleeYN(DEFAULT_INSCRIPTION_ANNULEE_YN)
            .dateAnnulationInscription(DEFAULT_DATE_ANNULATION_INSCRIPTION)
            .apteYN(DEFAULT_APTE_YN)
            .dateVisiteMedicale(DEFAULT_DATE_VISITE_MEDICALE)
            .beneficiaireCROUSYN(DEFAULT_BENEFICIAIRE_CROUSYN)
            .dateRemiseQuitusCROUS(DEFAULT_DATE_REMISE_QUITUS_CROUS)
            .nouveauCodeBUYN(DEFAULT_NOUVEAU_CODE_BUYN)
            .quitusBUYN(DEFAULT_QUITUS_BUYN)
            .dateRemiseQuitusBU(DEFAULT_DATE_REMISE_QUITUS_BU)
            .exporterBUYN(DEFAULT_EXPORTER_BUYN)
            .fraisObligatoiresPayesYN(DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN)
            .datePaiementFraisObligatoires(DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES)
            .numeroQuitusCROUS(DEFAULT_NUMERO_QUITUS_CROUS)
            .carteEturemiseYN(DEFAULT_CARTE_ETUREMISE_YN)
            .carteDupremiseYN(DEFAULT_CARTE_DUPREMISE_YN)
            .dateRemiseCarteEtu(DEFAULT_DATE_REMISE_CARTE_ETU)
            .dateRemiseCarteDup(DEFAULT_DATE_REMISE_CARTE_DUP)
            .inscritAdministrativementYN(DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN)
            .dateInscriptionAdministrative(DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE)
            .derniereModification(DEFAULT_DERNIERE_MODIFICATION)
            .inscritOnlineYN(DEFAULT_INSCRIT_ONLINE_YN)
            .emailUser(DEFAULT_EMAIL_USER);
        return processusDinscriptionAdministrative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessusDinscriptionAdministrative createUpdatedEntity(EntityManager em) {
        ProcessusDinscriptionAdministrative processusDinscriptionAdministrative = new ProcessusDinscriptionAdministrative()
            .inscriptionDemarreeYN(UPDATED_INSCRIPTION_DEMARREE_YN)
            .dateDemarrageInscription(UPDATED_DATE_DEMARRAGE_INSCRIPTION)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .dateAnnulationInscription(UPDATED_DATE_ANNULATION_INSCRIPTION)
            .apteYN(UPDATED_APTE_YN)
            .dateVisiteMedicale(UPDATED_DATE_VISITE_MEDICALE)
            .beneficiaireCROUSYN(UPDATED_BENEFICIAIRE_CROUSYN)
            .dateRemiseQuitusCROUS(UPDATED_DATE_REMISE_QUITUS_CROUS)
            .nouveauCodeBUYN(UPDATED_NOUVEAU_CODE_BUYN)
            .quitusBUYN(UPDATED_QUITUS_BUYN)
            .dateRemiseQuitusBU(UPDATED_DATE_REMISE_QUITUS_BU)
            .exporterBUYN(UPDATED_EXPORTER_BUYN)
            .fraisObligatoiresPayesYN(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN)
            .datePaiementFraisObligatoires(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES)
            .numeroQuitusCROUS(UPDATED_NUMERO_QUITUS_CROUS)
            .carteEturemiseYN(UPDATED_CARTE_ETUREMISE_YN)
            .carteDupremiseYN(UPDATED_CARTE_DUPREMISE_YN)
            .dateRemiseCarteEtu(UPDATED_DATE_REMISE_CARTE_ETU)
            .dateRemiseCarteDup(UPDATED_DATE_REMISE_CARTE_DUP)
            .inscritAdministrativementYN(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN)
            .dateInscriptionAdministrative(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE)
            .derniereModification(UPDATED_DERNIERE_MODIFICATION)
            .inscritOnlineYN(UPDATED_INSCRIT_ONLINE_YN)
            .emailUser(UPDATED_EMAIL_USER);
        return processusDinscriptionAdministrative;
    }

    @BeforeEach
    public void initTest() {
        processusDinscriptionAdministrative = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessusDinscriptionAdministrative() throws Exception {
        int databaseSizeBeforeCreate = processusDinscriptionAdministrativeRepository.findAll().size();
        // Create the ProcessusDinscriptionAdministrative
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = processusDinscriptionAdministrativeMapper.toDto(
            processusDinscriptionAdministrative
        );
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusDinscriptionAdministrativeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessusDinscriptionAdministrative testProcessusDinscriptionAdministrative = processusDinscriptionAdministrativeList.get(
            processusDinscriptionAdministrativeList.size() - 1
        );
        assertThat(testProcessusDinscriptionAdministrative.getInscriptionDemarreeYN()).isEqualTo(DEFAULT_INSCRIPTION_DEMARREE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateDemarrageInscription()).isEqualTo(DEFAULT_DATE_DEMARRAGE_INSCRIPTION);
        assertThat(testProcessusDinscriptionAdministrative.getInscriptionAnnuleeYN()).isEqualTo(DEFAULT_INSCRIPTION_ANNULEE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateAnnulationInscription()).isEqualTo(DEFAULT_DATE_ANNULATION_INSCRIPTION);
        assertThat(testProcessusDinscriptionAdministrative.getApteYN()).isEqualTo(DEFAULT_APTE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateVisiteMedicale()).isEqualTo(DEFAULT_DATE_VISITE_MEDICALE);
        assertThat(testProcessusDinscriptionAdministrative.getBeneficiaireCROUSYN()).isEqualTo(DEFAULT_BENEFICIAIRE_CROUSYN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseQuitusCROUS()).isEqualTo(DEFAULT_DATE_REMISE_QUITUS_CROUS);
        assertThat(testProcessusDinscriptionAdministrative.getNouveauCodeBUYN()).isEqualTo(DEFAULT_NOUVEAU_CODE_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getQuitusBUYN()).isEqualTo(DEFAULT_QUITUS_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseQuitusBU()).isEqualTo(DEFAULT_DATE_REMISE_QUITUS_BU);
        assertThat(testProcessusDinscriptionAdministrative.getExporterBUYN()).isEqualTo(DEFAULT_EXPORTER_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getFraisObligatoiresPayesYN()).isEqualTo(DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDatePaiementFraisObligatoires())
            .isEqualTo(DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES);
        assertThat(testProcessusDinscriptionAdministrative.getNumeroQuitusCROUS()).isEqualTo(DEFAULT_NUMERO_QUITUS_CROUS);
        assertThat(testProcessusDinscriptionAdministrative.getCarteEturemiseYN()).isEqualTo(DEFAULT_CARTE_ETUREMISE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getCarteDupremiseYN()).isEqualTo(DEFAULT_CARTE_DUPREMISE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseCarteEtu()).isEqualTo(DEFAULT_DATE_REMISE_CARTE_ETU);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseCarteDup()).isEqualTo(DEFAULT_DATE_REMISE_CARTE_DUP);
        assertThat(testProcessusDinscriptionAdministrative.getInscritAdministrativementYN())
            .isEqualTo(DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateInscriptionAdministrative())
            .isEqualTo(DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE);
        assertThat(testProcessusDinscriptionAdministrative.getDerniereModification()).isEqualTo(DEFAULT_DERNIERE_MODIFICATION);
        assertThat(testProcessusDinscriptionAdministrative.getInscritOnlineYN()).isEqualTo(DEFAULT_INSCRIT_ONLINE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
    }

    @Test
    @Transactional
    void createProcessusDinscriptionAdministrativeWithExistingId() throws Exception {
        // Create the ProcessusDinscriptionAdministrative with an existing ID
        processusDinscriptionAdministrative.setId(1L);
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = processusDinscriptionAdministrativeMapper.toDto(
            processusDinscriptionAdministrative
        );

        int databaseSizeBeforeCreate = processusDinscriptionAdministrativeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusDinscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcessusDinscriptionAdministratives() throws Exception {
        // Initialize the database
        processusDinscriptionAdministrativeRepository.saveAndFlush(processusDinscriptionAdministrative);

        // Get all the processusDinscriptionAdministrativeList
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processusDinscriptionAdministrative.getId().intValue())))
            .andExpect(jsonPath("$.[*].inscriptionDemarreeYN").value(hasItem(DEFAULT_INSCRIPTION_DEMARREE_YN)))
            .andExpect(jsonPath("$.[*].dateDemarrageInscription").value(hasItem(DEFAULT_DATE_DEMARRAGE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].inscriptionAnnuleeYN").value(hasItem(DEFAULT_INSCRIPTION_ANNULEE_YN)))
            .andExpect(jsonPath("$.[*].dateAnnulationInscription").value(hasItem(DEFAULT_DATE_ANNULATION_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].apteYN").value(hasItem(DEFAULT_APTE_YN)))
            .andExpect(jsonPath("$.[*].dateVisiteMedicale").value(hasItem(DEFAULT_DATE_VISITE_MEDICALE.toString())))
            .andExpect(jsonPath("$.[*].beneficiaireCROUSYN").value(hasItem(DEFAULT_BENEFICIAIRE_CROUSYN)))
            .andExpect(jsonPath("$.[*].dateRemiseQuitusCROUS").value(hasItem(DEFAULT_DATE_REMISE_QUITUS_CROUS.toString())))
            .andExpect(jsonPath("$.[*].nouveauCodeBUYN").value(hasItem(DEFAULT_NOUVEAU_CODE_BUYN)))
            .andExpect(jsonPath("$.[*].quitusBUYN").value(hasItem(DEFAULT_QUITUS_BUYN)))
            .andExpect(jsonPath("$.[*].dateRemiseQuitusBU").value(hasItem(DEFAULT_DATE_REMISE_QUITUS_BU.toString())))
            .andExpect(jsonPath("$.[*].exporterBUYN").value(hasItem(DEFAULT_EXPORTER_BUYN)))
            .andExpect(jsonPath("$.[*].fraisObligatoiresPayesYN").value(hasItem(DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN)))
            .andExpect(jsonPath("$.[*].datePaiementFraisObligatoires").value(hasItem(DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES.toString())))
            .andExpect(jsonPath("$.[*].numeroQuitusCROUS").value(hasItem(DEFAULT_NUMERO_QUITUS_CROUS)))
            .andExpect(jsonPath("$.[*].carteEturemiseYN").value(hasItem(DEFAULT_CARTE_ETUREMISE_YN)))
            .andExpect(jsonPath("$.[*].carteDupremiseYN").value(hasItem(DEFAULT_CARTE_DUPREMISE_YN)))
            .andExpect(jsonPath("$.[*].dateRemiseCarteEtu").value(hasItem(DEFAULT_DATE_REMISE_CARTE_ETU.toString())))
            .andExpect(jsonPath("$.[*].dateRemiseCarteDup").value(hasItem(DEFAULT_DATE_REMISE_CARTE_DUP)))
            .andExpect(jsonPath("$.[*].inscritAdministrativementYN").value(hasItem(DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN)))
            .andExpect(jsonPath("$.[*].dateInscriptionAdministrative").value(hasItem(DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE.toString())))
            .andExpect(jsonPath("$.[*].derniereModification").value(hasItem(DEFAULT_DERNIERE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].inscritOnlineYN").value(hasItem(DEFAULT_INSCRIT_ONLINE_YN)))
            .andExpect(jsonPath("$.[*].emailUser").value(hasItem(DEFAULT_EMAIL_USER)));
    }

    @Test
    @Transactional
    void getProcessusDinscriptionAdministrative() throws Exception {
        // Initialize the database
        processusDinscriptionAdministrativeRepository.saveAndFlush(processusDinscriptionAdministrative);

        // Get the processusDinscriptionAdministrative
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(get(ENTITY_API_URL_ID, processusDinscriptionAdministrative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processusDinscriptionAdministrative.getId().intValue()))
            .andExpect(jsonPath("$.inscriptionDemarreeYN").value(DEFAULT_INSCRIPTION_DEMARREE_YN))
            .andExpect(jsonPath("$.dateDemarrageInscription").value(DEFAULT_DATE_DEMARRAGE_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.inscriptionAnnuleeYN").value(DEFAULT_INSCRIPTION_ANNULEE_YN))
            .andExpect(jsonPath("$.dateAnnulationInscription").value(DEFAULT_DATE_ANNULATION_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.apteYN").value(DEFAULT_APTE_YN))
            .andExpect(jsonPath("$.dateVisiteMedicale").value(DEFAULT_DATE_VISITE_MEDICALE.toString()))
            .andExpect(jsonPath("$.beneficiaireCROUSYN").value(DEFAULT_BENEFICIAIRE_CROUSYN))
            .andExpect(jsonPath("$.dateRemiseQuitusCROUS").value(DEFAULT_DATE_REMISE_QUITUS_CROUS.toString()))
            .andExpect(jsonPath("$.nouveauCodeBUYN").value(DEFAULT_NOUVEAU_CODE_BUYN))
            .andExpect(jsonPath("$.quitusBUYN").value(DEFAULT_QUITUS_BUYN))
            .andExpect(jsonPath("$.dateRemiseQuitusBU").value(DEFAULT_DATE_REMISE_QUITUS_BU.toString()))
            .andExpect(jsonPath("$.exporterBUYN").value(DEFAULT_EXPORTER_BUYN))
            .andExpect(jsonPath("$.fraisObligatoiresPayesYN").value(DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN))
            .andExpect(jsonPath("$.datePaiementFraisObligatoires").value(DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES.toString()))
            .andExpect(jsonPath("$.numeroQuitusCROUS").value(DEFAULT_NUMERO_QUITUS_CROUS))
            .andExpect(jsonPath("$.carteEturemiseYN").value(DEFAULT_CARTE_ETUREMISE_YN))
            .andExpect(jsonPath("$.carteDupremiseYN").value(DEFAULT_CARTE_DUPREMISE_YN))
            .andExpect(jsonPath("$.dateRemiseCarteEtu").value(DEFAULT_DATE_REMISE_CARTE_ETU.toString()))
            .andExpect(jsonPath("$.dateRemiseCarteDup").value(DEFAULT_DATE_REMISE_CARTE_DUP))
            .andExpect(jsonPath("$.inscritAdministrativementYN").value(DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN))
            .andExpect(jsonPath("$.dateInscriptionAdministrative").value(DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE.toString()))
            .andExpect(jsonPath("$.derniereModification").value(DEFAULT_DERNIERE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.inscritOnlineYN").value(DEFAULT_INSCRIT_ONLINE_YN))
            .andExpect(jsonPath("$.emailUser").value(DEFAULT_EMAIL_USER));
    }

    @Test
    @Transactional
    void getNonExistingProcessusDinscriptionAdministrative() throws Exception {
        // Get the processusDinscriptionAdministrative
        restProcessusDinscriptionAdministrativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessusDinscriptionAdministrative() throws Exception {
        // Initialize the database
        processusDinscriptionAdministrativeRepository.saveAndFlush(processusDinscriptionAdministrative);

        int databaseSizeBeforeUpdate = processusDinscriptionAdministrativeRepository.findAll().size();

        // Update the processusDinscriptionAdministrative
        ProcessusDinscriptionAdministrative updatedProcessusDinscriptionAdministrative = processusDinscriptionAdministrativeRepository
            .findById(processusDinscriptionAdministrative.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProcessusDinscriptionAdministrative are not directly saved in db
        em.detach(updatedProcessusDinscriptionAdministrative);
        updatedProcessusDinscriptionAdministrative
            .inscriptionDemarreeYN(UPDATED_INSCRIPTION_DEMARREE_YN)
            .dateDemarrageInscription(UPDATED_DATE_DEMARRAGE_INSCRIPTION)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .dateAnnulationInscription(UPDATED_DATE_ANNULATION_INSCRIPTION)
            .apteYN(UPDATED_APTE_YN)
            .dateVisiteMedicale(UPDATED_DATE_VISITE_MEDICALE)
            .beneficiaireCROUSYN(UPDATED_BENEFICIAIRE_CROUSYN)
            .dateRemiseQuitusCROUS(UPDATED_DATE_REMISE_QUITUS_CROUS)
            .nouveauCodeBUYN(UPDATED_NOUVEAU_CODE_BUYN)
            .quitusBUYN(UPDATED_QUITUS_BUYN)
            .dateRemiseQuitusBU(UPDATED_DATE_REMISE_QUITUS_BU)
            .exporterBUYN(UPDATED_EXPORTER_BUYN)
            .fraisObligatoiresPayesYN(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN)
            .datePaiementFraisObligatoires(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES)
            .numeroQuitusCROUS(UPDATED_NUMERO_QUITUS_CROUS)
            .carteEturemiseYN(UPDATED_CARTE_ETUREMISE_YN)
            .carteDupremiseYN(UPDATED_CARTE_DUPREMISE_YN)
            .dateRemiseCarteEtu(UPDATED_DATE_REMISE_CARTE_ETU)
            .dateRemiseCarteDup(UPDATED_DATE_REMISE_CARTE_DUP)
            .inscritAdministrativementYN(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN)
            .dateInscriptionAdministrative(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE)
            .derniereModification(UPDATED_DERNIERE_MODIFICATION)
            .inscritOnlineYN(UPDATED_INSCRIT_ONLINE_YN)
            .emailUser(UPDATED_EMAIL_USER);
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = processusDinscriptionAdministrativeMapper.toDto(
            updatedProcessusDinscriptionAdministrative
        );

        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processusDinscriptionAdministrativeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusDinscriptionAdministrativeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        ProcessusDinscriptionAdministrative testProcessusDinscriptionAdministrative = processusDinscriptionAdministrativeList.get(
            processusDinscriptionAdministrativeList.size() - 1
        );
        assertThat(testProcessusDinscriptionAdministrative.getInscriptionDemarreeYN()).isEqualTo(UPDATED_INSCRIPTION_DEMARREE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateDemarrageInscription()).isEqualTo(UPDATED_DATE_DEMARRAGE_INSCRIPTION);
        assertThat(testProcessusDinscriptionAdministrative.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateAnnulationInscription()).isEqualTo(UPDATED_DATE_ANNULATION_INSCRIPTION);
        assertThat(testProcessusDinscriptionAdministrative.getApteYN()).isEqualTo(UPDATED_APTE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateVisiteMedicale()).isEqualTo(UPDATED_DATE_VISITE_MEDICALE);
        assertThat(testProcessusDinscriptionAdministrative.getBeneficiaireCROUSYN()).isEqualTo(UPDATED_BENEFICIAIRE_CROUSYN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseQuitusCROUS()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_CROUS);
        assertThat(testProcessusDinscriptionAdministrative.getNouveauCodeBUYN()).isEqualTo(UPDATED_NOUVEAU_CODE_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getQuitusBUYN()).isEqualTo(UPDATED_QUITUS_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseQuitusBU()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_BU);
        assertThat(testProcessusDinscriptionAdministrative.getExporterBUYN()).isEqualTo(UPDATED_EXPORTER_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getFraisObligatoiresPayesYN()).isEqualTo(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDatePaiementFraisObligatoires())
            .isEqualTo(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES);
        assertThat(testProcessusDinscriptionAdministrative.getNumeroQuitusCROUS()).isEqualTo(UPDATED_NUMERO_QUITUS_CROUS);
        assertThat(testProcessusDinscriptionAdministrative.getCarteEturemiseYN()).isEqualTo(UPDATED_CARTE_ETUREMISE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getCarteDupremiseYN()).isEqualTo(UPDATED_CARTE_DUPREMISE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseCarteEtu()).isEqualTo(UPDATED_DATE_REMISE_CARTE_ETU);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseCarteDup()).isEqualTo(UPDATED_DATE_REMISE_CARTE_DUP);
        assertThat(testProcessusDinscriptionAdministrative.getInscritAdministrativementYN())
            .isEqualTo(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateInscriptionAdministrative())
            .isEqualTo(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE);
        assertThat(testProcessusDinscriptionAdministrative.getDerniereModification()).isEqualTo(UPDATED_DERNIERE_MODIFICATION);
        assertThat(testProcessusDinscriptionAdministrative.getInscritOnlineYN()).isEqualTo(UPDATED_INSCRIT_ONLINE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void putNonExistingProcessusDinscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusDinscriptionAdministrativeRepository.findAll().size();
        processusDinscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusDinscriptionAdministrative
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = processusDinscriptionAdministrativeMapper.toDto(
            processusDinscriptionAdministrative
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processusDinscriptionAdministrativeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusDinscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessusDinscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusDinscriptionAdministrativeRepository.findAll().size();
        processusDinscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusDinscriptionAdministrative
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = processusDinscriptionAdministrativeMapper.toDto(
            processusDinscriptionAdministrative
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusDinscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessusDinscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusDinscriptionAdministrativeRepository.findAll().size();
        processusDinscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusDinscriptionAdministrative
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = processusDinscriptionAdministrativeMapper.toDto(
            processusDinscriptionAdministrative
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusDinscriptionAdministrativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessusDinscriptionAdministrativeWithPatch() throws Exception {
        // Initialize the database
        processusDinscriptionAdministrativeRepository.saveAndFlush(processusDinscriptionAdministrative);

        int databaseSizeBeforeUpdate = processusDinscriptionAdministrativeRepository.findAll().size();

        // Update the processusDinscriptionAdministrative using partial update
        ProcessusDinscriptionAdministrative partialUpdatedProcessusDinscriptionAdministrative = new ProcessusDinscriptionAdministrative();
        partialUpdatedProcessusDinscriptionAdministrative.setId(processusDinscriptionAdministrative.getId());

        partialUpdatedProcessusDinscriptionAdministrative
            .inscriptionDemarreeYN(UPDATED_INSCRIPTION_DEMARREE_YN)
            .dateDemarrageInscription(UPDATED_DATE_DEMARRAGE_INSCRIPTION)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .dateAnnulationInscription(UPDATED_DATE_ANNULATION_INSCRIPTION)
            .exporterBUYN(UPDATED_EXPORTER_BUYN)
            .fraisObligatoiresPayesYN(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN)
            .datePaiementFraisObligatoires(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES)
            .numeroQuitusCROUS(UPDATED_NUMERO_QUITUS_CROUS)
            .carteDupremiseYN(UPDATED_CARTE_DUPREMISE_YN)
            .dateRemiseCarteEtu(UPDATED_DATE_REMISE_CARTE_ETU)
            .dateRemiseCarteDup(UPDATED_DATE_REMISE_CARTE_DUP)
            .inscritAdministrativementYN(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN)
            .dateInscriptionAdministrative(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE);

        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessusDinscriptionAdministrative.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessusDinscriptionAdministrative))
            )
            .andExpect(status().isOk());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        ProcessusDinscriptionAdministrative testProcessusDinscriptionAdministrative = processusDinscriptionAdministrativeList.get(
            processusDinscriptionAdministrativeList.size() - 1
        );
        assertThat(testProcessusDinscriptionAdministrative.getInscriptionDemarreeYN()).isEqualTo(UPDATED_INSCRIPTION_DEMARREE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateDemarrageInscription()).isEqualTo(UPDATED_DATE_DEMARRAGE_INSCRIPTION);
        assertThat(testProcessusDinscriptionAdministrative.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateAnnulationInscription()).isEqualTo(UPDATED_DATE_ANNULATION_INSCRIPTION);
        assertThat(testProcessusDinscriptionAdministrative.getApteYN()).isEqualTo(DEFAULT_APTE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateVisiteMedicale()).isEqualTo(DEFAULT_DATE_VISITE_MEDICALE);
        assertThat(testProcessusDinscriptionAdministrative.getBeneficiaireCROUSYN()).isEqualTo(DEFAULT_BENEFICIAIRE_CROUSYN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseQuitusCROUS()).isEqualTo(DEFAULT_DATE_REMISE_QUITUS_CROUS);
        assertThat(testProcessusDinscriptionAdministrative.getNouveauCodeBUYN()).isEqualTo(DEFAULT_NOUVEAU_CODE_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getQuitusBUYN()).isEqualTo(DEFAULT_QUITUS_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseQuitusBU()).isEqualTo(DEFAULT_DATE_REMISE_QUITUS_BU);
        assertThat(testProcessusDinscriptionAdministrative.getExporterBUYN()).isEqualTo(UPDATED_EXPORTER_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getFraisObligatoiresPayesYN()).isEqualTo(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDatePaiementFraisObligatoires())
            .isEqualTo(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES);
        assertThat(testProcessusDinscriptionAdministrative.getNumeroQuitusCROUS()).isEqualTo(UPDATED_NUMERO_QUITUS_CROUS);
        assertThat(testProcessusDinscriptionAdministrative.getCarteEturemiseYN()).isEqualTo(DEFAULT_CARTE_ETUREMISE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getCarteDupremiseYN()).isEqualTo(UPDATED_CARTE_DUPREMISE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseCarteEtu()).isEqualTo(UPDATED_DATE_REMISE_CARTE_ETU);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseCarteDup()).isEqualTo(UPDATED_DATE_REMISE_CARTE_DUP);
        assertThat(testProcessusDinscriptionAdministrative.getInscritAdministrativementYN())
            .isEqualTo(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateInscriptionAdministrative())
            .isEqualTo(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE);
        assertThat(testProcessusDinscriptionAdministrative.getDerniereModification()).isEqualTo(DEFAULT_DERNIERE_MODIFICATION);
        assertThat(testProcessusDinscriptionAdministrative.getInscritOnlineYN()).isEqualTo(DEFAULT_INSCRIT_ONLINE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
    }

    @Test
    @Transactional
    void fullUpdateProcessusDinscriptionAdministrativeWithPatch() throws Exception {
        // Initialize the database
        processusDinscriptionAdministrativeRepository.saveAndFlush(processusDinscriptionAdministrative);

        int databaseSizeBeforeUpdate = processusDinscriptionAdministrativeRepository.findAll().size();

        // Update the processusDinscriptionAdministrative using partial update
        ProcessusDinscriptionAdministrative partialUpdatedProcessusDinscriptionAdministrative = new ProcessusDinscriptionAdministrative();
        partialUpdatedProcessusDinscriptionAdministrative.setId(processusDinscriptionAdministrative.getId());

        partialUpdatedProcessusDinscriptionAdministrative
            .inscriptionDemarreeYN(UPDATED_INSCRIPTION_DEMARREE_YN)
            .dateDemarrageInscription(UPDATED_DATE_DEMARRAGE_INSCRIPTION)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .dateAnnulationInscription(UPDATED_DATE_ANNULATION_INSCRIPTION)
            .apteYN(UPDATED_APTE_YN)
            .dateVisiteMedicale(UPDATED_DATE_VISITE_MEDICALE)
            .beneficiaireCROUSYN(UPDATED_BENEFICIAIRE_CROUSYN)
            .dateRemiseQuitusCROUS(UPDATED_DATE_REMISE_QUITUS_CROUS)
            .nouveauCodeBUYN(UPDATED_NOUVEAU_CODE_BUYN)
            .quitusBUYN(UPDATED_QUITUS_BUYN)
            .dateRemiseQuitusBU(UPDATED_DATE_REMISE_QUITUS_BU)
            .exporterBUYN(UPDATED_EXPORTER_BUYN)
            .fraisObligatoiresPayesYN(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN)
            .datePaiementFraisObligatoires(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES)
            .numeroQuitusCROUS(UPDATED_NUMERO_QUITUS_CROUS)
            .carteEturemiseYN(UPDATED_CARTE_ETUREMISE_YN)
            .carteDupremiseYN(UPDATED_CARTE_DUPREMISE_YN)
            .dateRemiseCarteEtu(UPDATED_DATE_REMISE_CARTE_ETU)
            .dateRemiseCarteDup(UPDATED_DATE_REMISE_CARTE_DUP)
            .inscritAdministrativementYN(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN)
            .dateInscriptionAdministrative(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE)
            .derniereModification(UPDATED_DERNIERE_MODIFICATION)
            .inscritOnlineYN(UPDATED_INSCRIT_ONLINE_YN)
            .emailUser(UPDATED_EMAIL_USER);

        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessusDinscriptionAdministrative.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessusDinscriptionAdministrative))
            )
            .andExpect(status().isOk());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        ProcessusDinscriptionAdministrative testProcessusDinscriptionAdministrative = processusDinscriptionAdministrativeList.get(
            processusDinscriptionAdministrativeList.size() - 1
        );
        assertThat(testProcessusDinscriptionAdministrative.getInscriptionDemarreeYN()).isEqualTo(UPDATED_INSCRIPTION_DEMARREE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateDemarrageInscription()).isEqualTo(UPDATED_DATE_DEMARRAGE_INSCRIPTION);
        assertThat(testProcessusDinscriptionAdministrative.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateAnnulationInscription()).isEqualTo(UPDATED_DATE_ANNULATION_INSCRIPTION);
        assertThat(testProcessusDinscriptionAdministrative.getApteYN()).isEqualTo(UPDATED_APTE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateVisiteMedicale()).isEqualTo(UPDATED_DATE_VISITE_MEDICALE);
        assertThat(testProcessusDinscriptionAdministrative.getBeneficiaireCROUSYN()).isEqualTo(UPDATED_BENEFICIAIRE_CROUSYN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseQuitusCROUS()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_CROUS);
        assertThat(testProcessusDinscriptionAdministrative.getNouveauCodeBUYN()).isEqualTo(UPDATED_NOUVEAU_CODE_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getQuitusBUYN()).isEqualTo(UPDATED_QUITUS_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseQuitusBU()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_BU);
        assertThat(testProcessusDinscriptionAdministrative.getExporterBUYN()).isEqualTo(UPDATED_EXPORTER_BUYN);
        assertThat(testProcessusDinscriptionAdministrative.getFraisObligatoiresPayesYN()).isEqualTo(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDatePaiementFraisObligatoires())
            .isEqualTo(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES);
        assertThat(testProcessusDinscriptionAdministrative.getNumeroQuitusCROUS()).isEqualTo(UPDATED_NUMERO_QUITUS_CROUS);
        assertThat(testProcessusDinscriptionAdministrative.getCarteEturemiseYN()).isEqualTo(UPDATED_CARTE_ETUREMISE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getCarteDupremiseYN()).isEqualTo(UPDATED_CARTE_DUPREMISE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseCarteEtu()).isEqualTo(UPDATED_DATE_REMISE_CARTE_ETU);
        assertThat(testProcessusDinscriptionAdministrative.getDateRemiseCarteDup()).isEqualTo(UPDATED_DATE_REMISE_CARTE_DUP);
        assertThat(testProcessusDinscriptionAdministrative.getInscritAdministrativementYN())
            .isEqualTo(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN);
        assertThat(testProcessusDinscriptionAdministrative.getDateInscriptionAdministrative())
            .isEqualTo(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE);
        assertThat(testProcessusDinscriptionAdministrative.getDerniereModification()).isEqualTo(UPDATED_DERNIERE_MODIFICATION);
        assertThat(testProcessusDinscriptionAdministrative.getInscritOnlineYN()).isEqualTo(UPDATED_INSCRIT_ONLINE_YN);
        assertThat(testProcessusDinscriptionAdministrative.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void patchNonExistingProcessusDinscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusDinscriptionAdministrativeRepository.findAll().size();
        processusDinscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusDinscriptionAdministrative
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = processusDinscriptionAdministrativeMapper.toDto(
            processusDinscriptionAdministrative
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processusDinscriptionAdministrativeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processusDinscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessusDinscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusDinscriptionAdministrativeRepository.findAll().size();
        processusDinscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusDinscriptionAdministrative
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = processusDinscriptionAdministrativeMapper.toDto(
            processusDinscriptionAdministrative
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processusDinscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessusDinscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusDinscriptionAdministrativeRepository.findAll().size();
        processusDinscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusDinscriptionAdministrative
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO = processusDinscriptionAdministrativeMapper.toDto(
            processusDinscriptionAdministrative
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processusDinscriptionAdministrativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessusDinscriptionAdministrative in the database
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessusDinscriptionAdministrative() throws Exception {
        // Initialize the database
        processusDinscriptionAdministrativeRepository.saveAndFlush(processusDinscriptionAdministrative);

        int databaseSizeBeforeDelete = processusDinscriptionAdministrativeRepository.findAll().size();

        // Delete the processusDinscriptionAdministrative
        restProcessusDinscriptionAdministrativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, processusDinscriptionAdministrative.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessusDinscriptionAdministrative> processusDinscriptionAdministrativeList =
            processusDinscriptionAdministrativeRepository.findAll();
        assertThat(processusDinscriptionAdministrativeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
