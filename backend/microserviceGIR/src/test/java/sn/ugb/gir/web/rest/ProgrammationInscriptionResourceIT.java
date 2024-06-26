package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.ugb.gir.domain.ProgrammationInscription;
import sn.ugb.gir.repository.ProgrammationInscriptionRepository;
import sn.ugb.gir.repository.search.ProgrammationInscriptionSearchRepository;
import sn.ugb.gir.service.dto.ProgrammationInscriptionDTO;
import sn.ugb.gir.service.mapper.ProgrammationInscriptionMapper;

/**
 * Integration tests for the {@link ProgrammationInscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgrammationInscriptionResourceIT {

    private static final String DEFAULT_LIBELLE_PROGRAMMATION = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_PROGRAMMATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT_PROGRAMMATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_PROGRAMMATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN_PROGRAMMATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_PROGRAMMATION = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_OUVERT_YN = false;
    private static final Boolean UPDATED_OUVERT_YN = true;

    private static final String DEFAULT_EMAIL_USER = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_USER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_FORCLOS_CLASSE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FORCLOS_CLASSE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_FORCLOS_CLASSE_YN = false;
    private static final Boolean UPDATED_FORCLOS_CLASSE_YN = true;

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/programmation-inscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/programmation-inscriptions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgrammationInscriptionRepository programmationInscriptionRepository;

    @Autowired
    private ProgrammationInscriptionMapper programmationInscriptionMapper;

    @Autowired
    private ProgrammationInscriptionSearchRepository programmationInscriptionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgrammationInscriptionMockMvc;

    private ProgrammationInscription programmationInscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgrammationInscription createEntity(EntityManager em) {
        ProgrammationInscription programmationInscription = new ProgrammationInscription()
            .libelleProgrammation(DEFAULT_LIBELLE_PROGRAMMATION)
            .dateDebutProgrammation(DEFAULT_DATE_DEBUT_PROGRAMMATION)
            .dateFinProgrammation(DEFAULT_DATE_FIN_PROGRAMMATION)
            .ouvertYN(DEFAULT_OUVERT_YN)
            .emailUser(DEFAULT_EMAIL_USER)
            .dateForclosClasse(DEFAULT_DATE_FORCLOS_CLASSE)
            .forclosClasseYN(DEFAULT_FORCLOS_CLASSE_YN)
            .actifYN(DEFAULT_ACTIF_YN);
        return programmationInscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgrammationInscription createUpdatedEntity(EntityManager em) {
        ProgrammationInscription programmationInscription = new ProgrammationInscription()
            .libelleProgrammation(UPDATED_LIBELLE_PROGRAMMATION)
            .dateDebutProgrammation(UPDATED_DATE_DEBUT_PROGRAMMATION)
            .dateFinProgrammation(UPDATED_DATE_FIN_PROGRAMMATION)
            .ouvertYN(UPDATED_OUVERT_YN)
            .emailUser(UPDATED_EMAIL_USER)
            .dateForclosClasse(UPDATED_DATE_FORCLOS_CLASSE)
            .forclosClasseYN(UPDATED_FORCLOS_CLASSE_YN)
            .actifYN(UPDATED_ACTIF_YN);
        return programmationInscription;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        programmationInscriptionSearchRepository.deleteAll();
        assertThat(programmationInscriptionSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        programmationInscription = createEntity(em);
    }

    @Test
    @Transactional
    void createProgrammationInscription() throws Exception {
        int databaseSizeBeforeCreate = programmationInscriptionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);
        restProgrammationInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ProgrammationInscription testProgrammationInscription = programmationInscriptionList.get(programmationInscriptionList.size() - 1);
        assertThat(testProgrammationInscription.getLibelleProgrammation()).isEqualTo(DEFAULT_LIBELLE_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateDebutProgrammation()).isEqualTo(DEFAULT_DATE_DEBUT_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateFinProgrammation()).isEqualTo(DEFAULT_DATE_FIN_PROGRAMMATION);
        assertThat(testProgrammationInscription.getOuvertYN()).isEqualTo(DEFAULT_OUVERT_YN);
        assertThat(testProgrammationInscription.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
        assertThat(testProgrammationInscription.getDateForclosClasse()).isEqualTo(DEFAULT_DATE_FORCLOS_CLASSE);
        assertThat(testProgrammationInscription.getForclosClasseYN()).isEqualTo(DEFAULT_FORCLOS_CLASSE_YN);
        assertThat(testProgrammationInscription.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createProgrammationInscriptionWithExistingId() throws Exception {
        // Create the ProgrammationInscription with an existing ID
        programmationInscription.setId(1L);
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        int databaseSizeBeforeCreate = programmationInscriptionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgrammationInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProgrammationInscriptions() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        // Get all the programmationInscriptionList
        restProgrammationInscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programmationInscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleProgrammation").value(hasItem(DEFAULT_LIBELLE_PROGRAMMATION)))
            .andExpect(jsonPath("$.[*].dateDebutProgrammation").value(hasItem(DEFAULT_DATE_DEBUT_PROGRAMMATION.toString())))
            .andExpect(jsonPath("$.[*].dateFinProgrammation").value(hasItem(DEFAULT_DATE_FIN_PROGRAMMATION.toString())))
            .andExpect(jsonPath("$.[*].ouvertYN").value(hasItem(DEFAULT_OUVERT_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].emailUser").value(hasItem(DEFAULT_EMAIL_USER)))
            .andExpect(jsonPath("$.[*].dateForclosClasse").value(hasItem(DEFAULT_DATE_FORCLOS_CLASSE.toString())))
            .andExpect(jsonPath("$.[*].forclosClasseYN").value(hasItem(DEFAULT_FORCLOS_CLASSE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getProgrammationInscription() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        // Get the programmationInscription
        restProgrammationInscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, programmationInscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(programmationInscription.getId().intValue()))
            .andExpect(jsonPath("$.libelleProgrammation").value(DEFAULT_LIBELLE_PROGRAMMATION))
            .andExpect(jsonPath("$.dateDebutProgrammation").value(DEFAULT_DATE_DEBUT_PROGRAMMATION.toString()))
            .andExpect(jsonPath("$.dateFinProgrammation").value(DEFAULT_DATE_FIN_PROGRAMMATION.toString()))
            .andExpect(jsonPath("$.ouvertYN").value(DEFAULT_OUVERT_YN.booleanValue()))
            .andExpect(jsonPath("$.emailUser").value(DEFAULT_EMAIL_USER))
            .andExpect(jsonPath("$.dateForclosClasse").value(DEFAULT_DATE_FORCLOS_CLASSE.toString()))
            .andExpect(jsonPath("$.forclosClasseYN").value(DEFAULT_FORCLOS_CLASSE_YN.booleanValue()))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingProgrammationInscription() throws Exception {
        // Get the programmationInscription
        restProgrammationInscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProgrammationInscription() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        programmationInscriptionSearchRepository.save(programmationInscription);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());

        // Update the programmationInscription
        ProgrammationInscription updatedProgrammationInscription = programmationInscriptionRepository
            .findById(programmationInscription.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProgrammationInscription are not directly saved in db
        em.detach(updatedProgrammationInscription);
        updatedProgrammationInscription
            .libelleProgrammation(UPDATED_LIBELLE_PROGRAMMATION)
            .dateDebutProgrammation(UPDATED_DATE_DEBUT_PROGRAMMATION)
            .dateFinProgrammation(UPDATED_DATE_FIN_PROGRAMMATION)
            .ouvertYN(UPDATED_OUVERT_YN)
            .emailUser(UPDATED_EMAIL_USER)
            .dateForclosClasse(UPDATED_DATE_FORCLOS_CLASSE)
            .forclosClasseYN(UPDATED_FORCLOS_CLASSE_YN)
            .actifYN(UPDATED_ACTIF_YN);
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(updatedProgrammationInscription);

        restProgrammationInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programmationInscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        ProgrammationInscription testProgrammationInscription = programmationInscriptionList.get(programmationInscriptionList.size() - 1);
        assertThat(testProgrammationInscription.getLibelleProgrammation()).isEqualTo(UPDATED_LIBELLE_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateDebutProgrammation()).isEqualTo(UPDATED_DATE_DEBUT_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateFinProgrammation()).isEqualTo(UPDATED_DATE_FIN_PROGRAMMATION);
        assertThat(testProgrammationInscription.getOuvertYN()).isEqualTo(UPDATED_OUVERT_YN);
        assertThat(testProgrammationInscription.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
        assertThat(testProgrammationInscription.getDateForclosClasse()).isEqualTo(UPDATED_DATE_FORCLOS_CLASSE);
        assertThat(testProgrammationInscription.getForclosClasseYN()).isEqualTo(UPDATED_FORCLOS_CLASSE_YN);
        assertThat(testProgrammationInscription.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProgrammationInscription> programmationInscriptionSearchList = IterableUtils.toList(
                    programmationInscriptionSearchRepository.findAll()
                );
                ProgrammationInscription testProgrammationInscriptionSearch = programmationInscriptionSearchList.get(
                    searchDatabaseSizeAfter - 1
                );
                assertThat(testProgrammationInscriptionSearch.getLibelleProgrammation()).isEqualTo(UPDATED_LIBELLE_PROGRAMMATION);
                assertThat(testProgrammationInscriptionSearch.getDateDebutProgrammation()).isEqualTo(UPDATED_DATE_DEBUT_PROGRAMMATION);
                assertThat(testProgrammationInscriptionSearch.getDateFinProgrammation()).isEqualTo(UPDATED_DATE_FIN_PROGRAMMATION);
                assertThat(testProgrammationInscriptionSearch.getOuvertYN()).isEqualTo(UPDATED_OUVERT_YN);
                assertThat(testProgrammationInscriptionSearch.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
                assertThat(testProgrammationInscriptionSearch.getDateForclosClasse()).isEqualTo(UPDATED_DATE_FORCLOS_CLASSE);
                assertThat(testProgrammationInscriptionSearch.getForclosClasseYN()).isEqualTo(UPDATED_FORCLOS_CLASSE_YN);
                assertThat(testProgrammationInscriptionSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programmationInscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProgrammationInscriptionWithPatch() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();

        // Update the programmationInscription using partial update
        ProgrammationInscription partialUpdatedProgrammationInscription = new ProgrammationInscription();
        partialUpdatedProgrammationInscription.setId(programmationInscription.getId());

        partialUpdatedProgrammationInscription
            .libelleProgrammation(UPDATED_LIBELLE_PROGRAMMATION)
            .ouvertYN(UPDATED_OUVERT_YN)
            .emailUser(UPDATED_EMAIL_USER)
            .dateForclosClasse(UPDATED_DATE_FORCLOS_CLASSE)
            .forclosClasseYN(UPDATED_FORCLOS_CLASSE_YN)
            .actifYN(UPDATED_ACTIF_YN);

        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgrammationInscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgrammationInscription))
            )
            .andExpect(status().isOk());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        ProgrammationInscription testProgrammationInscription = programmationInscriptionList.get(programmationInscriptionList.size() - 1);
        assertThat(testProgrammationInscription.getLibelleProgrammation()).isEqualTo(UPDATED_LIBELLE_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateDebutProgrammation()).isEqualTo(DEFAULT_DATE_DEBUT_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateFinProgrammation()).isEqualTo(DEFAULT_DATE_FIN_PROGRAMMATION);
        assertThat(testProgrammationInscription.getOuvertYN()).isEqualTo(UPDATED_OUVERT_YN);
        assertThat(testProgrammationInscription.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
        assertThat(testProgrammationInscription.getDateForclosClasse()).isEqualTo(UPDATED_DATE_FORCLOS_CLASSE);
        assertThat(testProgrammationInscription.getForclosClasseYN()).isEqualTo(UPDATED_FORCLOS_CLASSE_YN);
        assertThat(testProgrammationInscription.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateProgrammationInscriptionWithPatch() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();

        // Update the programmationInscription using partial update
        ProgrammationInscription partialUpdatedProgrammationInscription = new ProgrammationInscription();
        partialUpdatedProgrammationInscription.setId(programmationInscription.getId());

        partialUpdatedProgrammationInscription
            .libelleProgrammation(UPDATED_LIBELLE_PROGRAMMATION)
            .dateDebutProgrammation(UPDATED_DATE_DEBUT_PROGRAMMATION)
            .dateFinProgrammation(UPDATED_DATE_FIN_PROGRAMMATION)
            .ouvertYN(UPDATED_OUVERT_YN)
            .emailUser(UPDATED_EMAIL_USER)
            .dateForclosClasse(UPDATED_DATE_FORCLOS_CLASSE)
            .forclosClasseYN(UPDATED_FORCLOS_CLASSE_YN)
            .actifYN(UPDATED_ACTIF_YN);

        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgrammationInscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgrammationInscription))
            )
            .andExpect(status().isOk());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        ProgrammationInscription testProgrammationInscription = programmationInscriptionList.get(programmationInscriptionList.size() - 1);
        assertThat(testProgrammationInscription.getLibelleProgrammation()).isEqualTo(UPDATED_LIBELLE_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateDebutProgrammation()).isEqualTo(UPDATED_DATE_DEBUT_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateFinProgrammation()).isEqualTo(UPDATED_DATE_FIN_PROGRAMMATION);
        assertThat(testProgrammationInscription.getOuvertYN()).isEqualTo(UPDATED_OUVERT_YN);
        assertThat(testProgrammationInscription.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
        assertThat(testProgrammationInscription.getDateForclosClasse()).isEqualTo(UPDATED_DATE_FORCLOS_CLASSE);
        assertThat(testProgrammationInscription.getForclosClasseYN()).isEqualTo(UPDATED_FORCLOS_CLASSE_YN);
        assertThat(testProgrammationInscription.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programmationInscriptionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProgrammationInscription() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);
        programmationInscriptionRepository.save(programmationInscription);
        programmationInscriptionSearchRepository.save(programmationInscription);

        int databaseSizeBeforeDelete = programmationInscriptionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the programmationInscription
        restProgrammationInscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, programmationInscription.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(programmationInscriptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProgrammationInscription() throws Exception {
        // Initialize the database
        programmationInscription = programmationInscriptionRepository.saveAndFlush(programmationInscription);
        programmationInscriptionSearchRepository.save(programmationInscription);

        // Search the programmationInscription
        restProgrammationInscriptionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + programmationInscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programmationInscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleProgrammation").value(hasItem(DEFAULT_LIBELLE_PROGRAMMATION)))
            .andExpect(jsonPath("$.[*].dateDebutProgrammation").value(hasItem(DEFAULT_DATE_DEBUT_PROGRAMMATION.toString())))
            .andExpect(jsonPath("$.[*].dateFinProgrammation").value(hasItem(DEFAULT_DATE_FIN_PROGRAMMATION.toString())))
            .andExpect(jsonPath("$.[*].ouvertYN").value(hasItem(DEFAULT_OUVERT_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].emailUser").value(hasItem(DEFAULT_EMAIL_USER)))
            .andExpect(jsonPath("$.[*].dateForclosClasse").value(hasItem(DEFAULT_DATE_FORCLOS_CLASSE.toString())))
            .andExpect(jsonPath("$.[*].forclosClasseYN").value(hasItem(DEFAULT_FORCLOS_CLASSE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
