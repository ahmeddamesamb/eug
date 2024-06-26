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
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.repository.InscriptionAdministrativeFormationRepository;
import sn.ugb.gir.repository.search.InscriptionAdministrativeFormationSearchRepository;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;
import sn.ugb.gir.service.mapper.InscriptionAdministrativeFormationMapper;

/**
 * Integration tests for the {@link InscriptionAdministrativeFormationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InscriptionAdministrativeFormationResourceIT {

    private static final Boolean DEFAULT_INSCRIPTION_PRINCIPALE_YN = false;
    private static final Boolean UPDATED_INSCRIPTION_PRINCIPALE_YN = true;

    private static final Boolean DEFAULT_INSCRIPTION_ANNULEE_YN = false;
    private static final Boolean UPDATED_INSCRIPTION_ANNULEE_YN = true;

    private static final Boolean DEFAULT_EXONERE_YN = false;
    private static final Boolean UPDATED_EXONERE_YN = true;

    private static final Boolean DEFAULT_PAIEMENT_FRAIS_OBL_YN = false;
    private static final Boolean UPDATED_PAIEMENT_FRAIS_OBL_YN = true;

    private static final Boolean DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN = false;
    private static final Boolean UPDATED_PAIEMENT_FRAIS_INTEGERG_YN = true;

    private static final Boolean DEFAULT_CERTIFICAT_DELIVRE_YN = false;
    private static final Boolean UPDATED_CERTIFICAT_DELIVRE_YN = true;

    private static final Instant DEFAULT_DATE_CHOIX_FORMATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CHOIX_FORMATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_VALIDATION_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_VALIDATION_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/inscription-administrative-formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/inscription-administrative-formations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository;

    @Autowired
    private InscriptionAdministrativeFormationMapper inscriptionAdministrativeFormationMapper;

    @Autowired
    private InscriptionAdministrativeFormationSearchRepository inscriptionAdministrativeFormationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionAdministrativeFormationMockMvc;

    private InscriptionAdministrativeFormation inscriptionAdministrativeFormation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionAdministrativeFormation createEntity(EntityManager em) {
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation = new InscriptionAdministrativeFormation()
            .inscriptionPrincipaleYN(DEFAULT_INSCRIPTION_PRINCIPALE_YN)
            .inscriptionAnnuleeYN(DEFAULT_INSCRIPTION_ANNULEE_YN)
            .exonereYN(DEFAULT_EXONERE_YN)
            .paiementFraisOblYN(DEFAULT_PAIEMENT_FRAIS_OBL_YN)
            .paiementFraisIntegergYN(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN)
            .certificatDelivreYN(DEFAULT_CERTIFICAT_DELIVRE_YN)
            .dateChoixFormation(DEFAULT_DATE_CHOIX_FORMATION)
            .dateValidationInscription(DEFAULT_DATE_VALIDATION_INSCRIPTION);
        return inscriptionAdministrativeFormation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionAdministrativeFormation createUpdatedEntity(EntityManager em) {
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation = new InscriptionAdministrativeFormation()
            .inscriptionPrincipaleYN(UPDATED_INSCRIPTION_PRINCIPALE_YN)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .exonereYN(UPDATED_EXONERE_YN)
            .paiementFraisOblYN(UPDATED_PAIEMENT_FRAIS_OBL_YN)
            .paiementFraisIntegergYN(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN)
            .certificatDelivreYN(UPDATED_CERTIFICAT_DELIVRE_YN)
            .dateChoixFormation(UPDATED_DATE_CHOIX_FORMATION)
            .dateValidationInscription(UPDATED_DATE_VALIDATION_INSCRIPTION);
        return inscriptionAdministrativeFormation;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        inscriptionAdministrativeFormationSearchRepository.deleteAll();
        assertThat(inscriptionAdministrativeFormationSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        inscriptionAdministrativeFormation = createEntity(em);
    }

    @Test
    @Transactional
    void createInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeCreate = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        InscriptionAdministrativeFormation testInscriptionAdministrativeFormation = inscriptionAdministrativeFormationList.get(
            inscriptionAdministrativeFormationList.size() - 1
        );
        assertThat(testInscriptionAdministrativeFormation.getInscriptionPrincipaleYN()).isEqualTo(DEFAULT_INSCRIPTION_PRINCIPALE_YN);
        assertThat(testInscriptionAdministrativeFormation.getInscriptionAnnuleeYN()).isEqualTo(DEFAULT_INSCRIPTION_ANNULEE_YN);
        assertThat(testInscriptionAdministrativeFormation.getExonereYN()).isEqualTo(DEFAULT_EXONERE_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisOblYN()).isEqualTo(DEFAULT_PAIEMENT_FRAIS_OBL_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisIntegergYN()).isEqualTo(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN);
        assertThat(testInscriptionAdministrativeFormation.getCertificatDelivreYN()).isEqualTo(DEFAULT_CERTIFICAT_DELIVRE_YN);
        assertThat(testInscriptionAdministrativeFormation.getDateChoixFormation()).isEqualTo(DEFAULT_DATE_CHOIX_FORMATION);
        assertThat(testInscriptionAdministrativeFormation.getDateValidationInscription()).isEqualTo(DEFAULT_DATE_VALIDATION_INSCRIPTION);
    }

    @Test
    @Transactional
    void createInscriptionAdministrativeFormationWithExistingId() throws Exception {
        // Create the InscriptionAdministrativeFormation with an existing ID
        inscriptionAdministrativeFormation.setId(1L);
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        int databaseSizeBeforeCreate = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkExonereYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        // set the field null
        inscriptionAdministrativeFormation.setExonereYN(null);

        // Create the InscriptionAdministrativeFormation, which fails.
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        restInscriptionAdministrativeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllInscriptionAdministrativeFormations() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        // Get all the inscriptionAdministrativeFormationList
        restInscriptionAdministrativeFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionAdministrativeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].inscriptionPrincipaleYN").value(hasItem(DEFAULT_INSCRIPTION_PRINCIPALE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].inscriptionAnnuleeYN").value(hasItem(DEFAULT_INSCRIPTION_ANNULEE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].exonereYN").value(hasItem(DEFAULT_EXONERE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].paiementFraisOblYN").value(hasItem(DEFAULT_PAIEMENT_FRAIS_OBL_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].paiementFraisIntegergYN").value(hasItem(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].certificatDelivreYN").value(hasItem(DEFAULT_CERTIFICAT_DELIVRE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateChoixFormation").value(hasItem(DEFAULT_DATE_CHOIX_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].dateValidationInscription").value(hasItem(DEFAULT_DATE_VALIDATION_INSCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getInscriptionAdministrativeFormation() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        // Get the inscriptionAdministrativeFormation
        restInscriptionAdministrativeFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, inscriptionAdministrativeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscriptionAdministrativeFormation.getId().intValue()))
            .andExpect(jsonPath("$.inscriptionPrincipaleYN").value(DEFAULT_INSCRIPTION_PRINCIPALE_YN.booleanValue()))
            .andExpect(jsonPath("$.inscriptionAnnuleeYN").value(DEFAULT_INSCRIPTION_ANNULEE_YN.booleanValue()))
            .andExpect(jsonPath("$.exonereYN").value(DEFAULT_EXONERE_YN.booleanValue()))
            .andExpect(jsonPath("$.paiementFraisOblYN").value(DEFAULT_PAIEMENT_FRAIS_OBL_YN.booleanValue()))
            .andExpect(jsonPath("$.paiementFraisIntegergYN").value(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN.booleanValue()))
            .andExpect(jsonPath("$.certificatDelivreYN").value(DEFAULT_CERTIFICAT_DELIVRE_YN.booleanValue()))
            .andExpect(jsonPath("$.dateChoixFormation").value(DEFAULT_DATE_CHOIX_FORMATION.toString()))
            .andExpect(jsonPath("$.dateValidationInscription").value(DEFAULT_DATE_VALIDATION_INSCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInscriptionAdministrativeFormation() throws Exception {
        // Get the inscriptionAdministrativeFormation
        restInscriptionAdministrativeFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscriptionAdministrativeFormation() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        inscriptionAdministrativeFormationSearchRepository.save(inscriptionAdministrativeFormation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());

        // Update the inscriptionAdministrativeFormation
        InscriptionAdministrativeFormation updatedInscriptionAdministrativeFormation = inscriptionAdministrativeFormationRepository
            .findById(inscriptionAdministrativeFormation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInscriptionAdministrativeFormation are not directly saved in db
        em.detach(updatedInscriptionAdministrativeFormation);
        updatedInscriptionAdministrativeFormation
            .inscriptionPrincipaleYN(UPDATED_INSCRIPTION_PRINCIPALE_YN)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .exonereYN(UPDATED_EXONERE_YN)
            .paiementFraisOblYN(UPDATED_PAIEMENT_FRAIS_OBL_YN)
            .paiementFraisIntegergYN(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN)
            .certificatDelivreYN(UPDATED_CERTIFICAT_DELIVRE_YN)
            .dateChoixFormation(UPDATED_DATE_CHOIX_FORMATION)
            .dateValidationInscription(UPDATED_DATE_VALIDATION_INSCRIPTION);
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            updatedInscriptionAdministrativeFormation
        );

        restInscriptionAdministrativeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionAdministrativeFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        InscriptionAdministrativeFormation testInscriptionAdministrativeFormation = inscriptionAdministrativeFormationList.get(
            inscriptionAdministrativeFormationList.size() - 1
        );
        assertThat(testInscriptionAdministrativeFormation.getInscriptionPrincipaleYN()).isEqualTo(UPDATED_INSCRIPTION_PRINCIPALE_YN);
        assertThat(testInscriptionAdministrativeFormation.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testInscriptionAdministrativeFormation.getExonereYN()).isEqualTo(UPDATED_EXONERE_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisOblYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_OBL_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisIntegergYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN);
        assertThat(testInscriptionAdministrativeFormation.getCertificatDelivreYN()).isEqualTo(UPDATED_CERTIFICAT_DELIVRE_YN);
        assertThat(testInscriptionAdministrativeFormation.getDateChoixFormation()).isEqualTo(UPDATED_DATE_CHOIX_FORMATION);
        assertThat(testInscriptionAdministrativeFormation.getDateValidationInscription()).isEqualTo(UPDATED_DATE_VALIDATION_INSCRIPTION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationSearchList = IterableUtils.toList(
                    inscriptionAdministrativeFormationSearchRepository.findAll()
                );
                InscriptionAdministrativeFormation testInscriptionAdministrativeFormationSearch =
                    inscriptionAdministrativeFormationSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testInscriptionAdministrativeFormationSearch.getInscriptionPrincipaleYN())
                    .isEqualTo(UPDATED_INSCRIPTION_PRINCIPALE_YN);
                assertThat(testInscriptionAdministrativeFormationSearch.getInscriptionAnnuleeYN())
                    .isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
                assertThat(testInscriptionAdministrativeFormationSearch.getExonereYN()).isEqualTo(UPDATED_EXONERE_YN);
                assertThat(testInscriptionAdministrativeFormationSearch.getPaiementFraisOblYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_OBL_YN);
                assertThat(testInscriptionAdministrativeFormationSearch.getPaiementFraisIntegergYN())
                    .isEqualTo(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN);
                assertThat(testInscriptionAdministrativeFormationSearch.getCertificatDelivreYN()).isEqualTo(UPDATED_CERTIFICAT_DELIVRE_YN);
                assertThat(testInscriptionAdministrativeFormationSearch.getDateChoixFormation()).isEqualTo(UPDATED_DATE_CHOIX_FORMATION);
                assertThat(testInscriptionAdministrativeFormationSearch.getDateValidationInscription())
                    .isEqualTo(UPDATED_DATE_VALIDATION_INSCRIPTION);
            });
    }

    @Test
    @Transactional
    void putNonExistingInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionAdministrativeFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionAdministrativeFormationWithPatch() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();

        // Update the inscriptionAdministrativeFormation using partial update
        InscriptionAdministrativeFormation partialUpdatedInscriptionAdministrativeFormation = new InscriptionAdministrativeFormation();
        partialUpdatedInscriptionAdministrativeFormation.setId(inscriptionAdministrativeFormation.getId());

        partialUpdatedInscriptionAdministrativeFormation
            .paiementFraisOblYN(UPDATED_PAIEMENT_FRAIS_OBL_YN)
            .certificatDelivreYN(UPDATED_CERTIFICAT_DELIVRE_YN)
            .dateChoixFormation(UPDATED_DATE_CHOIX_FORMATION);

        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionAdministrativeFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscriptionAdministrativeFormation))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        InscriptionAdministrativeFormation testInscriptionAdministrativeFormation = inscriptionAdministrativeFormationList.get(
            inscriptionAdministrativeFormationList.size() - 1
        );
        assertThat(testInscriptionAdministrativeFormation.getInscriptionPrincipaleYN()).isEqualTo(DEFAULT_INSCRIPTION_PRINCIPALE_YN);
        assertThat(testInscriptionAdministrativeFormation.getInscriptionAnnuleeYN()).isEqualTo(DEFAULT_INSCRIPTION_ANNULEE_YN);
        assertThat(testInscriptionAdministrativeFormation.getExonereYN()).isEqualTo(DEFAULT_EXONERE_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisOblYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_OBL_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisIntegergYN()).isEqualTo(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN);
        assertThat(testInscriptionAdministrativeFormation.getCertificatDelivreYN()).isEqualTo(UPDATED_CERTIFICAT_DELIVRE_YN);
        assertThat(testInscriptionAdministrativeFormation.getDateChoixFormation()).isEqualTo(UPDATED_DATE_CHOIX_FORMATION);
        assertThat(testInscriptionAdministrativeFormation.getDateValidationInscription()).isEqualTo(DEFAULT_DATE_VALIDATION_INSCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateInscriptionAdministrativeFormationWithPatch() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();

        // Update the inscriptionAdministrativeFormation using partial update
        InscriptionAdministrativeFormation partialUpdatedInscriptionAdministrativeFormation = new InscriptionAdministrativeFormation();
        partialUpdatedInscriptionAdministrativeFormation.setId(inscriptionAdministrativeFormation.getId());

        partialUpdatedInscriptionAdministrativeFormation
            .inscriptionPrincipaleYN(UPDATED_INSCRIPTION_PRINCIPALE_YN)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .exonereYN(UPDATED_EXONERE_YN)
            .paiementFraisOblYN(UPDATED_PAIEMENT_FRAIS_OBL_YN)
            .paiementFraisIntegergYN(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN)
            .certificatDelivreYN(UPDATED_CERTIFICAT_DELIVRE_YN)
            .dateChoixFormation(UPDATED_DATE_CHOIX_FORMATION)
            .dateValidationInscription(UPDATED_DATE_VALIDATION_INSCRIPTION);

        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionAdministrativeFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscriptionAdministrativeFormation))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        InscriptionAdministrativeFormation testInscriptionAdministrativeFormation = inscriptionAdministrativeFormationList.get(
            inscriptionAdministrativeFormationList.size() - 1
        );
        assertThat(testInscriptionAdministrativeFormation.getInscriptionPrincipaleYN()).isEqualTo(UPDATED_INSCRIPTION_PRINCIPALE_YN);
        assertThat(testInscriptionAdministrativeFormation.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testInscriptionAdministrativeFormation.getExonereYN()).isEqualTo(UPDATED_EXONERE_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisOblYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_OBL_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisIntegergYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN);
        assertThat(testInscriptionAdministrativeFormation.getCertificatDelivreYN()).isEqualTo(UPDATED_CERTIFICAT_DELIVRE_YN);
        assertThat(testInscriptionAdministrativeFormation.getDateChoixFormation()).isEqualTo(UPDATED_DATE_CHOIX_FORMATION);
        assertThat(testInscriptionAdministrativeFormation.getDateValidationInscription()).isEqualTo(UPDATED_DATE_VALIDATION_INSCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscriptionAdministrativeFormationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteInscriptionAdministrativeFormation() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);
        inscriptionAdministrativeFormationRepository.save(inscriptionAdministrativeFormation);
        inscriptionAdministrativeFormationSearchRepository.save(inscriptionAdministrativeFormation);

        int databaseSizeBeforeDelete = inscriptionAdministrativeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the inscriptionAdministrativeFormation
        restInscriptionAdministrativeFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscriptionAdministrativeFormation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchInscriptionAdministrativeFormation() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormation = inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);
        inscriptionAdministrativeFormationSearchRepository.save(inscriptionAdministrativeFormation);

        // Search the inscriptionAdministrativeFormation
        restInscriptionAdministrativeFormationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + inscriptionAdministrativeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionAdministrativeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].inscriptionPrincipaleYN").value(hasItem(DEFAULT_INSCRIPTION_PRINCIPALE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].inscriptionAnnuleeYN").value(hasItem(DEFAULT_INSCRIPTION_ANNULEE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].exonereYN").value(hasItem(DEFAULT_EXONERE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].paiementFraisOblYN").value(hasItem(DEFAULT_PAIEMENT_FRAIS_OBL_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].paiementFraisIntegergYN").value(hasItem(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].certificatDelivreYN").value(hasItem(DEFAULT_CERTIFICAT_DELIVRE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dateChoixFormation").value(hasItem(DEFAULT_DATE_CHOIX_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].dateValidationInscription").value(hasItem(DEFAULT_DATE_VALIDATION_INSCRIPTION.toString())));
    }
}
