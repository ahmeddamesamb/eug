package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
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
import sn.ugb.gir.domain.FormationPrivee;
import sn.ugb.gir.repository.FormationPriveeRepository;
import sn.ugb.gir.repository.search.FormationPriveeSearchRepository;
import sn.ugb.gir.service.dto.FormationPriveeDTO;
import sn.ugb.gir.service.mapper.FormationPriveeMapper;

/**
 * Integration tests for the {@link FormationPriveeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationPriveeResourceIT {

    private static final Integer DEFAULT_NOMBRE_MENSUALITES = 1;
    private static final Integer UPDATED_NOMBRE_MENSUALITES = 2;

    private static final Boolean DEFAULT_PAIEMENT_PREMIER_MOIS_YN = false;
    private static final Boolean UPDATED_PAIEMENT_PREMIER_MOIS_YN = true;

    private static final Boolean DEFAULT_PAIEMENT_DERNIER_MOIS_YN = false;
    private static final Boolean UPDATED_PAIEMENT_DERNIER_MOIS_YN = true;

    private static final Boolean DEFAULT_FRAIS_DOSSIER_YN = false;
    private static final Boolean UPDATED_FRAIS_DOSSIER_YN = true;

    private static final Float DEFAULT_COUT_TOTAL = 1F;
    private static final Float UPDATED_COUT_TOTAL = 2F;

    private static final Float DEFAULT_MENSUALITE = 1F;
    private static final Float UPDATED_MENSUALITE = 2F;

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/formation-privees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/formation-privees/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationPriveeRepository formationPriveeRepository;

    @Autowired
    private FormationPriveeMapper formationPriveeMapper;

    @Autowired
    private FormationPriveeSearchRepository formationPriveeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationPriveeMockMvc;

    private FormationPrivee formationPrivee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationPrivee createEntity(EntityManager em) {
        FormationPrivee formationPrivee = new FormationPrivee()
            .nombreMensualites(DEFAULT_NOMBRE_MENSUALITES)
            .paiementPremierMoisYN(DEFAULT_PAIEMENT_PREMIER_MOIS_YN)
            .paiementDernierMoisYN(DEFAULT_PAIEMENT_DERNIER_MOIS_YN)
            .fraisDossierYN(DEFAULT_FRAIS_DOSSIER_YN)
            .coutTotal(DEFAULT_COUT_TOTAL)
            .mensualite(DEFAULT_MENSUALITE)
            .actifYN(DEFAULT_ACTIF_YN);
        return formationPrivee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationPrivee createUpdatedEntity(EntityManager em) {
        FormationPrivee formationPrivee = new FormationPrivee()
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .paiementPremierMoisYN(UPDATED_PAIEMENT_PREMIER_MOIS_YN)
            .paiementDernierMoisYN(UPDATED_PAIEMENT_DERNIER_MOIS_YN)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .coutTotal(UPDATED_COUT_TOTAL)
            .mensualite(UPDATED_MENSUALITE)
            .actifYN(UPDATED_ACTIF_YN);
        return formationPrivee;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        formationPriveeSearchRepository.deleteAll();
        assertThat(formationPriveeSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        formationPrivee = createEntity(em);
    }

    @Test
    @Transactional
    void createFormationPrivee() throws Exception {
        int databaseSizeBeforeCreate = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);
        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        FormationPrivee testFormationPrivee = formationPriveeList.get(formationPriveeList.size() - 1);
        assertThat(testFormationPrivee.getNombreMensualites()).isEqualTo(DEFAULT_NOMBRE_MENSUALITES);
        assertThat(testFormationPrivee.getPaiementPremierMoisYN()).isEqualTo(DEFAULT_PAIEMENT_PREMIER_MOIS_YN);
        assertThat(testFormationPrivee.getPaiementDernierMoisYN()).isEqualTo(DEFAULT_PAIEMENT_DERNIER_MOIS_YN);
        assertThat(testFormationPrivee.getFraisDossierYN()).isEqualTo(DEFAULT_FRAIS_DOSSIER_YN);
        assertThat(testFormationPrivee.getCoutTotal()).isEqualTo(DEFAULT_COUT_TOTAL);
        assertThat(testFormationPrivee.getMensualite()).isEqualTo(DEFAULT_MENSUALITE);
        assertThat(testFormationPrivee.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createFormationPriveeWithExistingId() throws Exception {
        // Create the FormationPrivee with an existing ID
        formationPrivee.setId(1L);
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        int databaseSizeBeforeCreate = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNombreMensualitesIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        // set the field null
        formationPrivee.setNombreMensualites(null);

        // Create the FormationPrivee, which fails.
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCoutTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        // set the field null
        formationPrivee.setCoutTotal(null);

        // Create the FormationPrivee, which fails.
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMensualiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        // set the field null
        formationPrivee.setMensualite(null);

        // Create the FormationPrivee, which fails.
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFormationPrivees() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        // Get all the formationPriveeList
        restFormationPriveeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationPrivee.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreMensualites").value(hasItem(DEFAULT_NOMBRE_MENSUALITES)))
            .andExpect(jsonPath("$.[*].paiementPremierMoisYN").value(hasItem(DEFAULT_PAIEMENT_PREMIER_MOIS_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].paiementDernierMoisYN").value(hasItem(DEFAULT_PAIEMENT_DERNIER_MOIS_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].fraisDossierYN").value(hasItem(DEFAULT_FRAIS_DOSSIER_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].coutTotal").value(hasItem(DEFAULT_COUT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].mensualite").value(hasItem(DEFAULT_MENSUALITE.doubleValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getFormationPrivee() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        // Get the formationPrivee
        restFormationPriveeMockMvc
            .perform(get(ENTITY_API_URL_ID, formationPrivee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formationPrivee.getId().intValue()))
            .andExpect(jsonPath("$.nombreMensualites").value(DEFAULT_NOMBRE_MENSUALITES))
            .andExpect(jsonPath("$.paiementPremierMoisYN").value(DEFAULT_PAIEMENT_PREMIER_MOIS_YN.booleanValue()))
            .andExpect(jsonPath("$.paiementDernierMoisYN").value(DEFAULT_PAIEMENT_DERNIER_MOIS_YN.booleanValue()))
            .andExpect(jsonPath("$.fraisDossierYN").value(DEFAULT_FRAIS_DOSSIER_YN.booleanValue()))
            .andExpect(jsonPath("$.coutTotal").value(DEFAULT_COUT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.mensualite").value(DEFAULT_MENSUALITE.doubleValue()))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFormationPrivee() throws Exception {
        // Get the formationPrivee
        restFormationPriveeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormationPrivee() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        formationPriveeSearchRepository.save(formationPrivee);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());

        // Update the formationPrivee
        FormationPrivee updatedFormationPrivee = formationPriveeRepository.findById(formationPrivee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormationPrivee are not directly saved in db
        em.detach(updatedFormationPrivee);
        updatedFormationPrivee
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .paiementPremierMoisYN(UPDATED_PAIEMENT_PREMIER_MOIS_YN)
            .paiementDernierMoisYN(UPDATED_PAIEMENT_DERNIER_MOIS_YN)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .coutTotal(UPDATED_COUT_TOTAL)
            .mensualite(UPDATED_MENSUALITE)
            .actifYN(UPDATED_ACTIF_YN);
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(updatedFormationPrivee);

        restFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationPriveeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        FormationPrivee testFormationPrivee = formationPriveeList.get(formationPriveeList.size() - 1);
        assertThat(testFormationPrivee.getNombreMensualites()).isEqualTo(UPDATED_NOMBRE_MENSUALITES);
        assertThat(testFormationPrivee.getPaiementPremierMoisYN()).isEqualTo(UPDATED_PAIEMENT_PREMIER_MOIS_YN);
        assertThat(testFormationPrivee.getPaiementDernierMoisYN()).isEqualTo(UPDATED_PAIEMENT_DERNIER_MOIS_YN);
        assertThat(testFormationPrivee.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
        assertThat(testFormationPrivee.getCoutTotal()).isEqualTo(UPDATED_COUT_TOTAL);
        assertThat(testFormationPrivee.getMensualite()).isEqualTo(UPDATED_MENSUALITE);
        assertThat(testFormationPrivee.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FormationPrivee> formationPriveeSearchList = IterableUtils.toList(formationPriveeSearchRepository.findAll());
                FormationPrivee testFormationPriveeSearch = formationPriveeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testFormationPriveeSearch.getNombreMensualites()).isEqualTo(UPDATED_NOMBRE_MENSUALITES);
                assertThat(testFormationPriveeSearch.getPaiementPremierMoisYN()).isEqualTo(UPDATED_PAIEMENT_PREMIER_MOIS_YN);
                assertThat(testFormationPriveeSearch.getPaiementDernierMoisYN()).isEqualTo(UPDATED_PAIEMENT_DERNIER_MOIS_YN);
                assertThat(testFormationPriveeSearch.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
                assertThat(testFormationPriveeSearch.getCoutTotal()).isEqualTo(UPDATED_COUT_TOTAL);
                assertThat(testFormationPriveeSearch.getMensualite()).isEqualTo(UPDATED_MENSUALITE);
                assertThat(testFormationPriveeSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationPriveeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFormationPriveeWithPatch() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();

        // Update the formationPrivee using partial update
        FormationPrivee partialUpdatedFormationPrivee = new FormationPrivee();
        partialUpdatedFormationPrivee.setId(formationPrivee.getId());

        partialUpdatedFormationPrivee.fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN).mensualite(UPDATED_MENSUALITE).actifYN(UPDATED_ACTIF_YN);

        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationPrivee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationPrivee))
            )
            .andExpect(status().isOk());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        FormationPrivee testFormationPrivee = formationPriveeList.get(formationPriveeList.size() - 1);
        assertThat(testFormationPrivee.getNombreMensualites()).isEqualTo(DEFAULT_NOMBRE_MENSUALITES);
        assertThat(testFormationPrivee.getPaiementPremierMoisYN()).isEqualTo(DEFAULT_PAIEMENT_PREMIER_MOIS_YN);
        assertThat(testFormationPrivee.getPaiementDernierMoisYN()).isEqualTo(DEFAULT_PAIEMENT_DERNIER_MOIS_YN);
        assertThat(testFormationPrivee.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
        assertThat(testFormationPrivee.getCoutTotal()).isEqualTo(DEFAULT_COUT_TOTAL);
        assertThat(testFormationPrivee.getMensualite()).isEqualTo(UPDATED_MENSUALITE);
        assertThat(testFormationPrivee.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateFormationPriveeWithPatch() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();

        // Update the formationPrivee using partial update
        FormationPrivee partialUpdatedFormationPrivee = new FormationPrivee();
        partialUpdatedFormationPrivee.setId(formationPrivee.getId());

        partialUpdatedFormationPrivee
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .paiementPremierMoisYN(UPDATED_PAIEMENT_PREMIER_MOIS_YN)
            .paiementDernierMoisYN(UPDATED_PAIEMENT_DERNIER_MOIS_YN)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .coutTotal(UPDATED_COUT_TOTAL)
            .mensualite(UPDATED_MENSUALITE)
            .actifYN(UPDATED_ACTIF_YN);

        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationPrivee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationPrivee))
            )
            .andExpect(status().isOk());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        FormationPrivee testFormationPrivee = formationPriveeList.get(formationPriveeList.size() - 1);
        assertThat(testFormationPrivee.getNombreMensualites()).isEqualTo(UPDATED_NOMBRE_MENSUALITES);
        assertThat(testFormationPrivee.getPaiementPremierMoisYN()).isEqualTo(UPDATED_PAIEMENT_PREMIER_MOIS_YN);
        assertThat(testFormationPrivee.getPaiementDernierMoisYN()).isEqualTo(UPDATED_PAIEMENT_DERNIER_MOIS_YN);
        assertThat(testFormationPrivee.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
        assertThat(testFormationPrivee.getCoutTotal()).isEqualTo(UPDATED_COUT_TOTAL);
        assertThat(testFormationPrivee.getMensualite()).isEqualTo(UPDATED_MENSUALITE);
        assertThat(testFormationPrivee.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationPriveeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFormationPrivee() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);
        formationPriveeRepository.save(formationPrivee);
        formationPriveeSearchRepository.save(formationPrivee);

        int databaseSizeBeforeDelete = formationPriveeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the formationPrivee
        restFormationPriveeMockMvc
            .perform(delete(ENTITY_API_URL_ID, formationPrivee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationPriveeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFormationPrivee() throws Exception {
        // Initialize the database
        formationPrivee = formationPriveeRepository.saveAndFlush(formationPrivee);
        formationPriveeSearchRepository.save(formationPrivee);

        // Search the formationPrivee
        restFormationPriveeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + formationPrivee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationPrivee.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreMensualites").value(hasItem(DEFAULT_NOMBRE_MENSUALITES)))
            .andExpect(jsonPath("$.[*].paiementPremierMoisYN").value(hasItem(DEFAULT_PAIEMENT_PREMIER_MOIS_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].paiementDernierMoisYN").value(hasItem(DEFAULT_PAIEMENT_DERNIER_MOIS_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].fraisDossierYN").value(hasItem(DEFAULT_FRAIS_DOSSIER_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].coutTotal").value(hasItem(DEFAULT_COUT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].mensualite").value(hasItem(DEFAULT_MENSUALITE.doubleValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
