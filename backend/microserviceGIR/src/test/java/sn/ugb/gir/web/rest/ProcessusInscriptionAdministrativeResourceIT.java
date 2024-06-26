package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.ProcessusInscriptionAdministrative;
import sn.ugb.gir.repository.ProcessusInscriptionAdministrativeRepository;
import sn.ugb.gir.repository.search.ProcessusInscriptionAdministrativeSearchRepository;
import sn.ugb.gir.service.dto.ProcessusInscriptionAdministrativeDTO;
import sn.ugb.gir.service.mapper.ProcessusInscriptionAdministrativeMapper;

/**
 * Integration tests for the {@link ProcessusInscriptionAdministrativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessusInscriptionAdministrativeResourceIT {

    private static final Boolean DEFAULT_INSCRIPTION_DEMARREE_YN = false;
    private static final Boolean UPDATED_INSCRIPTION_DEMARREE_YN = true;

    private static final Instant DEFAULT_DATE_DEMARRAGE_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEMARRAGE_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_INSCRIPTION_ANNULEE_YN = false;
    private static final Boolean UPDATED_INSCRIPTION_ANNULEE_YN = true;

    private static final Instant DEFAULT_DATE_ANNULATION_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ANNULATION_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_APTE_YN = false;
    private static final Boolean UPDATED_APTE_YN = true;

    private static final Instant DEFAULT_DATE_VISITE_MEDICALE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_VISITE_MEDICALE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_BENEFICIAIRE_CROUSYN = false;
    private static final Boolean UPDATED_BENEFICIAIRE_CROUSYN = true;

    private static final Instant DEFAULT_DATE_REMISE_QUITUS_CROUS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REMISE_QUITUS_CROUS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_NOUVEAU_CODE_BUYN = false;
    private static final Boolean UPDATED_NOUVEAU_CODE_BUYN = true;

    private static final Boolean DEFAULT_QUITUS_BUYN = false;
    private static final Boolean UPDATED_QUITUS_BUYN = true;

    private static final Instant DEFAULT_DATE_REMISE_QUITUS_BU = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REMISE_QUITUS_BU = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_EXPORTER_BUYN = false;
    private static final Boolean UPDATED_EXPORTER_BUYN = true;

    private static final Boolean DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN = false;
    private static final Boolean UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN = true;

    private static final Instant DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NUMERO_QUITUS_CROUS = 1;
    private static final Integer UPDATED_NUMERO_QUITUS_CROUS = 2;

    private static final Boolean DEFAULT_CARTE_ETUREMISE_YN = false;
    private static final Boolean UPDATED_CARTE_ETUREMISE_YN = true;

    private static final Boolean DEFAULT_CARTE_DUPREMISE_YN = false;
    private static final Boolean UPDATED_CARTE_DUPREMISE_YN = true;

    private static final Instant DEFAULT_DATE_REMISE_CARTE_ETU = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REMISE_CARTE_ETU = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DATE_REMISE_CARTE_DUP = 1;
    private static final Integer UPDATED_DATE_REMISE_CARTE_DUP = 2;

    private static final Boolean DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN = false;
    private static final Boolean UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN = true;

    private static final Instant DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DERNIERE_MODIFICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DERNIERE_MODIFICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_INSCRIT_ONLINE_YN = false;
    private static final Boolean UPDATED_INSCRIT_ONLINE_YN = true;

    private static final String DEFAULT_EMAIL_USER = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/processus-inscription-administratives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/processus-inscription-administratives/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessusInscriptionAdministrativeRepository processusInscriptionAdministrativeRepository;

    @Autowired
    private ProcessusInscriptionAdministrativeMapper processusInscriptionAdministrativeMapper;

    @Autowired
    private ProcessusInscriptionAdministrativeSearchRepository processusInscriptionAdministrativeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessusInscriptionAdministrativeMockMvc;

    private ProcessusInscriptionAdministrative processusInscriptionAdministrative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessusInscriptionAdministrative createEntity(EntityManager em) {
        ProcessusInscriptionAdministrative processusInscriptionAdministrative = new ProcessusInscriptionAdministrative()
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
        return processusInscriptionAdministrative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessusInscriptionAdministrative createUpdatedEntity(EntityManager em) {
        ProcessusInscriptionAdministrative processusInscriptionAdministrative = new ProcessusInscriptionAdministrative()
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
        return processusInscriptionAdministrative;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        processusInscriptionAdministrativeSearchRepository.deleteAll();
        assertThat(processusInscriptionAdministrativeSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        processusInscriptionAdministrative = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessusInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeCreate = processusInscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        // Create the ProcessusInscriptionAdministrative
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = processusInscriptionAdministrativeMapper.toDto(
            processusInscriptionAdministrative
        );
        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusInscriptionAdministrativeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ProcessusInscriptionAdministrative testProcessusInscriptionAdministrative = processusInscriptionAdministrativeList.get(
            processusInscriptionAdministrativeList.size() - 1
        );
        assertThat(testProcessusInscriptionAdministrative.getInscriptionDemarreeYN()).isEqualTo(DEFAULT_INSCRIPTION_DEMARREE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateDemarrageInscription()).isEqualTo(DEFAULT_DATE_DEMARRAGE_INSCRIPTION);
        assertThat(testProcessusInscriptionAdministrative.getInscriptionAnnuleeYN()).isEqualTo(DEFAULT_INSCRIPTION_ANNULEE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateAnnulationInscription()).isEqualTo(DEFAULT_DATE_ANNULATION_INSCRIPTION);
        assertThat(testProcessusInscriptionAdministrative.getApteYN()).isEqualTo(DEFAULT_APTE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateVisiteMedicale()).isEqualTo(DEFAULT_DATE_VISITE_MEDICALE);
        assertThat(testProcessusInscriptionAdministrative.getBeneficiaireCROUSYN()).isEqualTo(DEFAULT_BENEFICIAIRE_CROUSYN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseQuitusCROUS()).isEqualTo(DEFAULT_DATE_REMISE_QUITUS_CROUS);
        assertThat(testProcessusInscriptionAdministrative.getNouveauCodeBUYN()).isEqualTo(DEFAULT_NOUVEAU_CODE_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getQuitusBUYN()).isEqualTo(DEFAULT_QUITUS_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseQuitusBU()).isEqualTo(DEFAULT_DATE_REMISE_QUITUS_BU);
        assertThat(testProcessusInscriptionAdministrative.getExporterBUYN()).isEqualTo(DEFAULT_EXPORTER_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getFraisObligatoiresPayesYN()).isEqualTo(DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN);
        assertThat(testProcessusInscriptionAdministrative.getDatePaiementFraisObligatoires())
            .isEqualTo(DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES);
        assertThat(testProcessusInscriptionAdministrative.getNumeroQuitusCROUS()).isEqualTo(DEFAULT_NUMERO_QUITUS_CROUS);
        assertThat(testProcessusInscriptionAdministrative.getCarteEturemiseYN()).isEqualTo(DEFAULT_CARTE_ETUREMISE_YN);
        assertThat(testProcessusInscriptionAdministrative.getCarteDupremiseYN()).isEqualTo(DEFAULT_CARTE_DUPREMISE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseCarteEtu()).isEqualTo(DEFAULT_DATE_REMISE_CARTE_ETU);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseCarteDup()).isEqualTo(DEFAULT_DATE_REMISE_CARTE_DUP);
        assertThat(testProcessusInscriptionAdministrative.getInscritAdministrativementYN())
            .isEqualTo(DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateInscriptionAdministrative())
            .isEqualTo(DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE);
        assertThat(testProcessusInscriptionAdministrative.getDerniereModification()).isEqualTo(DEFAULT_DERNIERE_MODIFICATION);
        assertThat(testProcessusInscriptionAdministrative.getInscritOnlineYN()).isEqualTo(DEFAULT_INSCRIT_ONLINE_YN);
        assertThat(testProcessusInscriptionAdministrative.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
    }

    @Test
    @Transactional
    void createProcessusInscriptionAdministrativeWithExistingId() throws Exception {
        // Create the ProcessusInscriptionAdministrative with an existing ID
        processusInscriptionAdministrative.setId(1L);
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = processusInscriptionAdministrativeMapper.toDto(
            processusInscriptionAdministrative
        );

        int databaseSizeBeforeCreate = processusInscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusInscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProcessusInscriptionAdministratives() throws Exception {
        // Initialize the database
        processusInscriptionAdministrativeRepository.saveAndFlush(processusInscriptionAdministrative);

        // Get all the processusInscriptionAdministrativeList
        restProcessusInscriptionAdministrativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processusInscriptionAdministrative.getId().intValue())))
            .andExpect(jsonPath("$.[*].inscriptionDemarreeYN").value(hasItem(DEFAULT_INSCRIPTION_DEMARREE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDemarrageInscription").value(hasItem(DEFAULT_DATE_DEMARRAGE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].inscriptionAnnuleeYN").value(hasItem(DEFAULT_INSCRIPTION_ANNULEE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAnnulationInscription").value(hasItem(DEFAULT_DATE_ANNULATION_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].apteYN").value(hasItem(DEFAULT_APTE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateVisiteMedicale").value(hasItem(DEFAULT_DATE_VISITE_MEDICALE.toString())))
            .andExpect(jsonPath("$.[*].beneficiaireCROUSYN").value(hasItem(DEFAULT_BENEFICIAIRE_CROUSYN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateRemiseQuitusCROUS").value(hasItem(DEFAULT_DATE_REMISE_QUITUS_CROUS.toString())))
            .andExpect(jsonPath("$.[*].nouveauCodeBUYN").value(hasItem(DEFAULT_NOUVEAU_CODE_BUYN.booleanValue())))
            .andExpect(jsonPath("$.[*].quitusBUYN").value(hasItem(DEFAULT_QUITUS_BUYN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateRemiseQuitusBU").value(hasItem(DEFAULT_DATE_REMISE_QUITUS_BU.toString())))
            .andExpect(jsonPath("$.[*].exporterBUYN").value(hasItem(DEFAULT_EXPORTER_BUYN.booleanValue())))
            .andExpect(jsonPath("$.[*].fraisObligatoiresPayesYN").value(hasItem(DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].datePaiementFraisObligatoires").value(hasItem(DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES.toString())))
            .andExpect(jsonPath("$.[*].numeroQuitusCROUS").value(hasItem(DEFAULT_NUMERO_QUITUS_CROUS)))
            .andExpect(jsonPath("$.[*].carteEturemiseYN").value(hasItem(DEFAULT_CARTE_ETUREMISE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].carteDupremiseYN").value(hasItem(DEFAULT_CARTE_DUPREMISE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateRemiseCarteEtu").value(hasItem(DEFAULT_DATE_REMISE_CARTE_ETU.toString())))
            .andExpect(jsonPath("$.[*].dateRemiseCarteDup").value(hasItem(DEFAULT_DATE_REMISE_CARTE_DUP)))
            .andExpect(jsonPath("$.[*].inscritAdministrativementYN").value(hasItem(DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateInscriptionAdministrative").value(hasItem(DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE.toString())))
            .andExpect(jsonPath("$.[*].derniereModification").value(hasItem(DEFAULT_DERNIERE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].inscritOnlineYN").value(hasItem(DEFAULT_INSCRIT_ONLINE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].emailUser").value(hasItem(DEFAULT_EMAIL_USER)));
    }

    @Test
    @Transactional
    void getProcessusInscriptionAdministrative() throws Exception {
        // Initialize the database
        processusInscriptionAdministrativeRepository.saveAndFlush(processusInscriptionAdministrative);

        // Get the processusInscriptionAdministrative
        restProcessusInscriptionAdministrativeMockMvc
            .perform(get(ENTITY_API_URL_ID, processusInscriptionAdministrative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processusInscriptionAdministrative.getId().intValue()))
            .andExpect(jsonPath("$.inscriptionDemarreeYN").value(DEFAULT_INSCRIPTION_DEMARREE_YN.booleanValue()))
            .andExpect(jsonPath("$.dateDemarrageInscription").value(DEFAULT_DATE_DEMARRAGE_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.inscriptionAnnuleeYN").value(DEFAULT_INSCRIPTION_ANNULEE_YN.booleanValue()))
            .andExpect(jsonPath("$.dateAnnulationInscription").value(DEFAULT_DATE_ANNULATION_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.apteYN").value(DEFAULT_APTE_YN.booleanValue()))
            .andExpect(jsonPath("$.dateVisiteMedicale").value(DEFAULT_DATE_VISITE_MEDICALE.toString()))
            .andExpect(jsonPath("$.beneficiaireCROUSYN").value(DEFAULT_BENEFICIAIRE_CROUSYN.booleanValue()))
            .andExpect(jsonPath("$.dateRemiseQuitusCROUS").value(DEFAULT_DATE_REMISE_QUITUS_CROUS.toString()))
            .andExpect(jsonPath("$.nouveauCodeBUYN").value(DEFAULT_NOUVEAU_CODE_BUYN.booleanValue()))
            .andExpect(jsonPath("$.quitusBUYN").value(DEFAULT_QUITUS_BUYN.booleanValue()))
            .andExpect(jsonPath("$.dateRemiseQuitusBU").value(DEFAULT_DATE_REMISE_QUITUS_BU.toString()))
            .andExpect(jsonPath("$.exporterBUYN").value(DEFAULT_EXPORTER_BUYN.booleanValue()))
            .andExpect(jsonPath("$.fraisObligatoiresPayesYN").value(DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN.booleanValue()))
            .andExpect(jsonPath("$.datePaiementFraisObligatoires").value(DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES.toString()))
            .andExpect(jsonPath("$.numeroQuitusCROUS").value(DEFAULT_NUMERO_QUITUS_CROUS))
            .andExpect(jsonPath("$.carteEturemiseYN").value(DEFAULT_CARTE_ETUREMISE_YN.booleanValue()))
            .andExpect(jsonPath("$.carteDupremiseYN").value(DEFAULT_CARTE_DUPREMISE_YN.booleanValue()))
            .andExpect(jsonPath("$.dateRemiseCarteEtu").value(DEFAULT_DATE_REMISE_CARTE_ETU.toString()))
            .andExpect(jsonPath("$.dateRemiseCarteDup").value(DEFAULT_DATE_REMISE_CARTE_DUP))
            .andExpect(jsonPath("$.inscritAdministrativementYN").value(DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN.booleanValue()))
            .andExpect(jsonPath("$.dateInscriptionAdministrative").value(DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE.toString()))
            .andExpect(jsonPath("$.derniereModification").value(DEFAULT_DERNIERE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.inscritOnlineYN").value(DEFAULT_INSCRIT_ONLINE_YN.booleanValue()))
            .andExpect(jsonPath("$.emailUser").value(DEFAULT_EMAIL_USER));
    }

    @Test
    @Transactional
    void getNonExistingProcessusInscriptionAdministrative() throws Exception {
        // Get the processusInscriptionAdministrative
        restProcessusInscriptionAdministrativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessusInscriptionAdministrative() throws Exception {
        // Initialize the database
        processusInscriptionAdministrativeRepository.saveAndFlush(processusInscriptionAdministrative);

        int databaseSizeBeforeUpdate = processusInscriptionAdministrativeRepository.findAll().size();
        processusInscriptionAdministrativeSearchRepository.save(processusInscriptionAdministrative);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());

        // Update the processusInscriptionAdministrative
        ProcessusInscriptionAdministrative updatedProcessusInscriptionAdministrative = processusInscriptionAdministrativeRepository
            .findById(processusInscriptionAdministrative.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProcessusInscriptionAdministrative are not directly saved in db
        em.detach(updatedProcessusInscriptionAdministrative);
        updatedProcessusInscriptionAdministrative
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
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = processusInscriptionAdministrativeMapper.toDto(
            updatedProcessusInscriptionAdministrative
        );

        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processusInscriptionAdministrativeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusInscriptionAdministrativeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        ProcessusInscriptionAdministrative testProcessusInscriptionAdministrative = processusInscriptionAdministrativeList.get(
            processusInscriptionAdministrativeList.size() - 1
        );
        assertThat(testProcessusInscriptionAdministrative.getInscriptionDemarreeYN()).isEqualTo(UPDATED_INSCRIPTION_DEMARREE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateDemarrageInscription()).isEqualTo(UPDATED_DATE_DEMARRAGE_INSCRIPTION);
        assertThat(testProcessusInscriptionAdministrative.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateAnnulationInscription()).isEqualTo(UPDATED_DATE_ANNULATION_INSCRIPTION);
        assertThat(testProcessusInscriptionAdministrative.getApteYN()).isEqualTo(UPDATED_APTE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateVisiteMedicale()).isEqualTo(UPDATED_DATE_VISITE_MEDICALE);
        assertThat(testProcessusInscriptionAdministrative.getBeneficiaireCROUSYN()).isEqualTo(UPDATED_BENEFICIAIRE_CROUSYN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseQuitusCROUS()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_CROUS);
        assertThat(testProcessusInscriptionAdministrative.getNouveauCodeBUYN()).isEqualTo(UPDATED_NOUVEAU_CODE_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getQuitusBUYN()).isEqualTo(UPDATED_QUITUS_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseQuitusBU()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_BU);
        assertThat(testProcessusInscriptionAdministrative.getExporterBUYN()).isEqualTo(UPDATED_EXPORTER_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getFraisObligatoiresPayesYN()).isEqualTo(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN);
        assertThat(testProcessusInscriptionAdministrative.getDatePaiementFraisObligatoires())
            .isEqualTo(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES);
        assertThat(testProcessusInscriptionAdministrative.getNumeroQuitusCROUS()).isEqualTo(UPDATED_NUMERO_QUITUS_CROUS);
        assertThat(testProcessusInscriptionAdministrative.getCarteEturemiseYN()).isEqualTo(UPDATED_CARTE_ETUREMISE_YN);
        assertThat(testProcessusInscriptionAdministrative.getCarteDupremiseYN()).isEqualTo(UPDATED_CARTE_DUPREMISE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseCarteEtu()).isEqualTo(UPDATED_DATE_REMISE_CARTE_ETU);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseCarteDup()).isEqualTo(UPDATED_DATE_REMISE_CARTE_DUP);
        assertThat(testProcessusInscriptionAdministrative.getInscritAdministrativementYN())
            .isEqualTo(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateInscriptionAdministrative())
            .isEqualTo(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE);
        assertThat(testProcessusInscriptionAdministrative.getDerniereModification()).isEqualTo(UPDATED_DERNIERE_MODIFICATION);
        assertThat(testProcessusInscriptionAdministrative.getInscritOnlineYN()).isEqualTo(UPDATED_INSCRIT_ONLINE_YN);
        assertThat(testProcessusInscriptionAdministrative.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeSearchList = IterableUtils.toList(
                    processusInscriptionAdministrativeSearchRepository.findAll()
                );
                ProcessusInscriptionAdministrative testProcessusInscriptionAdministrativeSearch =
                    processusInscriptionAdministrativeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testProcessusInscriptionAdministrativeSearch.getInscriptionDemarreeYN())
                    .isEqualTo(UPDATED_INSCRIPTION_DEMARREE_YN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDateDemarrageInscription())
                    .isEqualTo(UPDATED_DATE_DEMARRAGE_INSCRIPTION);
                assertThat(testProcessusInscriptionAdministrativeSearch.getInscriptionAnnuleeYN())
                    .isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDateAnnulationInscription())
                    .isEqualTo(UPDATED_DATE_ANNULATION_INSCRIPTION);
                assertThat(testProcessusInscriptionAdministrativeSearch.getApteYN()).isEqualTo(UPDATED_APTE_YN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDateVisiteMedicale()).isEqualTo(UPDATED_DATE_VISITE_MEDICALE);
                assertThat(testProcessusInscriptionAdministrativeSearch.getBeneficiaireCROUSYN()).isEqualTo(UPDATED_BENEFICIAIRE_CROUSYN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDateRemiseQuitusCROUS())
                    .isEqualTo(UPDATED_DATE_REMISE_QUITUS_CROUS);
                assertThat(testProcessusInscriptionAdministrativeSearch.getNouveauCodeBUYN()).isEqualTo(UPDATED_NOUVEAU_CODE_BUYN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getQuitusBUYN()).isEqualTo(UPDATED_QUITUS_BUYN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDateRemiseQuitusBU()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_BU);
                assertThat(testProcessusInscriptionAdministrativeSearch.getExporterBUYN()).isEqualTo(UPDATED_EXPORTER_BUYN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getFraisObligatoiresPayesYN())
                    .isEqualTo(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDatePaiementFraisObligatoires())
                    .isEqualTo(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES);
                assertThat(testProcessusInscriptionAdministrativeSearch.getNumeroQuitusCROUS()).isEqualTo(UPDATED_NUMERO_QUITUS_CROUS);
                assertThat(testProcessusInscriptionAdministrativeSearch.getCarteEturemiseYN()).isEqualTo(UPDATED_CARTE_ETUREMISE_YN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getCarteDupremiseYN()).isEqualTo(UPDATED_CARTE_DUPREMISE_YN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDateRemiseCarteEtu()).isEqualTo(UPDATED_DATE_REMISE_CARTE_ETU);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDateRemiseCarteDup()).isEqualTo(UPDATED_DATE_REMISE_CARTE_DUP);
                assertThat(testProcessusInscriptionAdministrativeSearch.getInscritAdministrativementYN())
                    .isEqualTo(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDateInscriptionAdministrative())
                    .isEqualTo(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE);
                assertThat(testProcessusInscriptionAdministrativeSearch.getDerniereModification()).isEqualTo(UPDATED_DERNIERE_MODIFICATION);
                assertThat(testProcessusInscriptionAdministrativeSearch.getInscritOnlineYN()).isEqualTo(UPDATED_INSCRIT_ONLINE_YN);
                assertThat(testProcessusInscriptionAdministrativeSearch.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
            });
    }

    @Test
    @Transactional
    void putNonExistingProcessusInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusInscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        processusInscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusInscriptionAdministrative
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = processusInscriptionAdministrativeMapper.toDto(
            processusInscriptionAdministrative
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processusInscriptionAdministrativeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusInscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessusInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusInscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        processusInscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusInscriptionAdministrative
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = processusInscriptionAdministrativeMapper.toDto(
            processusInscriptionAdministrative
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusInscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessusInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusInscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        processusInscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusInscriptionAdministrative
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = processusInscriptionAdministrativeMapper.toDto(
            processusInscriptionAdministrative
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processusInscriptionAdministrativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProcessusInscriptionAdministrativeWithPatch() throws Exception {
        // Initialize the database
        processusInscriptionAdministrativeRepository.saveAndFlush(processusInscriptionAdministrative);

        int databaseSizeBeforeUpdate = processusInscriptionAdministrativeRepository.findAll().size();

        // Update the processusInscriptionAdministrative using partial update
        ProcessusInscriptionAdministrative partialUpdatedProcessusInscriptionAdministrative = new ProcessusInscriptionAdministrative();
        partialUpdatedProcessusInscriptionAdministrative.setId(processusInscriptionAdministrative.getId());

        partialUpdatedProcessusInscriptionAdministrative
            .inscriptionDemarreeYN(UPDATED_INSCRIPTION_DEMARREE_YN)
            .dateDemarrageInscription(UPDATED_DATE_DEMARRAGE_INSCRIPTION)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .dateAnnulationInscription(UPDATED_DATE_ANNULATION_INSCRIPTION)
            .apteYN(UPDATED_APTE_YN)
            .beneficiaireCROUSYN(UPDATED_BENEFICIAIRE_CROUSYN)
            .dateRemiseQuitusCROUS(UPDATED_DATE_REMISE_QUITUS_CROUS)
            .nouveauCodeBUYN(UPDATED_NOUVEAU_CODE_BUYN)
            .dateRemiseQuitusBU(UPDATED_DATE_REMISE_QUITUS_BU)
            .exporterBUYN(UPDATED_EXPORTER_BUYN)
            .numeroQuitusCROUS(UPDATED_NUMERO_QUITUS_CROUS)
            .carteEturemiseYN(UPDATED_CARTE_ETUREMISE_YN)
            .dateRemiseCarteEtu(UPDATED_DATE_REMISE_CARTE_ETU)
            .inscritAdministrativementYN(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN)
            .derniereModification(UPDATED_DERNIERE_MODIFICATION)
            .inscritOnlineYN(UPDATED_INSCRIT_ONLINE_YN)
            .emailUser(UPDATED_EMAIL_USER);

        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessusInscriptionAdministrative.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessusInscriptionAdministrative))
            )
            .andExpect(status().isOk());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        ProcessusInscriptionAdministrative testProcessusInscriptionAdministrative = processusInscriptionAdministrativeList.get(
            processusInscriptionAdministrativeList.size() - 1
        );
        assertThat(testProcessusInscriptionAdministrative.getInscriptionDemarreeYN()).isEqualTo(UPDATED_INSCRIPTION_DEMARREE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateDemarrageInscription()).isEqualTo(UPDATED_DATE_DEMARRAGE_INSCRIPTION);
        assertThat(testProcessusInscriptionAdministrative.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateAnnulationInscription()).isEqualTo(UPDATED_DATE_ANNULATION_INSCRIPTION);
        assertThat(testProcessusInscriptionAdministrative.getApteYN()).isEqualTo(UPDATED_APTE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateVisiteMedicale()).isEqualTo(DEFAULT_DATE_VISITE_MEDICALE);
        assertThat(testProcessusInscriptionAdministrative.getBeneficiaireCROUSYN()).isEqualTo(UPDATED_BENEFICIAIRE_CROUSYN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseQuitusCROUS()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_CROUS);
        assertThat(testProcessusInscriptionAdministrative.getNouveauCodeBUYN()).isEqualTo(UPDATED_NOUVEAU_CODE_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getQuitusBUYN()).isEqualTo(DEFAULT_QUITUS_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseQuitusBU()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_BU);
        assertThat(testProcessusInscriptionAdministrative.getExporterBUYN()).isEqualTo(UPDATED_EXPORTER_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getFraisObligatoiresPayesYN()).isEqualTo(DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN);
        assertThat(testProcessusInscriptionAdministrative.getDatePaiementFraisObligatoires())
            .isEqualTo(DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES);
        assertThat(testProcessusInscriptionAdministrative.getNumeroQuitusCROUS()).isEqualTo(UPDATED_NUMERO_QUITUS_CROUS);
        assertThat(testProcessusInscriptionAdministrative.getCarteEturemiseYN()).isEqualTo(UPDATED_CARTE_ETUREMISE_YN);
        assertThat(testProcessusInscriptionAdministrative.getCarteDupremiseYN()).isEqualTo(DEFAULT_CARTE_DUPREMISE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseCarteEtu()).isEqualTo(UPDATED_DATE_REMISE_CARTE_ETU);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseCarteDup()).isEqualTo(DEFAULT_DATE_REMISE_CARTE_DUP);
        assertThat(testProcessusInscriptionAdministrative.getInscritAdministrativementYN())
            .isEqualTo(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateInscriptionAdministrative())
            .isEqualTo(DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE);
        assertThat(testProcessusInscriptionAdministrative.getDerniereModification()).isEqualTo(UPDATED_DERNIERE_MODIFICATION);
        assertThat(testProcessusInscriptionAdministrative.getInscritOnlineYN()).isEqualTo(UPDATED_INSCRIT_ONLINE_YN);
        assertThat(testProcessusInscriptionAdministrative.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void fullUpdateProcessusInscriptionAdministrativeWithPatch() throws Exception {
        // Initialize the database
        processusInscriptionAdministrativeRepository.saveAndFlush(processusInscriptionAdministrative);

        int databaseSizeBeforeUpdate = processusInscriptionAdministrativeRepository.findAll().size();

        // Update the processusInscriptionAdministrative using partial update
        ProcessusInscriptionAdministrative partialUpdatedProcessusInscriptionAdministrative = new ProcessusInscriptionAdministrative();
        partialUpdatedProcessusInscriptionAdministrative.setId(processusInscriptionAdministrative.getId());

        partialUpdatedProcessusInscriptionAdministrative
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

        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessusInscriptionAdministrative.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessusInscriptionAdministrative))
            )
            .andExpect(status().isOk());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        ProcessusInscriptionAdministrative testProcessusInscriptionAdministrative = processusInscriptionAdministrativeList.get(
            processusInscriptionAdministrativeList.size() - 1
        );
        assertThat(testProcessusInscriptionAdministrative.getInscriptionDemarreeYN()).isEqualTo(UPDATED_INSCRIPTION_DEMARREE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateDemarrageInscription()).isEqualTo(UPDATED_DATE_DEMARRAGE_INSCRIPTION);
        assertThat(testProcessusInscriptionAdministrative.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateAnnulationInscription()).isEqualTo(UPDATED_DATE_ANNULATION_INSCRIPTION);
        assertThat(testProcessusInscriptionAdministrative.getApteYN()).isEqualTo(UPDATED_APTE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateVisiteMedicale()).isEqualTo(UPDATED_DATE_VISITE_MEDICALE);
        assertThat(testProcessusInscriptionAdministrative.getBeneficiaireCROUSYN()).isEqualTo(UPDATED_BENEFICIAIRE_CROUSYN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseQuitusCROUS()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_CROUS);
        assertThat(testProcessusInscriptionAdministrative.getNouveauCodeBUYN()).isEqualTo(UPDATED_NOUVEAU_CODE_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getQuitusBUYN()).isEqualTo(UPDATED_QUITUS_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseQuitusBU()).isEqualTo(UPDATED_DATE_REMISE_QUITUS_BU);
        assertThat(testProcessusInscriptionAdministrative.getExporterBUYN()).isEqualTo(UPDATED_EXPORTER_BUYN);
        assertThat(testProcessusInscriptionAdministrative.getFraisObligatoiresPayesYN()).isEqualTo(UPDATED_FRAIS_OBLIGATOIRES_PAYES_YN);
        assertThat(testProcessusInscriptionAdministrative.getDatePaiementFraisObligatoires())
            .isEqualTo(UPDATED_DATE_PAIEMENT_FRAIS_OBLIGATOIRES);
        assertThat(testProcessusInscriptionAdministrative.getNumeroQuitusCROUS()).isEqualTo(UPDATED_NUMERO_QUITUS_CROUS);
        assertThat(testProcessusInscriptionAdministrative.getCarteEturemiseYN()).isEqualTo(UPDATED_CARTE_ETUREMISE_YN);
        assertThat(testProcessusInscriptionAdministrative.getCarteDupremiseYN()).isEqualTo(UPDATED_CARTE_DUPREMISE_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseCarteEtu()).isEqualTo(UPDATED_DATE_REMISE_CARTE_ETU);
        assertThat(testProcessusInscriptionAdministrative.getDateRemiseCarteDup()).isEqualTo(UPDATED_DATE_REMISE_CARTE_DUP);
        assertThat(testProcessusInscriptionAdministrative.getInscritAdministrativementYN())
            .isEqualTo(UPDATED_INSCRIT_ADMINISTRATIVEMENT_YN);
        assertThat(testProcessusInscriptionAdministrative.getDateInscriptionAdministrative())
            .isEqualTo(UPDATED_DATE_INSCRIPTION_ADMINISTRATIVE);
        assertThat(testProcessusInscriptionAdministrative.getDerniereModification()).isEqualTo(UPDATED_DERNIERE_MODIFICATION);
        assertThat(testProcessusInscriptionAdministrative.getInscritOnlineYN()).isEqualTo(UPDATED_INSCRIT_ONLINE_YN);
        assertThat(testProcessusInscriptionAdministrative.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void patchNonExistingProcessusInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusInscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        processusInscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusInscriptionAdministrative
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = processusInscriptionAdministrativeMapper.toDto(
            processusInscriptionAdministrative
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processusInscriptionAdministrativeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processusInscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessusInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusInscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        processusInscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusInscriptionAdministrative
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = processusInscriptionAdministrativeMapper.toDto(
            processusInscriptionAdministrative
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processusInscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessusInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = processusInscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        processusInscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the ProcessusInscriptionAdministrative
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = processusInscriptionAdministrativeMapper.toDto(
            processusInscriptionAdministrative
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessusInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processusInscriptionAdministrativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessusInscriptionAdministrative in the database
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProcessusInscriptionAdministrative() throws Exception {
        // Initialize the database
        processusInscriptionAdministrativeRepository.saveAndFlush(processusInscriptionAdministrative);
        processusInscriptionAdministrativeRepository.save(processusInscriptionAdministrative);
        processusInscriptionAdministrativeSearchRepository.save(processusInscriptionAdministrative);

        int databaseSizeBeforeDelete = processusInscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the processusInscriptionAdministrative
        restProcessusInscriptionAdministrativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, processusInscriptionAdministrative.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeList =
            processusInscriptionAdministrativeRepository.findAll();
        assertThat(processusInscriptionAdministrativeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(processusInscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProcessusInscriptionAdministrative() throws Exception {
        // Initialize the database
        processusInscriptionAdministrative = processusInscriptionAdministrativeRepository.saveAndFlush(processusInscriptionAdministrative);
        processusInscriptionAdministrativeSearchRepository.save(processusInscriptionAdministrative);

        // Search the processusInscriptionAdministrative
        restProcessusInscriptionAdministrativeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + processusInscriptionAdministrative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processusInscriptionAdministrative.getId().intValue())))
            .andExpect(jsonPath("$.[*].inscriptionDemarreeYN").value(hasItem(DEFAULT_INSCRIPTION_DEMARREE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDemarrageInscription").value(hasItem(DEFAULT_DATE_DEMARRAGE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].inscriptionAnnuleeYN").value(hasItem(DEFAULT_INSCRIPTION_ANNULEE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAnnulationInscription").value(hasItem(DEFAULT_DATE_ANNULATION_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].apteYN").value(hasItem(DEFAULT_APTE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateVisiteMedicale").value(hasItem(DEFAULT_DATE_VISITE_MEDICALE.toString())))
            .andExpect(jsonPath("$.[*].beneficiaireCROUSYN").value(hasItem(DEFAULT_BENEFICIAIRE_CROUSYN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateRemiseQuitusCROUS").value(hasItem(DEFAULT_DATE_REMISE_QUITUS_CROUS.toString())))
            .andExpect(jsonPath("$.[*].nouveauCodeBUYN").value(hasItem(DEFAULT_NOUVEAU_CODE_BUYN.booleanValue())))
            .andExpect(jsonPath("$.[*].quitusBUYN").value(hasItem(DEFAULT_QUITUS_BUYN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateRemiseQuitusBU").value(hasItem(DEFAULT_DATE_REMISE_QUITUS_BU.toString())))
            .andExpect(jsonPath("$.[*].exporterBUYN").value(hasItem(DEFAULT_EXPORTER_BUYN.booleanValue())))
            .andExpect(jsonPath("$.[*].fraisObligatoiresPayesYN").value(hasItem(DEFAULT_FRAIS_OBLIGATOIRES_PAYES_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].datePaiementFraisObligatoires").value(hasItem(DEFAULT_DATE_PAIEMENT_FRAIS_OBLIGATOIRES.toString())))
            .andExpect(jsonPath("$.[*].numeroQuitusCROUS").value(hasItem(DEFAULT_NUMERO_QUITUS_CROUS)))
            .andExpect(jsonPath("$.[*].carteEturemiseYN").value(hasItem(DEFAULT_CARTE_ETUREMISE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].carteDupremiseYN").value(hasItem(DEFAULT_CARTE_DUPREMISE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateRemiseCarteEtu").value(hasItem(DEFAULT_DATE_REMISE_CARTE_ETU.toString())))
            .andExpect(jsonPath("$.[*].dateRemiseCarteDup").value(hasItem(DEFAULT_DATE_REMISE_CARTE_DUP)))
            .andExpect(jsonPath("$.[*].inscritAdministrativementYN").value(hasItem(DEFAULT_INSCRIT_ADMINISTRATIVEMENT_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateInscriptionAdministrative").value(hasItem(DEFAULT_DATE_INSCRIPTION_ADMINISTRATIVE.toString())))
            .andExpect(jsonPath("$.[*].derniereModification").value(hasItem(DEFAULT_DERNIERE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].inscritOnlineYN").value(hasItem(DEFAULT_INSCRIT_ONLINE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].emailUser").value(hasItem(DEFAULT_EMAIL_USER)));
    }
}
